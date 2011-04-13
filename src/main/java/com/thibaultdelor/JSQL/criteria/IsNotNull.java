package com.thibaultdelor.JSQL.criteria;

import com.thibaultdelor.JSQL.SQLOutputable;

public class IsNotNull extends PostFixedCriterion {

	public IsNotNull(SQLOutputable member) {
		super(member, " IS NOT NULL");
	}

}
