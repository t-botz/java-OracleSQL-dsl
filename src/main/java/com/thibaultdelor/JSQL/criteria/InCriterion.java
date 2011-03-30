package com.thibaultdelor.JSQL.criteria;

import com.thibaultdelor.JSQL.SQLOutputable;
import com.thibaultdelor.JSQL.literal.LiteralSet;

public class InCriterion implements Criterion{

	private SQLOutputable column;
	private LiteralSet values;

	public InCriterion(SQLOutputable col, LiteralSet values) {
		this.column = col;
		this.values = values ;
	}

	@Override
	public void output(StringBuilder sb, SQLContext context) {
		column.output(sb, context);
		sb.append(" IN (");
		values.output(sb, context);
		sb.append(")");
		
	}

}
