package com.thibaultdelor.JSQL;


public interface ITable extends SQLOutputable {

	String getAlias();

	Column get(String colName);

	Column get(Column col);

}
