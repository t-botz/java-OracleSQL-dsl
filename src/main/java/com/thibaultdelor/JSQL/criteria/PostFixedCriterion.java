package com.thibaultdelor.JSQL.criteria;

import com.thibaultdelor.JSQL.SQLOutputable;

abstract class PostFixedCriterion implements Criterion{
	
	private final SQLOutputable member;
	private final String operator;
	
	PostFixedCriterion(SQLOutputable member, String operator) {
		this.member = member;
		this.operator = operator;
	}
	
	@Override
	public void output(StringBuilder sb, SQLContext context) {
		member.output(sb, context);
		sb.append(operator);
	}

}
