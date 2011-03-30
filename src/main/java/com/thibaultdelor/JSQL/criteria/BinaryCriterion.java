package com.thibaultdelor.JSQL.criteria;

import com.thibaultdelor.JSQL.SQLOutputable;

public class BinaryCriterion implements Criterion{

	public static enum BinaryOperator implements SQLOutputable{
		EQUAL(" = "), 
		NOT_EQUAL(" <> "), 
		GREATER(" > "),
		GREATER_EQUAL(" >= "),
		LOWER(" < "),
		LOWER_EQUAL(" <= "),
		LIKE(" LIKE ")
		;
		
		private final String representation;
		
		private BinaryOperator(String representation) {
			this.representation = representation;
		}
		
		public String getRepresentation() {
			return representation;
		}

		@Override
		public void output(StringBuilder sb, SQLContext context) {
			sb.append(representation);
			
		}
	}
	
	private final SQLOutputable left; 
	private final SQLOutputable operator; 
	private final SQLOutputable right;
	
	
	public BinaryCriterion(SQLOutputable left, SQLOutputable operator,
			SQLOutputable right) {
		super();
		this.left = left;
		this.operator = operator;
		this.right = right;
	}



	@Override
	public void output(StringBuilder sb, SQLContext context) {
		left.output(sb, context);
		operator.output(sb, context);
		right.output(sb, context);
		
	}
	
}
