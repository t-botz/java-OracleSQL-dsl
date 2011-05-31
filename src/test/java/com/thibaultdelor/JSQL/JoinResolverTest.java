package com.thibaultdelor.JSQL;

import static com.thibaultdelor.JSQL.DemoDB.*;

import org.junit.Assert;
import org.junit.Test;

import com.thibaultdelor.JSQL.criteria.BinaryCriterion;
import com.thibaultdelor.JSQL.criteria.BinaryCriterion.BinaryOperator;
import com.thibaultdelor.JSQL.literal.PrimitiveLiteral;
import com.thibaultdelor.junit.sql.junit.SQLQueryMatcher;

public class JoinResolverTest {
	
	@Test
	public void SimpleAutoJoin() throws Exception {
		SelectQuery sq = new SelectQuery();
		sq	.select(ARTICLE_NAME)
			.from(SELL)
			.where(
				new BinaryCriterion(SELL_ID, BinaryOperator.EQUAL,
						new PrimitiveLiteral(2)))
			.autoJoin();
		String expected = "select article.name" +
		" from SELL" +
		" INNER JOIN article on article.id=sell.article_id" +
		" where sell.id=2";
		
		Assert.assertThat(sq.toSQLString(), new SQLQueryMatcher(expected));
	}
	
	@Test
	public void MultiReferencedTableTest() throws Exception {
		SelectQuery sq = new SelectQuery();
		sq	.select(USER_NAME)
		.from(SELL)
		.where(
				new BinaryCriterion(SELL_ID, BinaryOperator.EQUAL,
						new PrimitiveLiteral(2)))
						.autoJoin();
		String expected = "select user.name" +
		" from SELL" +
		" INNER JOIN user on user.id = sell.user_id" +
		" where sell.id=2";
		
		Assert.assertThat(sq.toSQLString(), new SQLQueryMatcher(expected));
	}
	
	@Test
	public void AutoJoinDistantTableTest() throws Exception {
		SelectQuery sq = new SelectQuery();
		sq	.select(PRODUCER_ID)
		.from(SELL)
		.where(new BinaryCriterion(SELL_ID, BinaryOperator.EQUAL,
				new PrimitiveLiteral(2)))
		.autoJoin();
		String expected = "select producer.id" +
		" from SELL" +
		" INNER JOIN article on article.id= sell.article_id" +
		" INNER JOIN producer on producer.id = article.producer_id" +
		" where sell.id=2";
		
		Assert.assertThat(sq.toSQLString(), new SQLQueryMatcher(expected));
	}
	@Test
	public void AutoJoinDistantTableFromTest() throws Exception {
		SelectQuery sq = new SelectQuery();
		sq	.select(PRODUCER_ID)
		.from(PRODUCER)
		.where(new BinaryCriterion(SELL_ID, BinaryOperator.EQUAL,
				new PrimitiveLiteral(2)))
				.autoJoin();
		String expected = "select producer.id" +
		" from producer" +
		" INNER JOIN article on article.producer_id = producer.id" +
		" INNER JOIN sell on sell.article_id = article.id" +
		" where sell.id=2";
		
		Assert.assertThat(sq.toSQLString(), new SQLQueryMatcher(expected));
	}
}
