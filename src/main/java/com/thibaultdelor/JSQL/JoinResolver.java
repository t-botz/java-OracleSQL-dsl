/*
 * 
 */
package com.thibaultdelor.JSQL;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import com.thibaultdelor.JSQL.Table.ForeignKey;
import com.thibaultdelor.JSQL.criteria.BinaryCriterion;
import com.thibaultdelor.JSQL.criteria.BinaryCriterion.BinaryOperator;
import com.thibaultdelor.JSQL.join.ExplicitJoin.JoinType;
import com.thibaultdelor.JSQL.join.JoinClause;
import com.thibaultdelor.JSQL.join.OnJoin;

/**
 * JoinResolver allows to find the best list of joins to add.
 */
@SuppressWarnings(value = "all")
public class JoinResolver {

	private final SelectQuery query;

	private int depth;
	List<TableNode> nodes = new ArrayList<JoinResolver.TableNode>();

	/**
	 * Constructor of JoinResolver.
	 * 
	 * @param query
	 *            the query to complete
	 * @param depth
	 *            the maximum number of joins to add by table
	 */
	public JoinResolver(SelectQuery query, int depth) {
		super();
		this.query = query;
		this.depth = depth;
	}

	public int getDepth() {
		return depth;
	}

	public void setDepth(int depth) {
		this.depth = depth;
	}

	/**
	 * Auto add join clauses thanks to foreign keys.
	 * 
	 * @param joinType
	 *            the join type
	 */
	public void resolve(JoinType joinType) {
		Set<ITable> existingTables = query.getExistingTables();
		buildNodes(existingTables);
		Set<Table> missingTables = query.getMissingTables();
		if (existingTables.size() == 0) {
			if (missingTables.size() == 0)
				throw new IllegalArgumentException(
						"No tables in query nor tables to join!?!");
			else {
				Table firstTable = missingTables.iterator().next();
				query.from(firstTable);
				existingTables.add(firstTable);
				missingTables.remove(firstTable);
			}
		}

		for (Table table : missingTables) {
			TableNode bestNode = findNodeByTableName(table.getName());

			if (bestNode == null || bestNode.depth < 0) {
				StringBuilder msg = new StringBuilder(
						"Cannot auto-resolve the table ");
				msg.append(table.getName());
				msg.append(" with existing tableNode : [");
				for (TableNode node : nodes) {
					msg.append(node.toString() + ",");
				}
				msg.deleteCharAt(msg.length() - 1);
				msg.append("]");
				throw new IllegalStateException(msg.toString());
			}
			includeNodeInQuery(bestNode, joinType);

		}

	}

	/**
	 * Builds the nodes hierarchy thanks to foreign keys of existing tables.
	 * 
	 * @param existingTables
	 *            the existing tables in query
	 */
	private void buildNodes(Set<ITable> existingTables) {
		nodes.clear();
		for (ITable table : existingTables) {
			if (table instanceof Table) {
				nodes.add(new TableNode((Table) table, 0, null, null, true));
			}
		}
		addJoinNodes(new ArrayList<JoinResolver.TableNode>(nodes));
	}

	/**
	 * Adds nodes in the nodes hierarchy.
	 * 
	 * @param baseNodes
	 *            nodes to explore
	 */
	private void addJoinNodes(Collection<TableNode> baseNodes) {
		ArrayList<TableNode> addedNode = new ArrayList<JoinResolver.TableNode>();
		for (TableNode tableNode : baseNodes) {
			if (tableNode.depth > depth)
				continue;

			for (ForeignKey fk : tableNode.table.getForeignKeys()) {
				Table remotetable = (Table) fk.getRemoteColumn().getTable();
				TableNode myNode = findNodeByTableName(remotetable.getName());
				if (myNode == null) {
					myNode = new TableNode(remotetable, tableNode.depth + 1, fk, tableNode, false);
					nodes.add(myNode);
					addedNode.add(myNode);
				} else {
					updateBestNode(myNode, tableNode);
				}
			}
		}
		if (addedNode.size() > 0)
			addJoinNodes(addedNode);

	}

