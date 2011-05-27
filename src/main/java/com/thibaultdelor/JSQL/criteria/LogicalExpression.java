package com.thibaultdelor.JSQL.criteria;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.thibaultdelor.JSQL.OutputUtils;
import com.thibaultdelor.JSQL.Table;

public class LogicalExpression implements Criterion {

	protected List<Criterion> criteria = new ArrayList<Criterion>();
	private final String separator;
	private String startDelimiter;
	private String endDelimiter;

	public LogicalExpression(String startDelimiter, String separator,
			String endDelimiter, Criterion... criteria) {
		this.separator = separator;
		this.startDelimiter = startDelimiter;
		this.endDelimiter = endDelimiter;
		for (Criterion criterion : criteria) {
			this.criteria.add(criterion);
		}
	}
	public LogicalExpression(String separator, Criterion... criteria) {
		this("(", separator, ")", criteria);
	}

	/**
	 * @throws IllegalStateException if the logical Expression has no criterion
	 * @see com.thibaultdelor.JSQL.SQLOutputable#output(java.lang.StringBuilder, com.thibaultdelor.JSQL.SQLOutputable.SQLContext)
	 * {@inheritDoc}
	 */
	@Override
	public void output(StringBuilder sb, SQLContext context) throws IllegalStateException{
		if(criteria.size()==0)
			throw new IllegalStateException("The logical expression has no criteria");
		sb.append(startDelimiter);
		OutputUtils.strJoin(criteria, separator, sb, context);
		sb.append(endDelimiter);

	}

	public void add(Criterion c) {
		this.criteria.add(c);
	}
	
	public int size() {
		return criteria.size();
	}
	@Override
	public void addNeededTables(Set<Table> tables) {
		for (Criterion c : criteria) {
			c.addNeededTables(tables);
		}
		
	}
}