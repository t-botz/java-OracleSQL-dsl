package com.thibaultdelor.JSQL;

import static com.thibaultdelor.JSQL.DemoDB.USER;
import static com.thibaultdelor.JSQL.DemoDB.USER_ID;
import static com.thibaultdelor.JSQL.DemoDB.USER_NAME;

import org.junit.Assert;
import org.junit.Test;

public class WhereCriterionTest {

	@Test
	public void InTest() {
		SelectQuery s = 
			new SelectQuery()
			.select(USER_NAME)
			.from(USER)
			.whereIn(USER_ID, "abc","efg");
		
		String expected = "select user.name" +
		" from user" +
		" where user.id in ('abc', 'efg')";
		Assert.assertThat(s.toSQLString(), new SQLQueryMatcher(expected));
	}
	
}
