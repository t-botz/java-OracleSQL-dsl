package com.thibaultdelor.JSQL.criteria;

import java.util.ArrayList;
import java.util.List;

import com.thibaultdelor.JSQL.OutputUtils;

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

	@Override
	public void output(StringBuilder sb, SQLContext context) {
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
}