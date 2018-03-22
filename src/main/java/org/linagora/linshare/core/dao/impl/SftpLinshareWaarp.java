package org.linagora.linshare.core.dao.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import org.linagora.linshare.core.common.FtpClientConfiguration;

public class SftpLinshareWaarp {

	public String getByUuid(String Uuid) {
		String FileName = null;
		Connection conn1 = null;
		PreparedStatement preparedStmt = null;
		ResultSet rs = null;
		String url = "jdbc:mysql://localhost:3306/linshare";
		String user = "root";
		String password = "root";
		try {
			conn1 = DriverManager.getConnection(url, user, password);
			String query = " select filename from sftplinsharewaarp where uuid=?";
			preparedStmt = conn1.prepareStatement(query);
			preparedStmt.setNString(1, Uuid);
			rs = preparedStmt.executeQuery();
			rs.next();
			FileName = rs.getString("filename");
		} catch (SQLException e) {

			e.printStackTrace();
		}
		return FileName;

	}

	public void insert(String fileName2, String uuid2) {
		PreparedStatement preparedStmt = null;
		Connection conn = null;
		Properties properties = new FtpClientConfiguration().getclientProperties();
		String url = properties.getProperty("linshare.db.url");
		String user = properties.getProperty("linshare.db.username");
		String password = properties.getProperty("linshare.db.password");
		try {
			conn = DriverManager.getConnection(url, user, password);
			String query = " insert into sftplinsharewaarp(filename,uuid)" + " values (?,?)";
			preparedStmt = conn.prepareStatement(query);

			preparedStmt.setNString(1, fileName2);
			preparedStmt.setNString(2, uuid2);
			preparedStmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
}
