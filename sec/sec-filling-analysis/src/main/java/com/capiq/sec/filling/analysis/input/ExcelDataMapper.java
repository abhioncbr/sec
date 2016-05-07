package com.capiq.sec.filling.analysis.input;

import java.util.LinkedList;

public interface ExcelDataMapper<E> {

	public void setObjectData(LinkedList<Object> paramValues, E targetObject);
}
