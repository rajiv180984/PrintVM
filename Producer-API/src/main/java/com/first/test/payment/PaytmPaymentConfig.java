package com.first.test.payment;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PaytmPaymentConfig implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7610621865988118894L;

	@Value("${paytm.MerchantID}")
	private String merchantID;
	
	@Value("${paytm.MerchantKey}")
	private String merchantKey;
	
	@Value("${paytm.Website}")
	private String website;

	@Value("${paytm.IndustryType}")
	private String industryType;
	
	@Value("${paytm.mobile.ChannelID}")
	private String mobileChannelID;
	
	@Value("${paytm.TransactionURL}")
	private String transactionURL;
			
	
	@Value("${paytm.TransactionStatusURL}")
	private String TransactionStatusURL;
	
	@Value("${paytm.callbackUrl}")
	private String callbackUrl;
	


	public String getMerchantID() {
		return merchantID;
	}


	public void setMerchantID(String merchantID) {
		this.merchantID = merchantID;
	}


	public String getMerchantKey() {
		return merchantKey;
	}


	public void setMerchantKey(String merchantKey) {
		this.merchantKey = merchantKey;
	}


	public String getWebsite() {
		return website;
	}


	public void setWebsite(String website) {
		this.website = website;
	}


	public String getIndustryType() {
		return industryType;
	}


	public void setIndustryType(String industryType) {
		this.industryType = industryType;
	}


	public String getMobileChannelID() {
		return mobileChannelID;
	}


	public void setMobileChannelID(String mobileChannelID) {
		this.mobileChannelID = mobileChannelID;
	}


	public String getTransactionURL() {
		return transactionURL;
	}


	public void setTransactionURL(String transactionURL) {
		this.transactionURL = transactionURL;
	}


	public String getTransactionStatusURL() {
		return TransactionStatusURL;
	}


	public void setTransactionStatusURL(String transactionStatusURL) {
		TransactionStatusURL = transactionStatusURL;
	}


	public String getCallbackUrl() {
		return callbackUrl;
	}


	public void setCallbackUrl(String callbackUrl) {
		this.callbackUrl = callbackUrl;
	}


	@Override
	public String toString() {
		return "PaytmPaymentConfig [merchantID=" + merchantID + ", merchantKey=" + merchantKey + ", website=" + website
				+ ", industryType=" + industryType + ", mobileChannelID=" + mobileChannelID + ", transactionURL="
				+ transactionURL + ", TransactionStatusURL=" + TransactionStatusURL + ", callbackUrl=" + callbackUrl
				+ "]";
	}
			
	
	
}
