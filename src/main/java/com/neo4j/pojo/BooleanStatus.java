package com.neo4j.pojo;

import java.util.HashMap;
import java.util.Map;

public class BooleanStatus {
	public Map<String, Object> value = new HashMap<>(1);

	public BooleanStatus() {
		value.put("status", false);
	}

	public BooleanStatus(boolean status) {
		value.put("status", status);
	}

}
