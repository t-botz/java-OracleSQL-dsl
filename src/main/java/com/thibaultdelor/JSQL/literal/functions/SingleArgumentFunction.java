package com.thibaultdelor.JSQL.literal.functions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

import com.thibaultdelor.JSQL.Distinctable;
import com.thibaultdelor.JSQL.SQLOutputable;
import com.thibaultdelor.JSQL.Table;
import com.thibaultdelor.JSQL.literal.SimpleLiteral;

public class SingleArgumentFunction implements Function,Distinctable{
	
	private final String functionName;
	private boolean distinct;
	private final SQLOutputable arg;

	public SingleArgumentFunction(String functName) {
		this(functName,new SimpleLiteral("*"));
	}
	
	public SingleArgumentFunction(String functName,SQLOutputable col) {
		this(functName,col, false);
	}
	
	public SingleArgumentFunction(String functName, SQLOutputable col, boolean distinct) {
		this.functionName = functName;
		this.arg = col;
		this.distinct = distinct;
	}
	
	public boolean isDistinct() {
		return distinct;
	}

	public void setDistinct(boolean distinct) {
		this.distinct = distinct;
	}

	@Override
	public void output(StringBuilder sb, SQLContext context) {
		sb.append(functionName);
		sb.append("(");
		if(distinct)
			sb.append("DISTINCT ");
		arg.output(sb, context);
		sb.append(')');
	}

	@Override
	public void addNeededTables(Set<Table> tables) {
		arg.addNeededTables(tables);
	}

	@Override
	public String getFunctionName() {
		return functionName;
	}

	@Override
	public Collection<SQLOutputable> getArguments() {
		ArrayList<SQLOutputable> array = new ArrayList<SQLOutputable>(1);
		array.add(arg);
		return array;
	}
}
