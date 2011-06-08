package com.thibaultdelor.JSQL.criteria;

import java.util.Set;

import com.thibaultdelor.JSQL.SQLOutputable;
import com.thibaultdelor.JSQL.SubQueryTable;
import com.thibaultdelor.JSQL.Table;
import com.thibaultdelor.JSQL.literal.LiteralSet;

public class InCriterion implements Criterion{

	private SQLOutputable column;
	private SQLOutputable inElement;

	
	private InCriterion(SQLOutputable col, SQLOutputable inElement) {
		this.column = col;
		this.inElement = inElement ;
	}

	public InCriterion(SQLOutputable col, LiteralSet values) {
		this(col, (SQLOutputable)values);
	}
	public InCriterion(SQLOutputable col, SubQueryTable subQuery) {
		this(col, (SQLOutputable)subQuery);
	}

	@Override
	public void output(StringBuilder sb, SQLContext context) {
		column.output(sb, context);
		sb.append(" IN (");
		inElement.output(sb, context);
		sb.append(")");
		
	}

	@Override
	public void addNeededTables(Set<Table> tables) {
		column.addNeededTables(tables);
		inElement.addNeededTables(tables);
		
	}

}
