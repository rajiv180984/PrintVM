package com.first.test.payment;

import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.first.test.controller.SmartOfficeServiceController;
//import com.paytm.pg.merchant.CheckSumServiceHelper;

@Service
public class PaytmPaymentServiceImpl implements PaytmPaymentService {
	private Logger logger = LoggerFactory.getLogger(PaytmPaymentServiceImpl.class);

	private static final String TXN_AMOUNT = "txnAmount";
	@Autowired
	private PaytmPaymentConfig paytmPaymentConfig;
/*
	@Autowired
	ChecksumGeneration checksumGeneration;
*/
	@Override
	public JsonNode paymentInitiate(JsonNode payMent) {
		// TODO Auto-generated method stub
		try {
			TreeMap<String, String> paramMap = new TreeMap<String, String>();
			paramMap.put("MID", paytmPaymentConfig.getMerchantID());
			paramMap.put("ORDER_ID", "ORDER00011");
			paramMap.put("CUST_ID", "CUST00011");
			paramMap.put("INDUSTRY_TYPE_ID", paytmPaymentConfig.getIndustryType());
			// This Channel for Mobile app
			paramMap.put("CHANNEL_ID", paytmPaymentConfig.getMobileChannelID());
			// Transaction Amount
			paramMap.put("TXN_AMOUNT", payMent.get(TXN_AMOUNT).asText());

			// To-Do YOUR_WEBSITE_HERE
			paramMap.put("WEBSITE", paytmPaymentConfig.getWebsite());
			paramMap.put("EMAIL", payMent.get("emailId").asText());
			paramMap.put("MOBILE_NO", payMent.get("mobileNo").asText());
			paramMap.put("CALLBACK_URL", paytmPaymentConfig.getCallbackUrl());

			/*String checkSum = CheckSumServiceHelper.getCheckSumServiceHelper()
					.genrateCheckSum(paytmPaymentConfig.getMerchantKey(), paramMap);
			paramMap.put("CHECKSUMHASH", checkSum);*/
			ObjectMapper mapper = new ObjectMapper();
			JsonNode node = mapper.convertValue(paramMap, JsonNode.class);
			return  node;

		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}

	@Override
	public JsonNode getPaymentStatus() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getCheckSum(JsonNode paymentDetails) {
		String checkSum = "";
		try {
			TreeMap<String, String> paramMap = new TreeMap<String, String>();
			paramMap.put("MID", paytmPaymentConfig.getMerchantID());
			paramMap.put("ORDER_ID", "ORDER00011");
			paramMap.put("CUST_ID", "CUST00011");
			paramMap.put("INDUSTRY_TYPE_ID", paytmPaymentConfig.getIndustryType());
			// This Channel for Mobile app
			paramMap.put("CHANNEL_ID", paytmPaymentConfig.getMobileChannelID());
			// Transaction Amount
			paramMap.put("TXN_AMOUNT", paymentDetails.get(TXN_AMOUNT).asText());

			// To-Do YOUR_WEBSITE_HERE
			paramMap.put("WEBSITE", paytmPaymentConfig.getWebsite());
			paramMap.put("EMAIL", paymentDetails.get("emailId").asText());
			paramMap.put("MOBILE_NO", paymentDetails.get("mobileNo").asText());
			paramMap.put("CALLBACK_URL", paytmPaymentConfig.getCallbackUrl());
			logger.info("Checksum  ============= " + paramMap.toString());
			/*checkSum = CheckSumServiceHelper.getCheckSumServiceHelper()
					.genrateCheckSum(paytmPaymentConfig.getMerchantKey(), paramMap);
			paramMap.put("CHECKSUMHASH", checkSum);*/

			//To-Do Persist Request in DB
			
			return checkSum;
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("Failed checksum ", e);

		}

		return checkSum;
	}

	@Override
	public boolean checksumVerification(JsonNode paymentDetails) {
		// TODO Auto-generated method stub
		try {
			TreeMap<String, String> paramMap = new TreeMap<String, String>();
			ObjectMapper mapper = new ObjectMapper();
			paramMap = mapper.treeToValue(paymentDetails, paramMap.getClass());
			
			/*paramMap.put("MID", paytmPaymentConfig.getMerchantID());
			paramMap.put("ORDER_ID", "ORDER00011");
			paramMap.put("CUST_ID", "CUST00011");
			paramMap.put("INDUSTRY_TYPE_ID", paytmPaymentConfig.getIndustryType());
			// This Channel for Mobile app
			paramMap.put("CHANNEL_ID", paytmPaymentConfig.getMobileChannelID());
			// Transaction Amount
			paramMap.put("TXN_AMOUNT", paymentDetails.get(TXN_AMOUNT).asText());

			// To-Do YOUR_WEBSITE_HERE
			paramMap.put("WEBSITE", paytmPaymentConfig.getWebsite());
			paramMap.put("EMAIL", paymentDetails.get("emailId").asText());
			paramMap.put("MOBILE_NO", paymentDetails.get("mobileNo").asText());
			paramMap.put("CALLBACK_URL", paytmPaymentConfig.getCallbackUrl());
*/			logger.info("Checksum  ============= " + paramMap.toString());

			String checkSum = paymentDetails.get("CHECKSUMHASH").asText();
			/*return CheckSumServiceHelper.getCheckSumServiceHelper().verifycheckSum(paytmPaymentConfig.getMerchantKey(),
					paramMap, checkSum);*/
		} catch (Exception e) {
			logger.error("failed verification ", e);
		}
		return false;
	}

}
