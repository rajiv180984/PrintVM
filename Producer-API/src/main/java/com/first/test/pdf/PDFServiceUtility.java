package com.first.test.pdf;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

// this function should be implemented from 
//@Component
public class PDFServiceUtility {

	public static void main(String[] args) {
		try {
			String fileName = "C:\\Users\\rajeev.rathor\\Downloads\\Spring.in.Action.3rd.Edition-www.gocit.vn.pdf";
			String fileName2 = "C:\\Users\\rajeev.rathor\\Downloads\\Spring-Action4.pdf";
			PdfReader reader = new PdfReader(fileName);
			int count = reader.getNumberOfPages();
			System.out.println("======== "+count);
			
				pageSelectionRange(fileName, fileName2, "100");
				// TODO Auto-generated catch block
				
		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public int getNumberOfPage(String filepath) {
		PdfReader reader;
		try {
			reader = new PdfReader(filepath);
			int count = reader.getNumberOfPages();
			System.out.println("======== "+count);
			
			return count;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
		
	}
	
	public static void pageSelectionRange(String Source, String dest, String range) throws DocumentException, IOException {
	    PdfReader reader = new PdfReader(Source);
	    int n = reader.getNumberOfPages();
	    reader.selectPages(range);
	    
	    PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(dest));
	    stamper.setFullCompression();
	    stamper.close();
	    
	  
	}
	
	
	public static void pageSelection(String Source, String dest, String rangeList) throws DocumentException, IOException {
	    PdfReader reader = new PdfReader(Source);
	    String pages[] = rangeList.split(",");
	    List<Integer> pageList = new ArrayList<>();
	    for(String s : pages) { pageList.add(Integer.valueOf(s.trim()));}
	    reader.selectPages(pageList);
	    
	    PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(dest));
	    stamper.setFullCompression();
	    stamper.close();  
	  
	}
	
	public static void splitePdfFilesIntoSmall(String filePath, String destinationFolder) {
		  try {
			PdfReader reader = new PdfReader(filePath);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		  
	}
}
