package com.thibaultdelor.JSQL;

public class DemoDB {

	public static final Table USER = new Table("USER");
	public static final Column USER_NAME = USER.get("name");
	public static final Column USER_ID = USER.get("id");

	public static final Table SELL = new Table("SELL");
	public static final Column SELL_DATE = SELL.get("date");
	public static final Column SELL_AMOUNT = SELL.get("amount");
	public static final Column SELL_USER_ID = SELL.get("id");
	
	
}
