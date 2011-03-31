package com.thibaultdelor.JSQL;

import java.util.Set;

public class OrderClause implements SQLOutputable {

	private final Column column;
	private final boolean asc;
	
	
	public OrderClause(Column column, boolean asc) {
		this.column = column;
		this.asc = asc;
	}
	
	public OrderClause(Column column) {
		this(column, true);
	}
	
	@Override
	public void output(StringBuilder sb, SQLContext context) {
		column.output(sb, context);
		if(!asc)
			sb.append(" DESC");
		
	}

	@Override
	public void addNeededTables(Set<Table> tables) {
		column.addNeededTables(tables);
	}
	
	
}
