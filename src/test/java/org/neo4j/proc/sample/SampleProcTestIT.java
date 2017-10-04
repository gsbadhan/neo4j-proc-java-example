
package org.neo4j.proc.sample;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.neo4j.driver.v1.AuthTokens;
import org.neo4j.driver.v1.Config;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.GraphDatabase;
import org.neo4j.driver.v1.Record;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.StatementResult;

public class SampleProcTestIT {
	private String DB_URL = "bolt://localhost:7687";
	private String DB_USER = "neo4j";
	private String DB_PASSWORD = "test";
	private Config config = null;
	private Driver driver = null;

	@Before
	public void setup() {
		config = Config.build().withMaxTransactionRetryTime(5, TimeUnit.MILLISECONDS).withMaxIdleSessions(10)
				.withLeakedSessionsLogging().withRoutingFailureLimit(20)
				.withRoutingRetryDelay(200, TimeUnit.MILLISECONDS).toConfig();
		driver = GraphDatabase.driver(DB_URL, AuthTokens.basic(DB_USER, DB_PASSWORD), config);
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
			params.put("name", "gurpreet");
			StatementResult statementResult = session.run("CALL sample.search({name})", params);
			List<Record> results = statementResult.list();
			assertNotNull(results);
			assertTrue(results.size() != 0);
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
			StatementResult statementResult = session.run("CALL sample.stats");
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
