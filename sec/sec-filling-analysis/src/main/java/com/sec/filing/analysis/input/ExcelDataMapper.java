package com.sec.filing.analysis.input;

import java.util.LinkedList;

public interface ExcelDataMapper<E> {

	public void setObjectData(LinkedList<Object> paramValues, E targetObject);
}
