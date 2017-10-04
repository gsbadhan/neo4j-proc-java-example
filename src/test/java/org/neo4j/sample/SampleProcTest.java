
package org.neo4j.sample;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.neo4j.driver.v1.Config;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.GraphDatabase;
import org.neo4j.driver.v1.Record;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.StatementResult;
import org.neo4j.harness.junit.Neo4jRule;
import org.neo4j.sample.SampleProc;

public class SampleProcTest {

	private Driver driver = null;

	@Rule
	public Neo4jRule neo4j = new Neo4jRule().withProcedure(SampleProc.class);

	@Before
	public void setup() {
		driver = GraphDatabase.driver(neo4j.boltURI(),
				Config.build().withEncryptionLevel(Config.EncryptionLevel.NONE).toConfig());
	}

	@After
	public void shutdown() {
		if (driver != null)
			driver.close();
	}

	@Test
	public void testSearch() {
		Session session = null;
		try {
			session = driver.session();
			Map<String, Object> params = new HashMap<>(1);
			params.put("name", "gu");
			StatementResult statementResult = session.run("CALL org.digi.lg.neo4j.udf.sample.search({name})", params);
			List<Record> results = statementResult.list();
			assertNotNull(results);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
	}

	@Test
	public void testGetStats() {
		Session session = null;
		try {
			session = driver.session();
			StatementResult statementResult = session.run("CALL org.digi.lg.neo4j.udf.sample.stats");
			List<Record> results = statementResult.list();
			assertNotNull(results);
			assertTrue(results.size() != 0);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
	}

}
