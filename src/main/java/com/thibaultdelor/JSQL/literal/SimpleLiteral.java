package com.thibaultdelor.JSQL.literal;

import com.thibaultdelor.JSQL.SQLOutputable;

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
	
}
