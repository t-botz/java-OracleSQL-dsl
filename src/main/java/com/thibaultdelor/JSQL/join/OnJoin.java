package com.thibaultdelor.JSQL.join;

import java.util.Set;

import com.thibaultdelor.JSQL.ITable;
import com.thibaultdelor.JSQL.SQLOutputable;
import com.thibaultdelor.JSQL.Table;
import com.thibaultdelor.JSQL.criteria.Criterion;

public class OnJoin implements ExplicitJoin{

	private final ITable joinTable;
	private final Criterion criterion;
	private final SQLOutputable joinType;
	
	public OnJoin(SQLOutputable joinType, ITable joinTable, Criterion criterion) {
		super();
		this.joinTable = joinTable;
		this.criterion = criterion;
		this.joinType = joinType;
	}
	
	public OnJoin(ITable joinTable, Criterion criterion) {
		this(JoinType.INNER_JOIN, joinTable, criterion);
	}

	@Override
	public void output(StringBuilder sb, SQLContext context) {
		joinType.output(sb, context);
		joinTable.output(sb, context);
		sb.append(" ON ");
		criterion.output(sb, context);
	}

	@Override
	public ITable getJoinTable() {
		return joinTable;
	}

	@Override
	public void addNeededTables(Set<Table> tables) {
		joinTable.addNeededTables(tables);
		criterion.addNeededTables(tables);
		joinType.addNeededTables(tables);
	}
}
