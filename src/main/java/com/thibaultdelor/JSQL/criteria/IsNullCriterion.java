package com.thibaultdelor.JSQL.criteria;

import com.thibaultdelor.JSQL.SQLOutputable;

public class IsNullCriterion extends PostFixedCriterion {

	public IsNullCriterion(SQLOutputable member) {
		super(member, " IS NULL");
	}

}
