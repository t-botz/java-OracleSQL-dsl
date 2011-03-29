package com.thibaultdelor.JSQL.criteria;

public class OrExpression extends LogicalExpression {
	
	public OrExpression(Criterion...criteria) {
		super(" OR ", criteria);
	}
	
}
