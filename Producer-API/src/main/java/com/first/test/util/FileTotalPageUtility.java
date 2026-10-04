package com.first.test.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.commons.io.FilenameUtils;
import org.apache.poi.hslf.usermodel.HSLFSlideShow;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileTotalPageUtility {
	//private static Logger logger = LoggerFactory.getLogger(FileTotalPageUtility.class);

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		String filePath = "/home/rajeev/Downloads/Rajeev_Rathor_new.doc";
	//	countNumberOfPage(filePath);
	}

	public static int countNumberOfPage(String filePath) throws IOException {
		File file = new File(filePath); // Creating File Object
		try {
		String fileExtension = FilenameUtils.getExtension(filePath); // Getting extension of file
		if(fileExtension.equalsIgnoreCase("pdf")) {
			return PageCountUtility.getTotalCountOfDoc(file);
		}else if (fileExtension.equalsIgnoreCase("docx")) {
			return readDocxFile(file);
		} else if (fileExtension.equalsIgnoreCase("doc")) {
			return readDocFile(file);
		} else if (fileExtension.equalsIgnoreCase(".ppt")) {
			HSLFSlideShow document = new HSLFSlideShow(new FileInputStream(filePath));
			return document.getSlides().size();

		} else if (fileExtension.equalsIgnoreCase(".pptx")) {
			// XSLFSlideShow xdocument = new XSLFSlideShow(filePath);

		}
		}catch (Exception e) {
			//logger.error("failed to count page", e);
		
		}
		return 0;
	}

	// Reading data from ".docx" file.
	// Important
	// https://stackoverflow.com/questions/2848514/number-of-pages-in-a-word-doc-in-java

	public static int readDocxFile(File file) throws IOException {
		FileInputStream fis = new FileInputStream(file);
		XWPFDocument doc = new XWPFDocument(fis);

		return doc.getProperties().getExtendedProperties().getUnderlyingProperties().getPages();
	}

	// Reading data from ".doc" file.
	public static int readDocFile(File file) throws IOException {
		HWPFDocument wordDoc = new HWPFDocument(new FileInputStream(file));
		return wordDoc.getDocProperties().getCPg();
	}

}