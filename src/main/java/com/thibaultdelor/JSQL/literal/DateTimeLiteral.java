package com.thibaultdelor.JSQL.literal;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTimeLiteral extends SimpleLiteral {
	
	private static final DateFormat javaDateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
	private static final String oracledateFormat = "MM/DD/YYYY HH24:MI:SS";
	
	public DateTimeLiteral(Date date) {
		super(getStringRepresentation(date));
	}

	public static String getStringRepresentation(Date date) {
		return "to_date('" + javaDateFormat.format(date) + "','"
				+ oracledateFormat + "')";
	}
}
