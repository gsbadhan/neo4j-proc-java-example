
package com.neo4j.core;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;


public class CommonFunctions {

	private CommonFunctions() {
	}


	public static Map<String, Object> addRelProperties(Map<String, Object> mMap, String typeName, Relationship r) {
		Map<String, Object> rProps = r.getAllProperties();
		if (rProps.isEmpty())
			return mMap;
		String prefix = typeName + ".";
		rProps.forEach((k, v) -> mMap.put(prefix + k, v));
		return mMap;
	}

	public static Map<String, Object> getNodeMap(Node n) {
		Map<String, Object> result =  n.getAllProperties();
		result.put("id", n.getId());
		return result;
	}

	public static String labelString(Node n) {
		return StreamSupport.stream(n.getLabels().spliterator(), false).map(Label::name).sorted()
				.collect(Collectors.joining(":"));
	}

}
