package com.thibaultdelor.JSQL.literal.functions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import com.thibaultdelor.JSQL.OutputUtils;
import com.thibaultdelor.JSQL.SQLOutputable;
import com.thibaultdelor.JSQL.Table;

public class SimpleFunction implements Function{
	protected final String functionName;
	protected final List<SQLOutputable> arguments = new ArrayList<SQLOutputable>();
	
	
	public SimpleFunction(String functionName, SQLOutputable... arguments) {
		super();
		this.functionName = functionName;
		for (SQLOutputable arg : arguments) {
			this.arguments.add(arg);
		}
	}


	@Override
	public void output(StringBuilder sb, SQLContext context) {
		sb.append(functionName);
		sb.append('(');
		OutputUtils.strJoin(arguments, ", ", sb, context);
		sb.append(')');
	}


	@Override
	public void addNeededTables(Set<Table> tables) {
		for (SQLOutputable arg : arguments) {
			arg.addNeededTables(tables);
		}
	}


	@Override
	public String getFunctionName() {
		return functionName;
	}


	@Override
	public Collection<SQLOutputable> getArguments() {
		return arguments;
	}
	
	
}
