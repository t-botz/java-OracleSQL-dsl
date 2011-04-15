package com.thibaultdelor.JSQL;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

public class SQLQueryMatcher extends BaseMatcher<CharSequence> {


	private final String originalExpectedQuery;
	private final String expectedQuery;

	private static final char[] whiteSpaceNotSensitiveChars = { ',', '(', ')',
			'=', '<', '>', '!' };

	public SQLQueryMatcher(CharSequence expectedQuery) {
		if (expectedQuery == null)
			throw new IllegalArgumentException(
					"Non-null value required by SQLQueryMatcher");
		this.originalExpectedQuery = expectedQuery.toString();
		this.expectedQuery = minimizeQuery(originalExpectedQuery);
	}

	@Override
	public boolean matches(Object match) {
		if (match == null)
			throw new IllegalArgumentException("Cannot match to a null value");
		
		String minimizedQuery = minimizeQuery(match.toString());
		boolean equals = minimizedQuery.equals(expectedQuery);
		
		if(!equals){
			StringBuilder sb = new StringBuilder();
			
			sb.append("\nOriginal Queries");
			sb.append("\n  expected : ");
			sb.append(originalExpectedQuery);
			sb.append("\n  got      : ");
			sb.append(match.toString());
			sb.append("\nMinimized Queries");
			sb.append("\n  expected : ");
			sb.append(expectedQuery);
			sb.append("\n  got      : ");
			sb.append(minimizedQuery);
			throw new AssertionError(sb.toString());
		}
		return equals;
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
		int lastType =0; //0 whitespace, 1 whitespace sensitive, 2 char
		for (int i = 0; i < query.length(); ++i) {
			char c = query.charAt(i);
			if (Character.isWhitespace(c)) {
				if (lastType==2) {
					result.append(' ');
				}
				lastType = 0;
			} else if (!isWhiteSpaceSensitive(c)) {
				if (lastType==0 && result.length() > 0)
					result.deleteCharAt(result.length() - 1);

				result.append(c);
				lastType = 1;
			} else {
				result.append(c);
				lastType = 2;
			}
		}
		return result.toString().trim().toLowerCase();
	}

	private static boolean isWhiteSpaceSensitive(char c) {
		for (char mychar : whiteSpaceNotSensitiveChars) {
			if (mychar == c)
				return false;
		}
		return true;
	}
}
