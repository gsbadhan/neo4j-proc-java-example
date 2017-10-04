
package com.neo4j.convert;

import static org.junit.Assert.assertNotNull;

import java.util.List;
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
import org.neo4j.shell.util.json.JSONObject;

public class ConvertProceduresTestIT {
	private String DB_URL = "bolt://localhost:7687";
	private String DB_USER = "neo4j";
	private String DB_PASSWORD = "test";
	private Config config = null;
	private Driver driver = null;

	@Before
	public void setup() {
		config = Config.build().withMaxTransactionRetryTime(5, TimeUnit.MILLISECONDS).withMaxIdleSessions(10)
				.withLeakedSessionsLogging().toConfig();
		driver = GraphDatabase.driver(DB_URL, AuthTokens.basic(DB_USER, DB_PASSWORD), config);
	}

	@After
	public void shutdown() {
		if (driver != null)
			driver.close();
	}

	@Test
	public void testConvertToTree() {
		Session session = null;
		try {
			session = driver.session();
			List<Record> records = session
					.run("MATCH p=(:principal)-[:belongs]->(:admin_unit)<-[:has]-(:contract) WITH COLLECT(p) as paths CALL org.digi.lg.convert.toTree(paths) yield value RETURN value")
					.list();
			assertNotNull(records.get(0).asMap());
			records.forEach(record -> {
				System.out.println(new JSONObject(record.asMap()));
			});
		} finally {
			if (session != null)
				session.close();
		}
	}
}
