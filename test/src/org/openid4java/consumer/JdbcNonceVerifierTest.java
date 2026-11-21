package org.openid4java.consumer;

import junit.framework.Test;
import junit.framework.TestSuite;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import javax.sql.DataSource;

public class JdbcNonceVerifierTest extends AbstractNonceVerifierTest {

	public JdbcNonceVerifierTest(String name) {
		super(name);
	}

	@Override
	public NonceVerifier createVerifier(int maxAge) {
		DataSource dataSource = new SingleConnectionDataSource(
				"jdbc:hsqldb:mem:saasstore_security_client", "sa", "", true);
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		jdbcTemplate.execute(
				"DROP TABLE IF EXISTS openid_nonce;");
		jdbcTemplate.execute(
						"CREATE TABLE openid_nonce (  "
								+ "opurl varchar(255) NOT NULL,  nonce varchar(25) NOT NULL,  "
								+ "date datetime DEFAULT NULL,  PRIMARY KEY (opurl,nonce))");

		JdbcNonceVerifier jdbcNonceVerifier = new JdbcNonceVerifier(maxAge,
				"openid_nonce");
		jdbcNonceVerifier.setDataSource(dataSource);
		return jdbcNonceVerifier;
	}

	public static Test suite() {
		return new TestSuite(JdbcNonceVerifierTest.class);
	}

}
