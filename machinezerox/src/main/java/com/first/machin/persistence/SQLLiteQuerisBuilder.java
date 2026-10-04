package com.first.machin.persistence;

public class SQLLiteQuerisBuilder {
	
	public static final String DIRECT_PRINT_INSERT ="INSERT into DirectPrint(printJobID, localPath, pageCount, printStatus, printParam, cost,requestTime) values(?,?,?,?,?,?,?)";
	
	public static final String DIRECT_PRINT_UPDATE_STATUS = "UPDATE DirectPrint SET printStatus=? WHERE printJobID=?";
	
	public static final String DIRECT_PRINT_UPDATE_STATUS_CLOUD = "UPDATE DirectPrint SET printStatus=?, cloudNotif=? WHERE printJobID=?";
			
	public static final String sql_remote_print_persist_request = "INSERT INTO printRequest(docId, docURL,status, mobile, securityCode, printProperties, localDocURL) VALUES(?,?,?,?,?,?,?)";
	
	public static final String SQL_SAVE_CLOUD_REQUEST = "";
	
	public static final String SQL_DIRECT_PRINT_BY_JOB_ID = "SELECT printJobID, localPath , printStatus from DirectPrint where printJobID = ";
	
	//CREATE TABLE IF NOT EXISTS DirectPrint (printJobID TEXT PRIMARY KEY, localPath text NOT NULL,printStatus text NOT NULL, pxprn text NOT NULL, requestTime text NOT NULL, printParam text NOT NULL, cost  REAL );";
	/*
	 
	 CREATE TABLE DirectPrint (printJobID TEXT PRIMARY KEY, localPath text NOT NULL,pageCount INT,  printStatus text NOT NULL,requestTime text NOT NULL, printParam text NOT NULL, cost  REAL );
	 
	 */
}
