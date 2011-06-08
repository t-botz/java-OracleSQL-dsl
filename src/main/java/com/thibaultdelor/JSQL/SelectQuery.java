package com.thibaultdelor.JSQL;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.thibaultdelor.JSQL.SQLOutputable.SQLContext;
import com.thibaultdelor.JSQL.criteria.BinaryCriterion;
import com.thibaultdelor.JSQL.criteria.BinaryCriterion.BinaryOperator;
import com.thibaultdelor.JSQL.criteria.Criterion;
import com.thibaultdelor.JSQL.criteria.InCriterion;
import com.thibaultdelor.JSQL.criteria.LogicalExpression;
import com.thibaultdelor.JSQL.join.ExplicitJoin;
import com.thibaultdelor.JSQL.join.ExplicitJoin.JoinType;
import com.thibaultdelor.JSQL.join.ImplicitJoin;
import com.thibaultdelor.JSQL.join.JoinClause;
import com.thibaultdelor.JSQL.join.OnJoin;
import com.thibaultdelor.JSQL.literal.LiteralSet;

public class SelectQuery {

	private final List<SQLOutputable> hints = new ArrayList<SQLOutputable>(0);
	private final List<SQLOutputable> columns = new ArrayList<SQLOutputable>();
	private final LinkedHashSet<ITable> from = new LinkedHashSet<ITable>();
	private final List<JoinClause> join = new ArrayList<JoinClause>();
	private final LogicalExpression where = new LogicalExpression("", "\nAND ",
			"");
	private final List<SQLOutputable> groupBy = new ArrayList<SQLOutputable>();
	private final List<Criterion> having = new ArrayList<Criterion>();
	private final List<OrderClause> orderBy = new ArrayList<OrderClause>();

	private final Set<Table> allReferencedTables = new HashSet<Table>(4);
	
	
	/** The join resolver use for auto join (lazy initialized). */
	private JoinResolver joinResolver = null;

	public SelectQuery select(SQLOutputable... cols) {
		for (SQLOutputable c : cols) {
			if (columns.add(c))
				c.addNeededTables(allReferencedTables);
		}
		return this;
	}

	public SelectQuery hints(SQLOutputable... hints) {
		for (SQLOutputable hint : hints) {
			if (this.hints.add(hint))
				hint.addNeededTables(allReferencedTables);
		}
		return this;
	}

	public SelectQuery from(ITable t) {
		if (from.add(t))
			t.addNeededTables(allReferencedTables);
		return this;
	}

	public SelectQuery join(JoinClause jc) {
		if (join.add(jc)) {
			if (jc instanceof ImplicitJoin) {
				ImplicitJoin implicitJoin = (ImplicitJoin) jc;
				from(implicitJoin.getJoinTable());
				where(implicitJoin.getCriterion());
			} else
				((ExplicitJoin) jc).addNeededTables(allReferencedTables);
		}
		return this;
	}

	public SelectQuery join(ITable joinTable, Criterion criterion) {
		return join(new ImplicitJoin(joinTable, criterion));
	}

	public SelectQuery join(ITable joinTable, Column mycolumn,
			Column foreigncolumn) {
		return join(joinTable, new BinaryCriterion(mycolumn,
				BinaryOperator.EQUAL, foreigncolumn));
	}

	public SelectQuery join(JoinType type, ITable joinTable, Criterion criterion) {
		return join(new OnJoin(type, joinTable, criterion));
	}

	public SelectQuery join(JoinType type, ITable joinTable, Column mycolumn,
			Column foreigncolumn) {
		return join(type, joinTable, new BinaryCriterion(mycolumn,
				BinaryOperator.EQUAL, foreigncolumn));
	}

	public SelectQuery where(Criterion c) {
		where.add(c);
		c.addNeededTables(allReferencedTables);
		return this;
	}

	public SelectQuery whereIn(Column col, String... values) {
		return where(new InCriterion(col, new LiteralSet(values)));
	}
	public SelectQuery whereIn(Column col, SubQueryTable sq) {
		return where(new InCriterion(col, sq));
	}

	public SelectQuery groupBy(SQLOutputable groupCol) {
		if (groupBy.add(groupCol))
			groupCol.addNeededTables(allReferencedTables);
		return this;

	}

	public SelectQuery having(Criterion c) {
		if (having.add(c))
			c.addNeededTables(allReferencedTables);
		return this;
	}

	public SelectQuery orderBy(OrderClause oc) {
		if (orderBy.add(oc))
			oc.addNeededTables(allReferencedTables);
		return this;
	}

	public SelectQuery orderBy(Column c, boolean asc) {
		return orderBy(new OrderClause(c, asc));
	}

	
	public SubQueryTable as(String alias){
		return new SubQueryTable(this, alias);
	}
	/**
	 * Add all necessary missing tables in the from clause.
	 */
	public void autoAddFrom() {
		Set<Table> missingTables = getMissingTables();

		for (Table table : missingTables) {
			from.add(table);
		}
	}

