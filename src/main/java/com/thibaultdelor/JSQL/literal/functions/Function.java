package com.thibaultdelor.JSQL.literal.functions;

import java.util.Collection;

import com.thibaultdelor.JSQL.SQLOutputable;

public interface Function extends SQLOutputable{

	String getFunctionName();
	
	Collection<SQLOutputable> getArguments();
}
