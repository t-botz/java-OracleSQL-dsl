package com.thibaultdelor.JSQL;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Table implements ITable {

	class ForeignKey {
		private Column foreignKey;
		private Column primaryKey;

		public ForeignKey(Column foreignKey, Column primaryKey) {
			if(!isMine(primaryKey)&& !isMine(foreignKey))
				throw new RuntimeException("Invalid foreign key!");
			this.foreignKey = foreignKey;
			this.primaryKey = primaryKey;
		}

		public Column getForeignKey() {
			return foreignKey;
		}

		public Column getPrimaryKey() {
			return primaryKey;
		}

		private Table getMyTable() {
			return Table.this;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getMyTable().hashCode();
			result = prime * result
					+ ((foreignKey == null) ? 0 : foreignKey.hashCode());
			result = prime * result
					+ ((primaryKey == null) ? 0 : primaryKey.hashCode());
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
			ForeignKey other = (ForeignKey) obj;
			if (!getMyTable().equals(other.getMyTable()))
				return false;
			if (foreignKey == null) {
				if (other.foreignKey != null)
					return false;
			} else if (!foreignKey.equals(other.foreignKey))
				return false;
			if (primaryKey == null) {
				if (other.primaryKey != null)
					return false;
			} else if (!primaryKey.equals(other.primaryKey))
				return false;
			return true;
		}

		private ForeignKey duplicateFor(Table t) {
			
			Column first = isMine(foreignKey)
					? new Column(t, foreignKey.getName())
					: foreignKey;
			Column second = isMine(primaryKey) 
					? new Column(t, primaryKey.getName())
					: primaryKey;
			return t.new ForeignKey(first, second);
		}

		private boolean isMine(Column col) {
			return col.getTable().equals(getMyTable());
		}
		
		public Column getRemoteColumn() {
			return isMine(primaryKey)?foreignKey:primaryKey;
		}
		public Column getMyColumn() {
			return isMine(primaryKey)?primaryKey:foreignKey;
		}
	}

	private final String name;
	private final String alias;
	private final List<Column> columns = new ArrayList<Column>();
	private final Set<ForeignKey> foreignKeys = new HashSet<Table.ForeignKey>();

	public Table(String name, String alias) {
		super();
		this.name = name;
		this.alias = alias;
	}

	public Table(String name) {
		this(name, null);
	}

	@Override
	public String getAlias() {
		return (alias == null) ? name : alias;
	}

	public String getName() {
		return name;
	}

	public Collection<Column> getColumns() {
		return Collections.unmodifiableList(columns);
	}

	public Set<ForeignKey> getForeignKeys() {
		return Collections.unmodifiableSet(foreignKeys);
	}

	public Table as(String alias) {
		Table table = new Table(name, alias);
		for (Column c : columns) {
			table.get(c);
		}
		for (ForeignKey fk : foreignKeys) {
			table.foreignKeys.add(fk.duplicateFor(table));
		}
		return table;
	}

	@Override
	public Column get(String colName) {
		Column c = new Column(this, colName);
		int indexOf = columns.indexOf(c);
		if (indexOf > -1)
			c = columns.get(indexOf);
		else
			columns.add(c);
		return c;
	}

	@Override
	public Column get(Column col) {
		return get(col.getName());
	}

	public void addForeignKey(Column origin, Column reference) {
		foreignKeys.add(new ForeignKey(origin, reference));
		if (! (reference.getTable() instanceof Table)) {
			throw new IllegalArgumentException(reference+" is not in a concrete table and so cannot be used in a foreign key");
		}
		Table table = (Table) reference.getTable();
		table.foreignKeys.add(table.new ForeignKey(reference, origin));
	}

	@Override
	public void output(StringBuilder sb, SQLContext context) {
		switch (context) {
		case FROM:
			sb.append(name);
			if (alias != null) {
				sb.append(" ");
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
	 * @param table
	 *            the table
	 * @return true, if is same table
	 */
	public boolean isSameTable(Table table) {
		if (table == null)
			return false;

		return name.equals(table.name);
	}

	@Override
	public void addNeededTables(Set<Table> tables) {
		tables.add(this);
	}
}
