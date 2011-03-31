package com.thibaultdelor.JSQL.literal;

import java.util.Set;

import com.thibaultdelor.JSQL.SQLOutputable;
import com.thibaultdelor.JSQL.Table;

public class SimpleLiteral implements SQLOutputable {

	private final String output;
	
	
	public SimpleLiteral(String output) {
		super();
		this.output = output;
	}

	@Override
	public void output(StringBuilder sb, SQLContext context) {
		sb.append(output);
	}

	@Override
	public void addNeededTables(Set<Table> tables) {
		return;
	}
	
}
