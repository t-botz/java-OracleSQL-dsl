package com.thibaultdelor.JSQL;

public interface SQLOutputable {

	public enum SQLContext{
		SELECT,FROM,WHERE,GROUPBY,HAVING,ORDER;
	}
	
	void output(StringBuilder sb, SQLContext context);
	
}
