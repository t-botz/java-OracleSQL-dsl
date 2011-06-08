package com.thibaultdelor.JSQL;

import static com.thibaultdelor.JSQL.DemoDB.*;

import org.junit.Assert;
import org.junit.Test;

import com.thibaultdelor.JSQL.criteria.BinaryCriterion;
import com.thibaultdelor.JSQL.criteria.BinaryCriterion.BinaryOperator;
import com.thibaultdelor.JSQL.join.ExplicitJoin.JoinType;
import com.thibaultdelor.JSQL.literal.StringLiteral;
import com.thibaultdelor.junit.sql.junit.SQLQueryMatcher;

public class SubQueryTest {

	@Test
	public void testSimpleSubQuery() throws Exception {
		SelectQuery selectSubquery = new SelectQuery().select(USER_NAME, USER_ID).from(USER);
		SubQueryTable sq = new SubQueryTable(selectSubquery, "s");
		
		SelectQuery s = new SelectQuery();
		s.select(SELL_DATE, sq.get(USER_NAME));
		s.from(SELL);
		s.join(sq, SELL_USER_ID, sq.get(USER_ID));
		
		String expected = "select sell.date,s.name " +
		" from sell, (" +
		" 	Select USER.name,USER.id FROM USER" +
		"	) s" +
		" where sell.user_id= s.id";
		Assert.assertThat(s.toSQLString(), new SQLQueryMatcher(expected));
	}
	
	@Test
	public void testSimpleSubQueryUsingAs() throws Exception {
		SubQueryTable sq = new SelectQuery().select(USER_NAME, USER_ID).from(USER)
				.as("s");

		SelectQuery s = new SelectQuery();
		s.select(SELL_DATE, sq.get(USER_NAME));
		s.from(SELL);
		s.join(sq, SELL_USER_ID, sq.get(USER_ID));

		String expected = "select sell.date,s.name " +
				" from sell, (" +
				" 	Select USER.name,USER.id FROM USER" +
				"	) s" +
				" where sell.user_id= s.id";
		Assert.assertThat(s.toSQLString(), new SQLQueryMatcher(expected));
	}

	@Test
	public void testSimpleJoinSubQuery() throws Exception {
		SubQueryTable sq = new SelectQuery().select(USER_NAME, USER_ID).from(USER)
				.as("s");

		SelectQuery s = new SelectQuery();
		s.select(SELL_DATE, sq.get(USER_NAME));
		s.from(SELL);
		s.join(JoinType.INNER_JOIN, sq, SELL_USER_ID, sq.get(USER_ID));

		String expected = "select sell.date,s.name " +
				" from sell" +
				" INNER JOIN (" +
				" 	Select USER.name,USER.id FROM USER" +
				"	) s on sell.user_id= s.id";
		Assert.assertThat(s.toSQLString(), new SQLQueryMatcher(expected));
	}

	@Test
	public void testSimpleWhereSubQuery() throws Exception {
		SubQueryTable sq = new SelectQuery().select(USER_ID).from(USER).where(
							new BinaryCriterion(USER_NAME, BinaryOperator.LIKE,
									new StringLiteral("A%")))
							.as("s");

		SelectQuery s = new SelectQuery();
		s.select(SELL_DATE, SELL_USER_ID);
		s.from(SELL);
		s.whereIn(SELL_USER_ID, sq);

		String expected = "select sell.date,sell.user_id " +
				" from sell" +
				" where sell.user_id in (" +
				" 	Select USER.id FROM USER where USER.name LIKE 'A%'" +
				"	)";
		Assert.assertThat(s.toSQLString(), new SQLQueryMatcher(expected));
	}
}
