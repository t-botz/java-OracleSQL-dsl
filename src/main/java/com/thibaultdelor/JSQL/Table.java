package com.thibaultdelor.JSQL;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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
}
