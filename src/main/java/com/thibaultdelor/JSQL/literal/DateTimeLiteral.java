package com.thibaultdelor.JSQL.literal;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTimeLiteral extends SimpleLiteral {
	
	private static final DateFormat FORMATTER = new SimpleDateFormat("''yyyy-MM-dd HH:mm:ss.S''");
	
	public DateTimeLiteral(Date d) {
		super(getFormattedDate(d));
	}
	
	private static String getFormattedDate(Date d) {
		return FORMATTER.format(d);
	}
}
