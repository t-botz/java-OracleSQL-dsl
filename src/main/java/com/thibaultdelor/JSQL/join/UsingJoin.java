package com.thibaultdelor.JSQL.join;

import java.util.Set;

import com.thibaultdelor.JSQL.Column;
import com.thibaultdelor.JSQL.ITable;
import com.thibaultdelor.JSQL.SQLOutputable;
import com.thibaultdelor.JSQL.Table;

public class UsingJoin implements ExplicitJoin{
	
	private final Column joinColumn;
	private final SQLOutputable joinType;
	
	public UsingJoin(SQLOutputable joinType, Column joinColumn) {
		super();
		this.joinColumn = joinColumn;
		joinColumn.setInUsing(true);
		this.joinType = joinType;
	}
	
	public UsingJoin(Column joinColumn) {
		this(JoinType.INNER_JOIN, joinColumn);
	}

	@Override
	public void output(StringBuilder sb, SQLContext context) {
		joinType.output(sb, context);
		joinColumn.getTable().output(sb, context);
		sb.append(" USING (");
		sb.append(joinColumn.getName());
		sb.append(")");
	}

	@Override
	public ITable getJoinTable() {
		return joinColumn.getTable();
	}

	@Override
	public void addNeededTables(Set<Table> tables) {
		joinColumn.addNeededTables(tables);
		joinType.addNeededTables(tables);
	}
	
	
}
