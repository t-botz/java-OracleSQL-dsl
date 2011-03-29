package com.thibaultdelor.JSQL.literal;

public class StringLiteral extends SimpleLiteral {

	public StringLiteral(String myString) {
		super(escape(myString));
	}

	public static String escape(String s) {
		if (s == null) return "null";
        
        StringBuilder str = new StringBuilder();
        str.append('\'');
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '\\'
                    || s.charAt(i) == '\"'
                    || s.charAt(i) == '\'') {
                str.append('\\');
            }
            str.append(s.charAt(i));
        }
        str.append('\'');
        return str.toString();
	}

	
}
