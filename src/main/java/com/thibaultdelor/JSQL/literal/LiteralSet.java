package com.thibaultdelor.JSQL.literal;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.thibaultdelor.JSQL.OutputUtils;
import com.thibaultdelor.JSQL.SQLOutputable;
import com.thibaultdelor.JSQL.Table;

public class LiteralSet implements SQLOutputable {

	private static final String separator = ", ";
	
	private List<SQLOutputable> values = new ArrayList<SQLOutputable>(); 

	public LiteralSet(String... values) {
		for (String string : values) {
			this.values.add(new StringLiteral(string));
		}
	}
	
	public LiteralSet(long... values) {
		for (long l : values) {
			this.values.add(new PrimitiveLiteral(l));
		}
	}

	public LiteralSet(int... values) {
		for (int i : values) {
			this.values.add(new PrimitiveLiteral(i));
		}
	}
	
	public LiteralSet(double... values) {
		for (double d : values) {
			this.values.add(new PrimitiveLiteral(d));
		}
	}
	
	@Override
	public void output(StringBuilder sb, SQLContext context) {
		OutputUtils.strJoin(values, separator, sb, context);
	}

	@Override
	public void addNeededTables(Set<Table> tables) {
		for (SQLOutputable s : values) {
			s.addNeededTables(tables);
		}
		
	}
}
