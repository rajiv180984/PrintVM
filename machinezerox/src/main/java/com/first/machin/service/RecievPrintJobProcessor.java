package com.first.machin.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Callable;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.first.machin.utils.FileWriteUtility;

import om.first.machin.model.DirectPrintJob;
import om.first.machin.model.PrintDocRequest;
import om.first.machin.model.PrintProperties;

public class RecievPrintJobProcessor implements Callable<Integer>
{
 
	private PrintDocRequest printDocRequest;
	private MultipartFile recieveFile;
	private File saveFileByPrintJobId;
	private final int SUCCESS = 1;
	private final int FAIL = -1;
 
    public RecievPrintJobProcessor(MultipartFile recieveFile, PrintDocRequest printDocRequest, File saveFileByPrintJobId) {
		super();
		this.recieveFile = recieveFile;
		this.printDocRequest = printDocRequest;
		this.saveFileByPrintJobId= saveFileByPrintJobId;
	}

	@Override
	public Integer call() throws Exception {
		
		// Create thread 
		
				// read base path
		OutputStream os = null;
			
			try {
			DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
			Date dateObj = new Date();
			System.out.println(" =======Date =========== "+df.format(dateObj));
			// rename File
			
			InputStream is = recieveFile.getInputStream();
			
			os = new FileOutputStream(saveFileByPrintJobId);
			os.write(recieveFile.getBytes());
			
			}catch (Exception e) {
				return FAIL;
			} finally {
				if(os !=null)  {
					os.close();
				}
			}
			
			
				// Read file withDate
				// check directory exist 
				// create file with PrintJobId
				// if file already exist Throw  error
				// Persist File in System
				// on successful Persist data in SQLLite
		
		return SUCCESS;
	}

	

	
}