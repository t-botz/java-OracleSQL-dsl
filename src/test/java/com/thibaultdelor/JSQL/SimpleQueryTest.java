package com.thibaultdelor.JSQL;

import static com.thibaultdelor.JSQL.DemoDB.USER;
import static com.thibaultdelor.JSQL.DemoDB.USER_ID;
import static com.thibaultdelor.JSQL.DemoDB.USER_NAME;

import org.junit.Assert;
import org.junit.Test;

import com.thibaultdelor.JSQL.criteria.BinaryCriterion;
import com.thibaultdelor.JSQL.criteria.BinaryCriterion.BinaryOperator;
import com.thibaultdelor.JSQL.literal.FunctionCall;
import com.thibaultdelor.JSQL.literal.SimpleLiteral;
import com.thibaultdelor.JSQL.literal.StringLiteral;

public class SimpleQueryTest {

	@Test
	public void simpleQuery() {
		SelectQuery s = new SelectQuery();
		s.select(USER_NAME).from(USER);
		String expected = "select user.name" +
				" from user";
		Assert.assertThat(s.toSQLString(), new SQLQueryMatcher(expected));
	}
	
	@Test
	public void multipleColumnQuery() {
		SelectQuery s = new SelectQuery();
		s	.select(USER_NAME,USER_ID)
			.from(USER);
		String expected = "select user.name, user.id" +
		" from user";
		Assert.assertThat(s.toSQLString(), new SQLQueryMatcher(expected));
	}

	@Test
	public void tableAliasQuery() {
		SelectQuery s = new SelectQuery();
		Table userU = USER.as("u");
		s.select(userU.get(USER_NAME)).from(userU);
		String expected = "select u.name" +
		" from user u";
		Assert.assertThat(s.toSQLString(), new SQLQueryMatcher(expected));
	}

	@Test
	public void colAliasQuery() {
		SelectQuery s = new SelectQuery();
		s.select(USER_NAME.as("n"),USER_ID).from(USER);
		String expected = "select user.name as n, user.id" +
		" from user";
		Assert.assertThat(s.toSQLString(), new SQLQueryMatcher(expected));
	}
	
	@Test
	public void hintsQuery() {
		SelectQuery s = new SelectQuery();
		s.select(USER_NAME).hints(Hint.ORDERED).from(USER);
		String expected = "select /*+ ORDERED */ user.name" +
		" from user";
		Assert.assertThat(s.toSQLString(), new SQLQueryMatcher(expected));
	}
	
	@Test
	public void whereQuery() {
		SelectQuery s = new SelectQuery();
		s.select(USER_NAME,USER_ID)
		.from(USER)
		.where(new BinaryCriterion(USER_NAME, BinaryOperator.EQUAL, new StringLiteral("hello")));
		String expected = "select user.name, user.id" +
		" from user" +
		" where user.name = 'hello'";
		Assert.assertThat(s.toSQLString(), new SQLQueryMatcher(expected));
	}

	@Test
	public void groupByQuery() {
		SelectQuery s = new SelectQuery();
		s.select(USER_NAME,USER_ID)
		.from(USER)
		.groupBy(USER_NAME);
		String expected = "select user.name, user.id" +
		" from user" +
		" group by user.name";
		Assert.assertThat(s.toSQLString(), new SQLQueryMatcher(expected));
	}
	
	@Test
	public void havingQuery() {
		SelectQuery s = new SelectQuery();
		s.select(USER_NAME,USER_ID)
		.from(USER)
		.groupBy(USER_ID)
		.having(new BinaryCriterion(USER_ID, BinaryOperator.GREATER , new SimpleLiteral("5")));
		String expected = "select user.name, user.id" +
		" from user" +
		" group by user.id" +
		" having user.id > 5";
		Assert.assertThat(s.toSQLString(), new SQLQueryMatcher(expected));
	}
	
	@Test
	public void functionQuery() {
		SelectQuery s = new SelectQuery();
		s.select(new FunctionCall("myfunction", USER_NAME)).from(USER);
		String expected = "select myfunction(user.name)" +
				" from user";
		Assert.assertThat(s.toSQLString(), new SQLQueryMatcher(expected));
	}
	
}
