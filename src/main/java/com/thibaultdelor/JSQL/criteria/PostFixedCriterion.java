package com.thibaultdelor.JSQL.criteria;

import java.util.Set;

import com.thibaultdelor.JSQL.SQLOutputable;
import com.thibaultdelor.JSQL.Table;

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

	@Override
	public void addNeededTables(Set<Table> tables) {
		member.addNeededTables(tables);
	}

	
}
