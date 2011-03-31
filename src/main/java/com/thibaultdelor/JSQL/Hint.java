package com.thibaultdelor.JSQL;

import java.util.Set;

public class Hint implements SQLOutputable{

	public static final Hint ORDERED = new Hint("ORDERED");
	
	private String output;
	
	public Hint(String output) {
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
