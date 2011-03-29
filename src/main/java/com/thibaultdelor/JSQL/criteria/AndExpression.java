package com.thibaultdelor.JSQL.criteria;



public class AndExpression extends LogicalExpression{

	public AndExpression(Criterion...criteria) {
		super(" AND ", criteria);
	}
	
}
