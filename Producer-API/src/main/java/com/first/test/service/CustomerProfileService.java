package com.first.test.service;

import com.first.test.model.ContrbutorMachines;
import com.first.test.model.Customer;
import com.first.test.model.Factor2OTPResponse;
import com.first.test.model.ResetProfile;
import com.first.test.model.WalletModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface CustomerProfileService {

	//
	
	Factor2OTPResponse createCustomerProfile(Customer customer);
	Customer getCustomerDetailByPhone(String mobileNo);

	String forgetProfile(ResetProfile resetProfile);
	
	String resetPassword(Map<String, String> map, String token);

	String activateProfileByEmail(String token, String mobile);

	HashMap<String, String> activateAccountbyOTP(String mobileNo, String sessionId, String otp, String pwd);

	Customer getProfileDetail(String emailOrMobile);

	String forgetProfileByEmailOrMobile(String userDetail);

	ContrbutorMachines getContrbutorMachines(String emailId);

	Factor2OTPResponse activateForExistingProfile(String emailId);

	void referralToFriend(List<String> friendList, String emailId);
	
	public Customer getProfileByEmailORMobile(String email, String mobileNo);

	Map<String, String> getLoginDetails(Map<String, String> map);

	void logout(String username);
	
	String isValidUser(String userName);

	WalletModel getWalletDetails(String emailId);
	Map<String, Object> existprofile(String data, String type);
	String resetPasswordByOTP(String map, String token);
	

}
