package com.thibaultdelor.JSQL.join;

import com.thibaultdelor.JSQL.SQLOutputable;
import com.thibaultdelor.JSQL.Table;
import com.thibaultdelor.JSQL.criteria.Criterion;

public class OnJoin implements ExplicitJoin{

	private final Table joinTable;
	private final Criterion criterion;
	private final SQLOutputable joinType;
	
	public OnJoin(SQLOutputable joinType, Table joinTable, Criterion criterion) {
		super();
		this.joinTable = joinTable;
		this.criterion = criterion;
		this.joinType = joinType;
	}
	
	public OnJoin(Table joinTable, Criterion criterion) {
		this(JoinType.INNER_JOIN, joinTable, criterion);
	}

	@Override
	public void output(StringBuilder sb, SQLContext context) {
		joinType.output(sb, context);
		joinTable.output(sb, context);
		sb.append(" ON (");
		criterion.output(sb, context);
		sb.append(")");
	}

	@Override
	public Table getJoinTable() {
		return joinTable;
	}
}
