
package com.neo4j.pojo;

import java.util.List;
import java.util.Map;

public class ListMapResultSet {
	public final List<Map<String, Object>> value;

	public ListMapResultSet(List<Map<String, Object>> value) {
		this.value = value;
	}
}
