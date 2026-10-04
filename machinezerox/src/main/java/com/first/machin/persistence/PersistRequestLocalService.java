package com.first.machin.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import om.first.machin.model.PartnerItemRequest;
import om.first.machin.model.PrintDocRequest;
import om.first.machin.model.PrintJobStatusUpdate;
import om.first.machin.model.PrintProperties;

@Service
public class PersistRequestLocalService {
	private Logger logger = LoggerFactory.getLogger(PersistRequestLocalService.class); 
	@Autowired
	SQLLiteConnection sQLLiteConnection;
	
	@Autowired
	private JdbcTemplate jdbcTemplate;

	public void persistLocally(PrintDocRequest printDocRequest, String localDocURL) throws JsonProcessingException {
		Connection conn = sQLLiteConnection.createConnection();
		//To-Do persist number of total pages to print
		
		ObjectMapper mapper = new ObjectMapper();
		// docId, docURL, status, mobile, securityCode,printProperties
		try {
			// conn = dataSource.getConnection();
			
			PreparedStatement pstmt = conn.prepareStatement(SQLLiteQuerisBuilder.sql_remote_print_persist_request);
			pstmt.setString(1, printDocRequest.getPrintJobId());
			pstmt.setString(2, printDocRequest.getDocumentURL());
			pstmt.setString(3, "PENDING");
			pstmt.setString(4, printDocRequest.getMobileNo());
			pstmt.setString(5, printDocRequest.getSecurityCode());
			pstmt.setString(6, mapper.writeValueAsString(printDocRequest.getPrintProperties()));
			pstmt.setString(7, localDocURL);
			pstmt.executeUpdate();
			System.out.println(" Persisted Locally");
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	public void persistStatusInLocalDB(PrintJobStatusUpdate printJobStatusUpdate) {
		logger.info(" Update PrintJob Status "+ printJobStatusUpdate.toString());
		Connection conn = sQLLiteConnection.createConnection();
		
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQLLiteQuerisBuilder.DIRECT_PRINT_UPDATE_STATUS);
			pstmt.setString(1, printJobStatusUpdate.getPrintJobStatus());
			pstmt.setString(2, printJobStatusUpdate.getPrintJobId());
			pstmt.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			logger.error("Failed to update status ", e);
		}
	}

	public PrintDocRequest createPrintJObFromPartnerCommand(PartnerItemRequest partnerItemRequest) {
		PrintDocRequest printDocRequest = null;
		Connection conn = null;
		ObjectMapper mapper = new ObjectMapper();
		 //(docId TEXT PRIMARY KEY, docURL TEXT NOT NULL, status TEXT NOT NULL, mobile TEXT NOT NULL, 
		//securityCode TEXT NOT NULL, printProperties TEXT NOT NULL, requestTime TIMESTAMP  DEFAULT CURRENT_TIMESTAMP, localDocURL TEXT);
		try {
			conn = sQLLiteConnection.createConnection();
			String query = "Select * from printRequest where docId =?";
			PreparedStatement pstmt = conn.prepareStatement(query);
			pstmt.setString(1, partnerItemRequest.getDocId());
			ResultSet result = pstmt.executeQuery();
			printDocRequest = new PrintDocRequest();
			printDocRequest.setPrintJobId(result.getString("docId"));
			printDocRequest.setDocumentURL(result.getString("docURL"));
			printDocRequest.setPrintStatus(result.getString("status"));
			printDocRequest.setMobileNo(result.getString("mobile"));
			PrintProperties printProperties = null;
			if(result.getString("printProperties") != null) {
				 printProperties = mapper.readValue(result.getString("printProperties"), PrintProperties.class);
			}else {
				printProperties = new PrintProperties();
				printProperties.setNumberOfCopies(1);
				
			}
			printDocRequest.setPrintProperties(printProperties);
			printDocRequest.setLocalPath(result.getString("localDocURL"));
			return printDocRequest;
			
		} catch (Exception e) {
           e.printStackTrace();
		} finally {
			try {
				if (conn != null || !conn.isClosed()) {
					conn.close();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return printDocRequest;
	}

	public void updateLocalCloudNotifStatus(PrintJobStatusUpdate printJobStatusUpdate, boolean b) {
		// TODO Auto-generated method stub
		
		logger.info(" Update PrintJob Status "+ printJobStatusUpdate.toString());
		Connection conn = sQLLiteConnection.createConnection();
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQLLiteQuerisBuilder.DIRECT_PRINT_UPDATE_STATUS_CLOUD);
			pstmt.setString(1, printJobStatusUpdate.getPrintJobStatus());
			pstmt.setString(2, printJobStatusUpdate.getPrintJobId());
			pstmt.setBoolean(3, b);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			logger.error("Failed to update status ", e);
		}
	}

	// Direct Print
	
	
	// Persit Print request
	
	// read print request
	// update print request
	
}
