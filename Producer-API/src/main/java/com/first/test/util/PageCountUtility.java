package com.first.test.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.first.test.service.AdminServiceImpl;
import com.itextpdf.text.pdf.PdfReader;

public class PageCountUtility {
	// private static Logger logger =
	// LoggerFactory.getLogger(PageCountUtility.class);
	private static Logger logger = LoggerFactory.getLogger(PageCountUtility.class);
	public static void main(String[] args) {
		System.out.println("== === print Count evaluator ========= ");

		try {
			int count = getTotalCountOfPrintPage("ALL", "/home/rajeev/Downloads/Rajeev_Rathor_new.doc");

			System.out.println("================ Total Count " + count);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.info(" Failed to evaluate count ", e);
		}
	}

	/**/
	public static int getTotalCountOfPrintPage(String printexp, String path) {
		try {
			// 
			if(path ==null) {
				return expressionCountEvaluteFromExpr(printexp);
			}
			
			if (printexp.equalsIgnoreCase("ALL")) {

				return FileTotalPageUtility.countNumberOfPage(path);

			} else {
				return expressionCountEvaluteFromExpr(printexp);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			
		}
		
		return 0;

	}

	private static int expressionCountEvaluteFromExpr(String printexp) {
		int count = 0;
		String range[];
		// TODO Auto-generated method stub
		if (printexp.indexOf('-') > 0 || printexp.indexOf(',') > 0) {
			for (String str : printexp.split(",")) {

				if (str.indexOf('-') > 0) {
					range = str.split("-");
					count += Integer.parseInt(range[1].trim()) - Integer.parseInt(range[0].trim()) + 1;

				} else {
					count += 1;
				}

			}
		}
		return count;
	}

	public static int getTotalCountOfDoc(File path) {

		try {
			PdfReader document = new PdfReader(new FileInputStream(path));
			return document.getNumberOfPages();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			logger.info(" Failed to evaluate count ", e);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;

	}

}
