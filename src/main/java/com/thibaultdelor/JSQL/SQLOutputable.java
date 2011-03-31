package com.thibaultdelor.JSQL;

import java.util.Set;

/**
 * An SQL Object printable in a query.
 */
public interface SQLOutputable {

	/**
	 * The Enum SQLContext represents the current context of the query building.
	 */
	public enum SQLContext{
		SELECT,FROM,WHERE,GROUPBY,HAVING,ORDER;
	}
	
	/**
	 * Output this SQL object.
	 *
	 * @param sb the sb
	 * @param context the context
	 */
	void output(StringBuilder sb, SQLContext context);
	
	/**
	 * Adds all tables required by this SQL object.
	 *
	 * @param tables the set of table to append
	 */
	void addNeededTables(Set<Table> tables);
}
