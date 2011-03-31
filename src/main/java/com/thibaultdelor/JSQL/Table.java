package com.thibaultdelor.JSQL;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class Table implements SQLOutputable {

	private final String name;
	private final String alias;
	private final List<Column> columns = new ArrayList<Column>();
	private final Map<Column, Column> foreignKeys = new HashMap<Column, Column>();

	public Table(String name, String alias) {
		super();
		this.name = name;
		this.alias = alias;
	}

	public Table(String name) {
		this(name, null);
	}

	public String getAlias() {
		return (alias == null) ? name : alias;
	}

	public Collection<Column> getColumns() {
		return Collections.unmodifiableList(columns);
	}

	public Map<Column, Column> getForeignKeys() {
		return Collections.unmodifiableMap(foreignKeys);
	}

	public Table as(String alias) {
		Table table = new Table(name, alias);
		for (Column c : columns) {
			table.get(c);
		}
		for (Entry<Column, Column> entry : foreignKeys.entrySet()) {
			table.foreignKeys.put(table.get(entry.getKey()),entry.getValue());
		}
		return table;
	}

	public Column get(String colName) {
		Column c = new Column(this, colName);
		int indexOf = columns.indexOf(c);
		if (indexOf > -1)
			c = columns.get(indexOf);
		else
			columns.add(c);
		return c;
	}

	public Column get(Column userName) {
		return get(userName.getName());
	}

	public void addForeignKey(Column origin, Column reference) {
		foreignKeys.put(origin, reference);
	}

	@Override
	public void output(StringBuilder sb, SQLContext context) {
		switch (context) {
		case FROM:
			sb.append(name);
			if (alias != null) {
				sb.append(" AS ");
				sb.append(alias);
			}
			break;
		default:
			throw new RuntimeException("Invalid context " + context + " for "
					+ this);
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((alias == null) ? 0 : alias.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Table other = (Table) obj;
		if (alias == null) {
			if (other.alias != null)
				return false;
		} else if (!alias.equals(other.alias))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
	
	/**
	 * Checks if is the same table (same name).
	 *
	 * @param table the table
	 * @return true, if is same table
	 */
	public boolean isSameTable(Table table) {
		if(table==null)
		return false;
		
		return name.equals(table.name);
	}

	@Override
	public void addNeededTables(Set<Table> tables) {
		tables.add(this);
	}
}
