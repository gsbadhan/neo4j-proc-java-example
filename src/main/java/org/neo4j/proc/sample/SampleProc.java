
package org.neo4j.proc.sample;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.stream.Stream;

import org.apache.log4j.Logger;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Result;
import org.neo4j.kernel.internal.GraphDatabaseAPI;
import org.neo4j.procedure.Context;
import org.neo4j.procedure.Name;
import org.neo4j.procedure.Procedure;

public class SampleProc {

	@Context
	public GraphDatabaseAPI db;
	private static final Logger log = Logger.getLogger(SampleProc.class);

	/**
	 * 
	 * @param name
	 * @return
	 * @throws InterruptedException
	 * @throws ExecutionException
	 * 
	 * @call procedure: call sample.search('guru');
	 */
	@Procedure("sample.search")
	public Stream<ResultObj> search(@Name("name") String name) throws InterruptedException, ExecutionException {
		log.info("Search request has come for input: " + name);
		Map<String, Object> param = new HashMap<>(1);
		param.put("name", name);
		Result nodes = db.execute("match (n) where n.name contains {name} return n", param);
		List<ResultObj> resultObjs = new LinkedList<>();
		if (nodes.hasNext()) {
			Map<String, Object> next = nodes.next();
			Node node = (Node) next.get("n");
			resultObjs.add(new ResultObj(node, 1));
		}
		Stream<ResultObj> result = resultObjs.stream();
		log.info("***Search completed****");
		return result;
	}

	/**
	 * 
	 * @return
	 * 
	 * @call procedure: call sample.stats;
	 */
	@Procedure("sample.stats")
	public Stream<Stats> getStats() {
		log.info("calling stats");
		List<Stats> outList = new ArrayList<Stats>();
		for (int i = 0; i <= 2; i++) {
			outList.add(new Stats(i));
		}
		log.info("calling stats is " + outList.size());
		return outList.stream();

	}

	public static class ResultObj {
		final public Node node;
		final public String level;

		public Node getNode() {
			return node;
		}

		public ResultObj(Node node, Integer level) {
			super();
			this.node = node;
			this.level = Integer.toString(level);
		}

		@Override
		public String toString() {
			return "ResultObj [node=" + node + ", level=" + level + "]";
		}

	}

	public static class Stats {
		final public long size;

		public Stats(long size) {
			this.size = size;
		}

		public long getSize() {
			return size;
		}
	}

}