	/**
	 * Update a node if needed with a a direct linked node.<br />
	 * e.g. If we have found a best path for baseNode, this path can be bettre
	 * too for myNode
	 * 
	 * @param myNode
	 *            the node to update
	 * @param baseNode
	 *            the origanal node
	 */
	private void updateBestNode(TableNode myNode, TableNode baseNode) {

		// if passing by myNode is better
		if ((baseNode.depth + 1) < myNode.depth
				|| (baseNode.inQuery && baseNode.depth + 1 == myNode.depth)) {

			// update myNode property to use this better path
			myNode.bestForeignKey = null;
			for (ForeignKey fk : myNode.table.getForeignKeys()) {
				if (((Table) fk.getRemoteColumn().getTable()).isSameTable(baseNode.table)) {
					myNode.bestForeignKey = fk;
					break;
				}
			}
			if (myNode.bestForeignKey == null)
				throw new RuntimeException("NOLink betwen node! mynode :"
						+ myNode.toString() + " ; basenode : "
						+ baseNode.toString());
			myNode.depth = baseNode.depth + 1;
			myNode.closestNode = baseNode;

			// recursively update childs
			for (ForeignKey fk : myNode.table.getForeignKeys()) {
				TableNode child = findNodeByTableName(((Table) fk.getRemoteColumn().getTable())
						.getName());
				if (child != null)
					updateBestNode(child, myNode);
			}
		}
	}

	/**
	 * Include the table of the node in thequery with a join Clause.
	 * 
	 * @param node
	 *            the node to include in query
	 * @param joinType
	 *            the join type
	 */
	private void includeNodeInQuery(TableNode node, JoinType joinType) {
		if (node.inQuery)
			return;
		includeNodeInQuery(node.closestNode, joinType);

		Column col = node.bestForeignKey.getPrimaryKey();
		Column fk = node.bestForeignKey.getForeignKey();
		BinaryCriterion criterion = new BinaryCriterion(col,
				BinaryOperator.EQUAL, fk);
		query.join(new OnJoin(node.table, criterion));
		node.inQuery = true;
	}

	private TableNode findNodeByTableName(String tableName) {
		for (TableNode node : nodes) {
			if (node.table.getName().equals(tableName))
				return node;
		}
		return null;
	}

	/**
	 * Represent a table of the database with extra information allowing to find
	 * the best path to existing tables in the database
	 */
	private static class TableNode {

		/** The base table. */
		Table table;

		/** The number of link needed to access an existing table . */
		int depth;

		/** The foreign key used to join tables. */
		ForeignKey bestForeignKey;

		/** The closest node. */
		TableNode closestNode;

		/** inQuery is true if the table is already joined in the query. */
		boolean inQuery;

		TableNode() {
		}

		TableNode(Table table, int depth, ForeignKey fk,
				TableNode closestNode, boolean inQuery) {
			this.table = table;
			this.depth = depth;
			this.bestForeignKey = fk;
			this.closestNode = closestNode;
			this.inQuery = inQuery;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result
					+ ((table.getName() == null) ? 0 : table.getName()
							.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			TableNode other = (TableNode) obj;
			if (table.getName() == null) {
				if (other.table.getName() != null)
					return false;
			} else if (!table.getName().equals(other.table.getName()))
				return false;
			return true;
		}

		@Override
		public String toString() {
			StringBuilder builder = new StringBuilder();
			builder.append("TableNode [table=");
			builder.append(table.getName());
			builder.append(", depth=");
			builder.append(depth);
			builder.append(", bestForeignKey=");
			builder.append(bestForeignKey);
			builder.append(", inQuery=");
			builder.append(inQuery);
			builder.append("]");
			return builder.toString();
		}

	}

}
