package com.first.test.notification;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.text.StrSubstitutor;
//import org.apache.commons.lang.text.StrSubstitutor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.first.test.util.ZeroxConstants;
// using SendGrid's Java Library
// https://github.com/sendgrid/sendgrid-java
import com.sendgrid.Content;
import com.sendgrid.Email;
import com.sendgrid.Mail;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;

@Service
public class EmailServiceUtility {
	public static final String EMAILTO = "emailto";
	public static final String SUBJECT = "subject";
	public static final String EMAILFROM = "emailfrom";
	public static final String EMAIL_BODY = "emailBody";
	@Value("${email.sendgrid.apikey}")
	private String sendGridApiKey;
	
	

	public void sendMail(Map<String, String> map) throws IOException {
		System.out.println("Email Send Utility ====API Key ===" + sendGridApiKey);
		Email from = new Email(map.get(EMAILFROM));
		  
		   
		String subject = map.get(SUBJECT);
		
		Email to = new Email(map.get(EMAILTO));
		Content content = new Content("text/plain",map.get(EMAIL_BODY));
		Mail mail = new Mail(from, subject, to, content);

		SendGrid sg = new SendGrid(sendGridApiKey);
		Request request = new Request();
		try {
			request.setMethod(Method.POST);
			request.setEndpoint("mail/send");
			request.setBody(mail.build());
			Response response = sg.api(request);
			System.out.println(response.getStatusCode());
			System.out.println(response.getBody());
			System.out.println(response.getHeaders());
		} catch (IOException ex) {
			throw ex;
		}
	}

	public String getSendGridApiKey() {
		return sendGridApiKey;
	}

	public void setSendGridApiKey(String sendGridApiKey) {
		this.sendGridApiKey = sendGridApiKey;
	}

	public static void main(String[] args) {
		EmailServiceUtility utility = new EmailServiceUtility();
		Map<String, String> emailMap = new HashMap<>();
		
		emailMap.put("subect", "Failed machine");
		emailMap.put("emailtext", "Load paper Tray");
		// To-Do Email Id of Admin
		emailMap.put(EMAILFROM, "rathor.rajeev@gmail.com");
		emailMap.put(EMAILTO,"rathor.rajeev@gmail.com" );

		try {
			utility.sendMail(emailMap);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static String emailBodyBuilder(Map<String, String> map) {
		// Email Body maker
		String emailBody = ZeroxConstants.emailActivateProfile;
		emailBody = StrSubstitutor.replace(emailBody, map);
		return emailBody;
	}

	public static String createEmailBodyForResetPassword(Map<String, String> emailBody) {
		
		String resetPasswordBody = ZeroxConstants.passwordResetEmailBody;
		String formattedString = StrSubstitutor.replace(resetPasswordBody, emailBody);
				//.replace(resetPasswordBody, emailBody);
		
		return formattedString;
	}
}