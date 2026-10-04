package com.first.test.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.first.test.model.PrintDocRequest;
import com.first.test.model.Range;
import com.first.test.service.PaymentServiceImpl;

public class CommonUtility {

	private static Logger logger = LoggerFactory.getLogger(CommonUtility.class);

	private static String alphaNumericString = "$@#ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789abcdefghijklmnopqrstuvxyz$@#!_-";

	public static String regexEmail = "^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";

	public static String regexNumber = "\\d+";
	public static Pattern patternEmail = Pattern.compile(regexEmail);
	public static Pattern patternMobile = Pattern.compile(regexNumber);
	public static Map<String, Object> uiMap = new HashMap<>();
	
	public static ArrayList<String> paperSide =  new ArrayList<String>(Arrays.asList("Single Side", "Both Side"));
	public static ArrayList<String> paperSize =  new ArrayList<String>(Arrays.asList("A4", "A3"));
	public static ArrayList<String> color =  new ArrayList<String>(Arrays.asList("Black & White", "Color"));
	public static ArrayList<String> orientation =  new ArrayList<String>(Arrays.asList("Portrait", "Landscape"));
	public static ArrayList<String> paperQuality =  new ArrayList<String>(
			Arrays.asList("Standard Premium", "Standard Premium-100 gsm","Bond-85 gsm",  "Bond-100 gsm", "Matte-100 gsm"));

	public static ArrayList<String> bindingType =  new ArrayList<String>(Arrays.asList("Spiral Bound", "Twin Loop Wire", "3 Staples with Plastic Cover",
			"Loose Leaf"));
	
	public static Map<Range, Float> costMap;
	static {
		costMap = new HashMap<>();
		costMap.put(new Range(1, 3), 3f);
		costMap.put(new Range(4, 10), 2.5f);
		costMap.put(new Range(11, 25), 2.0f);
		costMap.put(new Range(26, 50), 1.75f);
		costMap.put(new Range(51, 100), 1.55f);
		
		uiMap.put("paperSize", paperSize);
		uiMap.put("orientation", orientation);
		uiMap.put("paperQuality", paperQuality);
		uiMap.put("bindingType", bindingType);
		
	}

	private static final String DELIM = "@@@";
	
	public static boolean validEmail(String userDetails) {
		Matcher matcher = patternEmail.matcher(userDetails);
		boolean flag = matcher.matches();
		System.out.print("========This is email ===== " + flag);
		return flag;
	}

	public static boolean validMobile(String userDetails) {
		Matcher matcher = patternMobile.matcher(userDetails);
		return matcher.matches();
	}

	public static String randomString(int i) {
		// TODO Auto-generated method stub
		StringBuilder pwd = new StringBuilder();
		Random random = new Random();
		for (int j = 0; j < 8; i++) {
			pwd.append(alphaNumericString.charAt(random.nextInt(70)));
		}
		return pwd.toString();
	}

	// FIXME
	public static float getRangeCost(int pageCount) {
		//TO DO 
		// ebaluate Range
		return costMap.get(new Range(4, 10));
		
	}
	public static void main(String[] args) {
		System.out.println(validMobile("9988551218"));
	}

	public static String randomUUIDGenerator() {
		UUID uuid = UUID.randomUUID();
		return uuid.toString();
	}

	public static Map<String, Object> getUIMap(){
		return uiMap;
		
		
	}

	public static String createPrivateCertKey(PrintDocRequest printDocRequest) {
	
		//machineId@@@printJobId@@@totalCount@@@expn@@@emailId
		StringBuilder strBdlr = new StringBuilder(printDocRequest.getMachineId());
		strBdlr.append(printDocRequest.getMachineId())
		.append(DELIM).append(printDocRequest.getPrintJobId())
		.append(DELIM).append(printDocRequest.getPrintProperties().getPageCount())
		.append(DELIM).append(printDocRequest.getPrintProperties().getPexpn())
		.append(DELIM).append(printDocRequest.getEmailId());
		
		logger.info(" Private key :", strBdlr.toString());
		
		return strBdlr.toString();
		
	}
	
}
