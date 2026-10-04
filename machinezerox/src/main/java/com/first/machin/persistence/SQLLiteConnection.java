package com.first.machin.persistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import om.first.machin.model.PrintDocRequest;

@Service
public class SQLLiteConnection {

	@Value("${sqllite.connectionUrl}")
	private String connectionUrl ;

	private Connection connection;

	public Connection createConnection() {
		// SQLite connection string
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(connectionUrl);
			
			System.out.println("**************************** ");
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		
		return connection;
		
	}

	
	/*@Bean("connection")
	public Connection dataSource() {
		System.out.println("======== DataSource Connection1 =======");
		final DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName("org.sqlite.JDBC");
		dataSource.setUrl("jdbc:sqlite:C:/Software/sqlLite/SQLLiteTool/sqlite-tools-win32-x86-3290000/machine.db");
		System.out.println("======== DataSource Connection =======");
		try {
			
			return dataSource.getConnection();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
*/
	/**
	 * Insert a new row into the warehouses table
	 *
	 * @param name
	 * @param capacity
	 */
	public void persistLocally(PrintDocRequest printDocRequest) {
		String sql = "INSERT INTO contacts(docId, docURL,status, mobile, securityCode, printProperties) VALUES(?,?,?,?,?)";
		ObjectMapper mapper = new ObjectMapper();
		// docId, docURL, status, mobile, securityCode,printProperties
		try {
			Connection conn = this.createConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, printDocRequest.getPrintJobId());
			pstmt.setString(2, printDocRequest.getDocumentURL());
			pstmt.setString(3, "PENDING");
			pstmt.setString(4, printDocRequest.getPrintJobId());
			pstmt.setString(5, printDocRequest.getSecurityCode());
			pstmt.setString(5, mapper.writeValueAsString(printDocRequest.getPrintProperties()));
			pstmt.executeUpdate();
			
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	
	/**
	 * @param args
	 *            the command line arguments
	 */
	public static void main(String[] args) {
		//SQLLiteConnection sqlLite = new SQLLiteConnection();
	}

}