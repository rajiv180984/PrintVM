package com.first.test.config;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@PropertySource(value = "file:${configpath}-${spring.profiles.active}.properties", ignoreResourceNotFound = true)
@ConfigurationProperties
@Configuration("serviceConfig")
public class ServiceConfig implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6544039438343070318L;
	@Value("${zerox.resetPasswordAPI.URL}")
	private String resetPasswordAPIURL;
	
	@Value("${zerox.activateProfile.URL}")
	private String activateLinkURL;
	
	@Value("${zerox.adminEmail}")
	private String adminEmail;
	
	@Value("${zerox.activateProfile.subject}")
	private String activateProfileSubject;
	
	@Value("${zerox.sms.otp.apikey}")
	private String factorAPIKEY;
	
	@Value("${zerox.sms.otp.factor.baseurl}")
	private String factor2baseURL;
	
	
	@Value("${zerox.friend.refferal}")
	private String referalCode;
	
	@Value("${zerox.friend.refferal.money}")
	private int referalMoney;
	
	@Value("${record.limit}")
	private int recordLimit;
	
	@Value("${authAPI.url}")
	private String authUrl;
	

	public String getResetPasswordAPIURL() {
		return resetPasswordAPIURL;
	}

	public void setResetPasswordAPIURL(String resetPasswordAPIURL) {
		this.resetPasswordAPIURL = resetPasswordAPIURL;
	}

	public String getActivateLinkURL() {
		return activateLinkURL;
	}

	public void setActivateLinkURL(String activateLinkURL) {
		this.activateLinkURL = activateLinkURL;
	}

	public String getAdminEmail() {
		return adminEmail;
	}

	public void setAdminEmail(String adminEmail) {
		this.adminEmail = adminEmail;
	}

	public String getActivateProfileSubject() {
		return activateProfileSubject;
	}

	public void setActivateProfileSubject(String activateProfileSubject) {
		this.activateProfileSubject = activateProfileSubject;
	}

	public String getFactorAPIKEY() {
		return factorAPIKEY;
	}

	public void setFactorAPIKEY(String factorAPIKEY) {
		this.factorAPIKEY = factorAPIKEY;
	}

	public String getFactor2baseURL() {
		return factor2baseURL;
	}

	public void setFactor2baseURL(String factor2baseURL) {
		this.factor2baseURL = factor2baseURL;
	}

	public String getReferalCode() {
		return referalCode;
	}

	public void setReferalCode(String referalCode) {
		this.referalCode = referalCode;
	}

	public int getReferalMoney() {
		return referalMoney;
	}

	public void setReferalMoney(int referalMoney) {
		this.referalMoney = referalMoney;
	}

	public int getRecordLimit() {
		return recordLimit;
	}

	public void setRecordLimit(int recordLimit) {
		this.recordLimit = recordLimit;
	}

	public String getAuthUrl() {
		return authUrl;
	}

	public void setAuthUrl(String authUrl) {
		this.authUrl = authUrl;
	}

	
	
}
