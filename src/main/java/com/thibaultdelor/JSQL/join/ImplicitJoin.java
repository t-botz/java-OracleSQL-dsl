package com.thibaultdelor.JSQL.join;

import com.thibaultdelor.JSQL.Table;
import com.thibaultdelor.JSQL.criteria.Criterion;

public class ImplicitJoin implements JoinClause{
	
	private final Table joinTable;
	private final Criterion criterion;
	

	public ImplicitJoin(Table joinTable, Criterion criterion) {
		super();
		this.joinTable = joinTable;
		this.criterion = criterion;
	}
	
	public Table getJoinTable() {
		return joinTable;
	}
	
	public Criterion getCriterion() {
		return criterion;
	}
}
