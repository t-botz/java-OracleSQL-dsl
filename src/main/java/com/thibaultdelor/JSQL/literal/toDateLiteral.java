package com.thibaultdelor.JSQL.literal;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class toDateLiteral extends SimpleLiteral{
	private static final DateFormat javaDateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
	private static final String oracledateFormat = "MM/DD/YYYY HH24:MI:SS";
	
	public toDateLiteral(Date date) {
		super(getStringRepresentation(date));
	}

	private static String getStringRepresentation(Date date) {
		return "to_date('" + javaDateFormat.format(date) + "','"
				+ oracledateFormat + "')";
	}
}
