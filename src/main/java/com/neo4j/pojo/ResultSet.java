
package com.neo4j.pojo;

import org.apache.commons.lang3.ObjectUtils;
import org.neo4j.graphdb.Node;


public class ResultSet {
	public final Node value;
	public String label;

	/**
	 * fetch node's label implicitly
	 * 
	 * @param value
	 */
	public ResultSet(final Node value) {
		this.value = value;
		if (value != null && value.getLabels() != null)
			value.getLabels().forEach(lbl -> label = lbl.name());
	}

	/**
	 * set node's label explicitly
	 * 
	 * @param value
	 * @param label
	 */
	public ResultSet(final Node value, String label) {
		this.value = value;
		this.label = label;
	}

	
	@Override
	public String toString() {
		return "ResultSet [value=" + value + ", label=" + label + "]";
	}

}
