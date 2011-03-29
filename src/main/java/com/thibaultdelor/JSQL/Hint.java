package com.thibaultdelor.JSQL;

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

}
