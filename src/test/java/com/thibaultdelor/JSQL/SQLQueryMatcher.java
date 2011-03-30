package com.thibaultdelor.JSQL;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

public class SQLQueryMatcher extends BaseMatcher<CharSequence> {

	private final String expectedQuery;

	private static final char[] whiteSpaceNotSensitiveChars = { ',', '(', ')',
			'=', '<', '>', '!' };

	public SQLQueryMatcher(CharSequence expectedQuery) {
		if (expectedQuery == null)
			throw new IllegalArgumentException(
					"Non-null value required by SQLQueryMatcher");
		this.expectedQuery = minimizeQuery(expectedQuery.toString());
	}

	@Override
	public boolean matches(Object match) {
		if (match == null)
			throw new IllegalArgumentException("Cannot match to a null value");
		
		String minimizedQuery = minimizeQuery(match.toString());
		return minimizedQuery.equalsIgnoreCase(expectedQuery);
	}

	@Override
	public void describeTo(Description description) {
		description.appendText("SQLQueryMatcher(").appendValue(
				this.expectedQuery).appendText(")");

	}

	/**
	 * Minimize a query string by removing all extras spaces. Minimizing a query
	 * has no effect on its semantic.
	 * 
	 * @param query
	 *            the query string
	 * @return the minimized query string
	 */
	public static String minimizeQuery(String query) {
		StringBuilder result = new StringBuilder();
		boolean lastWasSpace = true;
		for (int i = 0; i < query.length(); ++i) {
			char c = query.charAt(i);
			if (Character.isWhitespace(c)) {
				if (!(lastWasSpace)) {
					result.append(' ');
				}
				lastWasSpace = true;
			} else if (!isWhiteSpaceSensitive(c)) {
				if (lastWasSpace && result.length() > 0)
					result.deleteCharAt(result.length() - 1);

				result.append(c);
				lastWasSpace = true;
			} else {
				result.append(c);
				lastWasSpace = false;
			}
		}
		return result.toString().trim();
	}

	private static boolean isWhiteSpaceSensitive(char c) {
		for (char mychar : whiteSpaceNotSensitiveChars) {
			if (mychar == c)
				return false;
		}
		return true;
	}
}
