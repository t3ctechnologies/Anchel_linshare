package org.linagora.linshare.core.dao.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Properties;

import org.linagora.linshare.core.common.FtpClientConfiguration;

public class SftpLinshareWaarp {

	public void insert(String fileName2, String uuid2) {
		PreparedStatement preparedStmt = null;
		Connection conn = null;
		Properties properties = new FtpClientConfiguration().getclientProperties();
		String url = properties.getProperty("linshare.db.url");
		String user = properties.getProperty("linshare.db.username");
		String password = properties.getProperty("linshare.db.password");
		try {
			conn = DriverManager.getConnection(url, user, password);
			String query = " insert into S3FILENAMEHANDLER(filename,uuid,processedOn)" + " values (?,?,?)";
			preparedStmt = conn.prepareStatement(query);

			preparedStmt.setString(1, fileName2);
			preparedStmt.setString(2, uuid2);
			preparedStmt.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
			preparedStmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
