package com.first.test.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class FileConversionUtility {

	private static Map<String, List<String>> map = new HashMap<>();
	private static Map<String, Integer> mapCount = new HashMap<>();
	private static Random random;
	static {
		random = new Random();
		List<String> docTopdf = new ArrayList<String>();
		docTopdf.add("https://www.ilovepdf.com/word_to_pdf");
		docTopdf.add("https://smallpdf.com/word-to-pdf");
		docTopdf.add("https://www.freepdfconvert.com/word-to-pdf");

		map.put("docTopdf", docTopdf);

		List<String> jpgTopdf = new ArrayList<String>();
		jpgTopdf.add("https://smallpdf.com/jpg-to-pdf");
		jpgTopdf.add("https://jpg2pdf.com/");
		jpgTopdf.add("https://www.ilovepdf.com/jpg_to_pdf");

		map.put("jpgTopdf", jpgTopdf);

		List<String> pngTopdf = new ArrayList<String>();
		pngTopdf.add("https://png2pdf.com/");
		pngTopdf.add("https://smallpdf.com/blog/png-to-pdf");

		map.put("pngTopdf", pngTopdf);

		List<String> pptToPdf = new ArrayList<String>();
		pptToPdf.add("https://smallpdf.com/ppt-to-pdf");

		map.put("pptToPdf", pptToPdf);

		List<String> splitFile = new ArrayList<String>();

		splitFile.add("https://smallpdf.com/split-pdf");

		map.put("splitFile", splitFile);

		List<String> mergeFile = new ArrayList<String>();

		mergeFile.add("https://smallpdf.com/merge-pdf");

		map.put("mergeFile", mergeFile);

		List<String> ocr = new ArrayList<String>();

		ocr.add("https://www.onlineocr.net/");

		map.put("ocr", ocr);
		
		mapCount.put("docTopdf", docTopdf.size());
		mapCount.put("jpgTopdf", jpgTopdf.size());
		mapCount.put("pngTopdf", pngTopdf.size());
		mapCount.put("pptToPdf", pptToPdf.size());
		mapCount.put("mergeFile", mergeFile.size());
		mapCount.put("splitFile", splitFile.size());
		mapCount.put("ocr", ocr.size());
		

		
		
	}
	
	public static String getUtilityURLbyType(String utilityName) {
		//System.out.println(" input "+ utilityName);
		return map.get(utilityName).get(random.nextInt(mapCount.get(utilityName)));
	}
	
	public static void main(String[] args) {
		for(int i =0 ;i<100;i++)
		System.out.println("======= "+random.nextInt(1));
	}
}
