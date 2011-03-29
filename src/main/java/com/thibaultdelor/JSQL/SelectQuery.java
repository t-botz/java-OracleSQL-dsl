package com.thibaultdelor.JSQL;

import java.util.ArrayList;
import java.util.List;

import com.thibaultdelor.JSQL.SQLOutputable.SQLContext;
import com.thibaultdelor.JSQL.criteria.BinaryCriterion;
import com.thibaultdelor.JSQL.criteria.BinaryCriterion.BinaryOperator;
import com.thibaultdelor.JSQL.criteria.Criterion;
import com.thibaultdelor.JSQL.criteria.LogicalExpression;
import com.thibaultdelor.JSQL.join.ExplicitJoin.JoinType;
import com.thibaultdelor.JSQL.join.ImplicitJoin;
import com.thibaultdelor.JSQL.join.JoinClause;
import com.thibaultdelor.JSQL.join.OnJoin;

public class SelectQuery {

	private final List<Hint> hints = new ArrayList<Hint>(0); 
	private final List<Column> columns = new ArrayList<Column>();
	private final List<Table> from = new ArrayList<Table>();
	private final List<JoinClause> join= new ArrayList<JoinClause>();
	private final LogicalExpression where = new LogicalExpression("","\nAND ","");
	private final List<Column> groupBy = new ArrayList<Column>();
	private final List<Criterion> having = new ArrayList<Criterion>();
	private final List<Column> orderBy = new ArrayList<Column>();
	
	public SelectQuery select(Column... cols) {
		for (Column c : cols) {
			columns.add(c);
		}
		return this;
	}

	public SelectQuery hints(Hint... hints) {
		for (Hint hint : hints) {
			this.hints.add(hint);
		}
		return this;
	}
	
	public SelectQuery from(Table t){
		from.add(t);
		return this;
	}
	
	public SelectQuery join(JoinClause jc){
		join.add(jc);
		if (jc instanceof ImplicitJoin) {
			ImplicitJoin implicitJoin = (ImplicitJoin) jc;
			from(implicitJoin.getJoinTable());
			where(implicitJoin.getCriterion());
		}
		return this;
	}

	public SelectQuery join(Table joinTable, Criterion criterion){
		return join(new ImplicitJoin(joinTable, criterion));
	}
	
	public SelectQuery join(Table joinTable, Column mycolumn, Column foreigncolumn){
		return join(joinTable, new BinaryCriterion(mycolumn,BinaryOperator.EQUAL,foreigncolumn));
	}
	
	public SelectQuery join(JoinType type, Table joinTable, Criterion criterion){
		return join(new OnJoin(type, joinTable, criterion));
	}
	public SelectQuery join(JoinType type, Table joinTable, Column mycolumn, Column foreigncolumn){
		return join(type, joinTable, new BinaryCriterion(mycolumn,BinaryOperator.EQUAL,foreigncolumn));
	}
	
	public SelectQuery where(Criterion c) {
		where.add(c);
		return this;
	}
	
	public SelectQuery groupBy(Column c) {
		groupBy.add(c);
		return this;
		
	}
	
	public SelectQuery having(Criterion c) {
		having.add(c);
		return this;
	}
	
	public SelectQuery orderBy(Column c) {
		orderBy.add(c);
		return this;
	}
	
	public String toSQLString() {
		StringBuilder query = new StringBuilder();
		appendSelect(query);
		appendFrom(query);
		appendWhere(query);
		appendGroupBy(query);
		appendHaving(query);
		appendOrderBy(query);
		
		return query.toString();
	}

	private void appendSelect(StringBuilder query) {
		query.append("SELECT ");
		if(hints.size()>0)
		{
			query.append("/*+ ");
			OutputUtils.strJoin(hints, " ", query, SQLContext.SELECT);
			query.append(" */ ");
		}
		OutputUtils.strJoin(columns, ", ", query, SQLContext.SELECT);
	}

	private void appendFrom(StringBuilder query) {
		//Implicit join
		query.append("\nFROM ");
		OutputUtils.strJoin(from, ", ", query, SQLContext.FROM);
		
		//Explicit join
		List<SQLOutputable> explicitJoins = new ArrayList<SQLOutputable>();
		for (JoinClause join : this.join) {
			if (join instanceof SQLOutputable) {
				explicitJoins.add((SQLOutputable) join);
			}
		}
		if(explicitJoins.size()>0)
		{
			query.append("\n");
			OutputUtils.strJoin(explicitJoins, "\n", query, SQLContext.FROM);
		}
	}

	private void appendWhere(StringBuilder query) {
		if(where.size()==0)
			return;
		
		query.append("\nWHERE ");
		where.output(query, SQLContext.WHERE);
	}

	private void appendGroupBy(StringBuilder query) {
		if(groupBy.size()==0)
			return;
		
		query.append("\nGROUP BY ");
		OutputUtils.strJoin(groupBy, ", ", query, SQLContext.GROUPBY);
	}

	private void appendHaving(StringBuilder query) {
		if(having.size()==0)
			return;
		
		query.append("\nHAVING ");
		OutputUtils.strJoin(having, "\nAND ", query, SQLContext.HAVING);
	}
	
	private void appendOrderBy(StringBuilder query) {
		if(orderBy.size()==0)
			return;
		
		query.append("\nORDER BY ");
		OutputUtils.strJoin(orderBy, ", ", query, SQLContext.ORDER);
	}
}
