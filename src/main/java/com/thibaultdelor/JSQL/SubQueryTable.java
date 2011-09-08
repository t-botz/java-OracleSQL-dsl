package com.thibaultdelor.JSQL;

import java.util.Set;

public class SubQueryTable implements ITable {

	private final String alias;
	private final SelectQuery query;
	
	public SubQueryTable(SelectQuery query, String alias) {
		if(alias==null)
			throw new ExceptionInInitializerError("alias cannot be null!");
		this.alias = alias;

		if(query==null)
			throw new ExceptionInInitializerError("alias cannot be null!");
		this.query = query;
	}

	@Override
	public void output(StringBuilder sb, SQLContext context) {
		switch (context) {
		case FROM:
			sb.append('(');
			sb.append(query.toSQLStringBuilder());
			sb.append(") ");
			sb.append(alias);
			break;
		case WHERE:
			sb.append(query.toSQLStringBuilder());
			break;

		default:
			throw new RuntimeException("Invalid context " + context + " for "
					+ this);
		}

	}

	@Override
	public void addNeededTables(Set<Table> tables) {
	}

	@Override
	public String getAlias() {
		return alias;
	}

	@Override
	public Column get(String colName) {
		return new Column(this, colName);
	}

	@Override
	public Column get(Column col) {
		return get(col.getName());
	}

	/**
	 * Gets the sub-query.
	 *
	 * @return the sub-query
	 */
	public SelectQuery getSubQuery() {
		return query;
	}

	
}
