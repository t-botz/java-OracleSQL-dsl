package com.thibaultdelor.JSQL;

public class Column implements SQLOutputable {

	private final Table table;
	private final String name;
	private final String alias;
	private boolean isInUsing = false;
	
	
	Column(Table table, String name, String alias) {
		super();
		this.table = table;
		this.name = name;
		this.alias = alias;
	}
	
	Column(Table table, String name) {
		this(table, name, null);
	}
	
	public Column as(String alias) {
		return new Column(table, name, alias);
	}

	@Override
	public void output(StringBuilder sb, SQLContext context) {
		switch (context) {
		case FROM:
		case GROUPBY:
		case HAVING:
		case ORDER:
		case WHERE:
			sb.append(table.getAlias());
			sb.append(".");
			sb.append(name);
			break;
		case SELECT:
			if(!isInUsing)
			{
				sb.append(table.getAlias());
				sb.append(".");
			}
			sb.append(name);
			if(alias!=null){
				sb.append(" AS ");
				sb.append(alias);
			}
			break;
		default:
			throw new RuntimeException("Invalid context "+context+" for "+this);
		}
	}
	
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((table == null) ? 0 : table.hashCode());
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
		
		Column other = (Column) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		
		if (table == null) {
			if (other.table != null)
				return false;
		} else if (!table.equals(other.table))
			return false;
		
		return true;
	}

	@Override
	public String toString() {
		return "Column [table=" + table + ", name=" + name + ", alias=" + alias
				+ "]";
	}

	public Table getTable() {
		return table;
	}

	public String getName() {
		return name;
	}

	public void setInUsing(boolean inUsing) {
		this.isInUsing = inUsing;
		
	}

	

}
