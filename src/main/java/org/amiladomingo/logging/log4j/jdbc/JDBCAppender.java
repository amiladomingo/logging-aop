package org.amiladomingo.logging.log4j.jdbc;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.beans.factory.InitializingBean;

public class JDBCAppender extends org.apache.log4j.jdbc.JDBCAppender implements
		InitializingBean {

	private DataSource dataSource;

	public JDBCAppender() {
	}

	public JDBCAppender(DataSource dataSource) {
		setDataSource(dataSource);
	}

	/**
	 * Set the JDBC DataSource to obtain connections from.
	 */
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	protected Connection getConnection() throws SQLException {
		return dataSource.getConnection();
	}

	public void afterPropertiesSet() throws Exception {
		if (dataSource == null) {
			throw new IllegalArgumentException(
					"Property 'dataSource' is required");
		}
	}
}