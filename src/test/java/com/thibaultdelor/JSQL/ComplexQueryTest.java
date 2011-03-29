package com.thibaultdelor.JSQL;

import static com.thibaultdelor.JSQL.DemoDB.*;

import org.hamcrest.text.IsEqualIgnoringWhiteSpace;
import org.junit.Assert;
import org.junit.Test;

import com.thibaultdelor.JSQL.criteria.BinaryCriterion;
import com.thibaultdelor.JSQL.criteria.BinaryCriterion.BinaryOperator;
import com.thibaultdelor.JSQL.join.ExplicitJoin.JoinType;

public class ComplexQueryTest {

	@Test
	public void joinQuery() {
		SelectQuery s = 
			new SelectQuery()
			.select(USER_NAME,SELL_DATE)
			.from(USER)
			.join(SELL,SELL_USER_ID,USER_ID);
		
		String expected = "select user.name, sell.date" +
		" from user, sell" +
		" where sell.id = user.id";
		Assert.assertThat(s.toSQLString(), new IsEqualIgnoringWhiteSpace(expected));
		
	}
	
	@Test
	public void innerJoinQuery() {
		SelectQuery s = 
			new SelectQuery()
			.select(USER_NAME,SELL_DATE)
			.from(USER)
			.join(JoinType.INNER_JOIN,SELL,SELL_USER_ID,USER_ID);
		
		String expected = "select user.name, sell.date" +
		" from user" +
		" inner join sell on (sell.id = user.id)";
		Assert.assertThat(s.toSQLString(), new IsEqualIgnoringWhiteSpace(expected));
		
	}
	
	@Test
	public void selfJoinQuery() {
		SelectQuery s = new SelectQuery();
		Table u = USER.as("u1");
		Column id1 = u.get("id");
		s.select(u.get(USER_NAME)).from(u);
		u = USER.as("u2");
		Column id2 = u.get("id");
		s	.select(u.get(USER_NAME))
			.join(u,  new BinaryCriterion(id1, BinaryOperator.EQUAL, id2));
		String expected = "select u1.name, u2.name" +
				" from user as u1, user as u2" +
				" where u1.id = u2.id";
		Assert.assertThat(s.toSQLString(), new IsEqualIgnoringWhiteSpace(expected));

	}
}
