
package com.neo4j.proc.convert;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Path;
import org.neo4j.graphdb.PropertyContainer;
import org.neo4j.graphdb.Relationship;
import org.neo4j.kernel.internal.GraphDatabaseAPI;
import org.neo4j.procedure.Context;
import org.neo4j.procedure.Description;
import org.neo4j.procedure.Name;
import org.neo4j.procedure.Procedure;

import com.neo4j.core.CommonFunctions;
import com.neo4j.pojo.MapResult;

public class ConvertProcedures {

	@Context
	public GraphDatabaseAPI graph;

	@SuppressWarnings("unchecked")
	@Procedure("convert.toTree")
	@Description("convert paths to Tree")
	public Stream<MapResult> convertToTree(@Name("paths") List<Path> paths) {
		Map<Long, Map<String, Object>> maps = new HashMap<>(paths.size() * 100);
		for (Path path : paths) {
			Iterator<PropertyContainer> it = path.iterator();
			while (it.hasNext()) {
				Node n = (Node) it.next();
				Map<String, Object> nMap = maps.computeIfAbsent(n.getId(), id -> CommonFunctions.getNodeMap(n));
				if (it.hasNext()) {
					Relationship r = (Relationship) it.next();
					Node m = r.getOtherNode(n);
					Map<String, Object> mMap = maps.computeIfAbsent(m.getId(), id -> CommonFunctions.getNodeMap(m));
					String typeName = r.getType().name().toLowerCase();
					mMap = CommonFunctions.addRelProperties(mMap, typeName, r);
					if (!nMap.containsKey(typeName))
						nMap.put(typeName, new ArrayList<>(16));
					List<Object> list = (List<Object>) nMap.get(typeName);
					if (!list.contains(mMap))
						list.add(mMap);
				}
			}
		}
		return paths.stream().map(Path::startNode).map(n -> maps.remove(n.getId())).filter(m -> m != null)
				.map(MapResult::new);
	}
}
