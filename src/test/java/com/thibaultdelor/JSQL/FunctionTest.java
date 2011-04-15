package com.thibaultdelor.JSQL;

import static com.thibaultdelor.JSQL.DemoDB.*;

import org.junit.Assert;
import org.junit.Test;

import com.thibaultdelor.JSQL.literal.functions.SimpleFunction;
import com.thibaultdelor.JSQL.literal.functions.SingleArgumentFunction;

public class FunctionTest {
	
	@Test
	public void simpleFunctionQuery() {
		SelectQuery s = new SelectQuery();
		s.select(new SingleArgumentFunction("myFunc", USER_NAME)).from(USER);
		String expected = "select myFunc(user.name)" +
				" from user";
		Assert.assertThat(s.toSQLString(), new SQLQueryMatcher(expected));
	}

	@Test
	public void distinctFunctionQuery() {
		SelectQuery s = new SelectQuery();
		s.select(new SingleArgumentFunction("myFunc", USER_NAME, true)).from(USER);
		String expected = "select myFunc(distinct user.name)" +
		" from user";
		Assert.assertThat(s.toSQLString(), new SQLQueryMatcher(expected));
	}

	@Test
	public void multipleArgFunctionQuery() {
		SelectQuery s = new SelectQuery();
		s.select(new SimpleFunction("myFunc", USER_NAME, USER_ID)).from(USER);
		String expected = "select myFunc(user.name,user.id)" +
		" from user";
		Assert.assertThat(s.toSQLString(), new SQLQueryMatcher(expected));
	}
}
