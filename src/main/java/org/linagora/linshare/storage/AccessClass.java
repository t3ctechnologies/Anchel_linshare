package org.linagora.linshare.storage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import org.linagora.linshare.core.common.FtpClientConfiguration;

public class AccessClass {

	Properties properties = null;
	String url = null;
	String user = null;
	String password = null;

	public AccessClass() {
		properties = new FtpClientConfiguration().getclientProperties();
		url = properties.getProperty("linshare.db.url");
		user = properties.getProperty("linshare.db.username");
		password = properties.getProperty("linshare.db.password");
	}

	public void update(String specialKey) throws SQLException {

		try {
			Connection conn = (Connection) DriverManager.getConnection(url, user, password);
			String query = " update S3BUCKETMAPPING set isdeleted=1 where specialKey=?";
			PreparedStatement preparedStmt = conn.prepareStatement(query);
			preparedStmt.setNString(1, specialKey);
			preparedStmt.executeUpdate();
			conn.close();
		}

		catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public String TakeSpecialId(String file) {
		String splId = null;
		try {
			Connection conn = (Connection) DriverManager.getConnection(url, user, password);
			String query = " select specialKey from S3BUCKETMAPPING where fileName =?";

			PreparedStatement preparedStmt = conn.prepareStatement(query);

			preparedStmt.setNString(1, file);

			ResultSet rs = preparedStmt.executeQuery();
			rs.next();
			splId = rs.getString(1);

			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return splId;
	}

}