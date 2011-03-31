package com.thibaultdelor.JSQL;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.thibaultdelor.JSQL.join.ExplicitJoin.JoinType;
import com.thibaultdelor.JSQL.join.JoinClause;

/**
 * JoinResolver allows to find the best list of joins to add.
 */
@SuppressWarnings(value="all") //TODO yet Not Implemented
public class JoinResolver {

	
	private final Set<Table> mustJoinTables;
	private final Set<Table> existingTables;
	
	private final int depth;
	

	public JoinResolver(Set<Table> mustJoinTables, Set<Table> existingTables,
			int depth) {
		super();
		this.mustJoinTables = new HashSet<Table>(mustJoinTables);
		this.existingTables = new HashSet<Table>(existingTables);
		this.depth = depth;
	}


	public List<JoinClause> resolve(JoinType joinType) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	
	
}