	/**
	 * Based on declared foreign keys, it joins all missing table.<br />
	 * <br />
	 * 
	 * <b>syntax sugar for autoJoin(JoinType.INNER_JOIN, 1);</b>
	 * 
	 * @see SelectQuery#autoJoin(JoinType, int)
	 */
	public void autoJoin() {
		autoJoin(JoinType.INNER_JOIN, 0);
	}

	/**
	 * Auto join missing tables. It looks for the most optimum joins sequence.
	 * The depth of this algorithms is the maximum number of joins allowed per
	 * missing table.<br/>
	 * <br/>
	 * The criteria for determining the optimum join sequence are the number of
	 * joins clause necessary, and then the order of the declaration of foreign
	 * keys.<br />
	 * e.g. if for one table to autojoin the first declared foreign key
	 * reference an existing table, it will be joined thanks to its foreign key
	 * because no other foreign keys can do better.<br/>
	 * On the other hand if the first key reference a table that is not in the query
	 * but which has a foreign key that reference an existing table, we will need
	 * two join clause. So, we will check if the next key of our table reference
	 * an existing table, if yes this keys will be used since we just need only
	 * one join clause.
	 * 
	 * 
	 * @param joinType
	 *            the join type to use
	 * @param depth
	 *            the maximum depth of foreign key relation between tables, 0 for unlimited
	 */
	public void autoJoin(JoinType joinType, int depth) {
		if(depth<0)
			throw new IllegalArgumentException("depth mustn't be negative");
		if(depth==0)
			depth = Integer.MAX_VALUE;
		if(joinResolver == null){
			joinResolver = new JoinResolver(this, depth);
		}
		else {
			joinResolver.setDepth(depth);
		}
		joinResolver.resolve(joinType);
	}
	
	Set<ITable> getExistingTables() {
		Set<ITable> joinedTables = new HashSet<ITable>(from);
		for (JoinClause jc : join) {
			joinedTables.add(jc.getJoinTable());
		}
		return joinedTables;
	}
	Set<Table> getMissingTables() {
		Set<Table> missingTables = new HashSet<Table>(allReferencedTables);
		missingTables.removeAll(from);
		Set<Table> joinedTables = new HashSet<Table>(4);
		for (JoinClause jc : join) {
			if (jc.getJoinTable() instanceof Table) {
				joinedTables.add((Table) jc.getJoinTable());
			}
		}
		missingTables.removeAll(joinedTables);
		return missingTables;
	}

	public StringBuilder toSQLStringBuilder() {
		StringBuilder query = new StringBuilder();
		appendSelect(query);
		appendFrom(query);
		appendWhere(query);
		appendGroupBy(query);
		appendHaving(query);
		appendOrderBy(query);

		return query;
	}
	
	public String toSQLString() {
		return toSQLStringBuilder().toString();
	}

	private void appendSelect(StringBuilder query) {
		query.append("SELECT ");
		if (hints.size() > 0) {
			query.append("/*+ ");
			OutputUtils.strJoin(hints, " ", query, SQLContext.SELECT);
			query.append(" */ ");
		}
		OutputUtils.strJoin(columns, ", ", query, SQLContext.SELECT);
	}

	private void appendFrom(StringBuilder query) {
		if(from.isEmpty())
			throw new IllegalStateException("No Table in from clause!");
		// Implicit join
		query.append("\nFROM ");
		OutputUtils.strJoin(from, ", ", query, SQLContext.FROM);

		// Explicit join
		List<SQLOutputable> explicitJoins = new ArrayList<SQLOutputable>();
		for (JoinClause join : this.join) {
			if (join instanceof SQLOutputable) {
				explicitJoins.add((SQLOutputable) join);
			}
		}
		if (explicitJoins.size() > 0) {
			query.append("\n");
			OutputUtils.strJoin(explicitJoins, "\n", query, SQLContext.FROM);
		}
	}

	private void appendWhere(StringBuilder query) {
		if (where.size() == 0)
			return;

		query.append("\nWHERE ");
		where.output(query, SQLContext.WHERE);
	}

	private void appendGroupBy(StringBuilder query) {
		if (groupBy.size() == 0)
			return;

		query.append("\nGROUP BY ");
		OutputUtils.strJoin(groupBy, ", ", query, SQLContext.GROUPBY);
	}

	private void appendHaving(StringBuilder query) {
		if (having.size() == 0)
			return;

		query.append("\nHAVING ");
		OutputUtils.strJoin(having, "\nAND ", query, SQLContext.HAVING);
	}

	private void appendOrderBy(StringBuilder query) {
		if (orderBy.size() == 0)
			return;

		query.append("\nORDER BY ");

		OutputUtils.strJoin(orderBy, ", ", query, SQLContext.ORDER);
	}
}
