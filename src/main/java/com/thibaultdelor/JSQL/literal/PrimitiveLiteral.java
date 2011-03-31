package com.thibaultdelor.JSQL.literal;

public class PrimitiveLiteral extends SimpleLiteral {

	public PrimitiveLiteral(long i) {
		super(Long.toString(i));
	}
	
	public PrimitiveLiteral(double i) {
		super(Double.toString(i));
	}
}
