package com.thibaultdelor.JSQL;

public class DemoDB {

	public static final Table USER = new Table("USER");
	public static final Column USER_ID = USER.get("id");
	public static final Column USER_NAME = USER.get("name");

	public static final Table SELL = new Table("SELL");
	public static final Column SELL_ID = SELL.get("id");
	public static final Column SELL_DATE = SELL.get("date");
	public static final Column SELL_AMOUNT = SELL.get("amount");
	public static final Column SELL_USER_ID = SELL.get("user_id");
	public static final Column SELL_ARTICLE_ID = SELL.get("article_id");

	public static final Table ARTICLE = new Table("ARTICLE");
	public static final Column ARTICLE_ID = ARTICLE.get("id");
	public static final Column ARTICLE_NAME = ARTICLE.get("name");
	public static final Column ARTICLE_PRICE = ARTICLE.get("price");
	public static final Column ARTICLE_PRODUCER_ID = ARTICLE.get("producer_id");

	public static final Table PRODUCER = new Table("PRODUCER");
	public static final Column PRODUCER_ID = PRODUCER.get("id");
	public static final Column PRODUCER_NAME = PRODUCER.get("name");
	
	public static final Table LOG = new Table("LOG");
	public static final Column LOG_USER_ID = LOG.get("user_id");
	public static final Column LOG_TIME = LOG.get("time");

	static{
		SELL.addForeignKey(SELL_USER_ID, USER_ID);
		SELL.addForeignKey(SELL_ARTICLE_ID, ARTICLE_ID);
		LOG.addForeignKey(LOG_USER_ID, USER_ID);
		ARTICLE.addForeignKey(ARTICLE_PRODUCER_ID, PRODUCER_ID);
	}
	
	
}
