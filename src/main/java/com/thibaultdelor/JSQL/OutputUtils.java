package com.thibaultdelor.JSQL;

import java.util.Iterator;

import com.thibaultdelor.JSQL.SQLOutputable.SQLContext;

public class OutputUtils {

	public static void strJoin(Iterable<? extends SQLOutputable> strings , String separator, StringBuilder output , SQLContext context)
	{
		Iterator<? extends SQLOutputable> it = strings.iterator();
		boolean hasNext = it.hasNext();
		while (hasNext) {
			SQLOutputable o = it.next();
			o.output(output, context);
			hasNext = it.hasNext();
			if(hasNext)
				output.append(separator);
		}
	}
}
