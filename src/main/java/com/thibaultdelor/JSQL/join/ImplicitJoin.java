package com.thibaultdelor.JSQL.join;

import com.thibaultdelor.JSQL.ITable;
import com.thibaultdelor.JSQL.criteria.Criterion;

public class ImplicitJoin implements JoinClause{
	
	private final ITable joinTable;
	private final Criterion criterion;
	

	public ImplicitJoin(ITable joinTable, Criterion criterion) {
		super();
		this.joinTable = joinTable;
		this.criterion = criterion;
	}
	
	public ITable getJoinTable() {
		return joinTable;
	}
	
	public Criterion getCriterion() {
		return criterion;
	}
}
