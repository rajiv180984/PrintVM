package com.first.test.service;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.lang3.RandomStringUtils;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
/*import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
*/import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.first.test.config.ServiceConfig;
import com.first.test.doa.CustomerProfileDOA;
import com.first.test.exception.EmailSenderException;
import com.first.test.exception.InvalidDataException;
import com.first.test.exception.NoRecordFoundException;
import com.first.test.exception.OTPAuthenticationException;
import com.first.test.exception.ProfileException;
import com.first.test.exception.UserAlreadyExistException;
import com.first.test.model.AutherityRoles;
import com.first.test.model.ContrbutorMachines;
import com.first.test.model.Customer;
import com.first.test.model.Factor2OTPResponse;
import com.first.test.model.ReferralFriend;
import com.first.test.model.ResetProfile;
import com.first.test.model.UserCredentialOauth;
import com.first.test.model.WalletModel;
import com.first.test.notification.EmailServiceUtility;
import com.first.test.util.CommonUtility;
import com.first.test.util.ErrorCodeNo;
import com.first.test.util.ProfileStatus;
import com.first.test.util.RandomStringGenerator;
import com.first.test.util.Roles;
import com.first.test.util.ServiceStatus;
import com.google.gson.Gson;
import com.mongodb.MongoWriteException;
import com.mongodb.client.result.UpdateResult;

@Service
public class CustomerProfileServiceImpl implements CustomerProfileService {

	private static final String VERIFIED = "Verified";
	private Logger logger = LoggerFactory.getLogger(CustomerProfileServiceImpl.class);
	@Autowired
	CustomerProfileDOA CustomerProfileDOA;

	@Autowired
	MongoTemplate mongoTemplate;

	@Autowired
	ServiceConfig serviceConfig;

	@Autowired
	EmailServiceUtility emailServiceUtility;

	@Autowired
	private Gson gson;
	
	@PersistenceContext
	private EntityManager entityManager;
	
	@Autowired
	SMSSendService smsSendService;
	
	
	/*@Autowired
	RedisTemplate<String, Object> redisTemplate;
	*/
	
/*	@Resource(name="redisTemplate")
    private HashOperations<String, String, Customer> hashOps;
	
*/
	@Override
	//@Transactional
	public Factor2OTPResponse createCustomerProfile(Customer customer) {
		// credetias
		Factor2OTPResponse factor2OTPResponse = null;
		
		try {
			customer = customerProfilePersist(customer);
			// To-Do In future this call will be add in Kafka and Run Asynch
			sendMailForActivateProfile(customer, serviceConfig.getActivateProfileSubject(), customer.getUserCredentials().getWebLinkForActivation());
		
			factor2OTPResponse = smsSendService.sendOTPForActivateAccount(customer.getMobileNo());
		/*	factor2OTPResponse = new Factor2OTPResponse();
			factor2OTPResponse.setDetails("yiud-f4gdfg-fklg");
			factor2OTPResponse.setStatus("SUCCESS");
		*/	
		
			
		} catch (MongoWriteException | DuplicateKeyException e) {
			Customer existingCust = getProfileByEmailORMobile(customer.getEmailId(), customer.getMobileNo());
			logger.info(" Profile Exist {} " , existingCust);
			if (existingCust != null && existingCust.getActiveStatus().equals(ProfileStatus.NEW.name())) {
				logger.info("Already Exsist but not acivated");
				throw new UserAlreadyExistException("Profile Exist but Deactivated",
						ErrorCodeNo.DEACTIVATED.getValue());
			}
			if (existingCust != null) {
				logger.info("Already Exsist");
				factor2OTPResponse = new Factor2OTPResponse();
				if(existingCust.getEmailId().equalsIgnoreCase(customer.getEmailId())) {
				//throw new UserAlreadyExistException("EMAILID already Exist", ErrorCodeNo.DUPLICATE_EMAIL.getValue());
					logger.error("EMAILID already Exist", ErrorCodeNo.DUPLICATE_EMAIL.getValue());
					
					factor2OTPResponse.setStatus(String.valueOf(ErrorCodeNo.DEACTIVATED.getValue()));
					factor2OTPResponse.setDetails(ErrorCodeNo.DEACTIVATED.name());
				}else if(existingCust.getMobileNo().equalsIgnoreCase(customer.getMobileNo())) {
					//throw new UserAlreadyExistException("Mobile already Exist", ErrorCodeNo.DUPLICATE_MOBILE.getValue());
					logger.error(("Mobile already Exist" + ErrorCodeNo.DUPLICATE_MOBILE.getValue()));
					factor2OTPResponse.setStatus(String.valueOf(ErrorCodeNo.DUPLICATE_MOBILE.getValue()));
					factor2OTPResponse.setDetails(ErrorCodeNo.DUPLICATE_MOBILE.name());
				}
			}
			logger.error(" Failed to write data ", e);
			// getProfileDetail(emailOrMobile);
			//throw new UserAlreadyExistException(customer.getEmailId() + " OR " + customer.getMobileNo(), ErrorCodeNo.DUPLICATE_DATA.getValue());
		}catch (ProfileException e) {
			// TODO: handle exception
			factor2OTPResponse = new Factor2OTPResponse();
			
			factor2OTPResponse.setStatus(String.valueOf(ErrorCodeNo.DUPLICATE_MOBILE.getValue()));
			factor2OTPResponse.setDetails(ErrorCodeNo.DUPLICATE_MOBILE.name());
		}
		catch (Exception e) {
			// TODO: handle exception
			logger.error("Failed to create profile ", e);
			//throw new InvalidDataException(" Failed to persist data");
			
			factor2OTPResponse = new Factor2OTPResponse();
			factor2OTPResponse.setStatus(String.valueOf(ErrorCodeNo.ProfileFailed.getValue()));
			factor2OTPResponse.setDetails(ErrorCodeNo.ProfileFailed.name());
		}
		return factor2OTPResponse;
	}

	@Async
	private boolean referralCodeProfileAndPromocodeVerification(Customer customer) {
		// First add Referral Code 
		LocalDateTime current =  LocalDateTime.now();  
		Timestamp currentTimestamp = Timestamp.valueOf(current);
		try {
		ReferralFriend referral = new ReferralFriend();
		referral.setReferalByEmailId(customer.getEmailId());
		referral.setReferalCode(customer.getReferralCode());
		referral.setMobile(customer.getMobileNo());
		referral.setCount(0);
		referral.setEarnedMoney(0);
		referral.setEnable(true);
		referral.setPromotype("referral");
		LocalDateTime nextMonth = current.plusDays(30);
    	Timestamp expire = Timestamp.valueOf(nextMonth);
    	referral.setCreateOn(current);
    	referral.setExpireOn(nextMonth);
    	referral.setReferralMoney(serviceConfig.getReferalMoney());
		// Add value in DB, So that User can refer and earn
    	mongoTemplate.save(referral);
		}catch (Exception e) {
			logger.error("Failed to Add Refferral");
		}
		
		// Verification of Promocode getRefferal Detail by referralCode 
		if(customer.getPromocode() == null) {
			return false;
		}
		try {
		ReferralFriend fetchReferralFriend = fetchReferralFriend(customer.getPromocode());
		// update wallet details
		if(fetchReferralFriend != null) {
			logger.info("Referral Processing {}", fetchReferralFriend);
			logger.info("====== Is expire code {}", fetchReferralFriend.getExpireOn().isAfter(current));
			if(fetchReferralFriend.getCount()<5 & fetchReferralFriend.getExpireOn().isAfter(current)	) {
				logger.info(" referral code processing ===========");
				
				Query query = new Query(Criteria.where("referalByEmailId").is(fetchReferralFriend.getReferalByEmailId()));
				Update update = new Update();
				update.set("count", fetchReferralFriend.getCount()+1);
				update.set("earnedMoney",fetchReferralFriend.getEarnedMoney() + fetchReferralFriend.getReferralMoney());
				
				mongoTemplate.updateFirst(query, update, ReferralFriend.class);
				
				// Add amount in Wallet
				addReferalAmountInWallet(fetchReferralFriend.getReferalByEmailId(), fetchReferralFriend.getEarnedMoney());
				return true;
			}
		}

		}catch (Exception e) {
			logger.error("Failed to proecss referral", e);
		}
		// count > 5
		// expire> 
		// add data in Wallet
    	return false;
	}

	private void addReferalAmountInWallet(String emailId, int earnedMoney) {
		if (earnedMoney > 50) {
			return;
		}
		try {

			logger.info(" Earned money {}", earnedMoney);
			Query query = new Query(Criteria.where("emailId").is(emailId));
			WalletModel wallet = mongoTemplate.findOne(query, WalletModel.class);
			logger.info("Fetched Wallet record {}", wallet);
			Update update = new Update();
			update.set("balance", wallet.getBalance() + earnedMoney);
			mongoTemplate.updateFirst(query, update, WalletModel.class);
			logger.info(" Balance added in Wallet of account :{}", emailId);
			
		} catch (Exception e) {
			logger.info(" Failed to update Referral", e);
		}
	}

	private ReferralFriend fetchReferralFriend(String promocode) {
		// TODO Auto-generated method stub
		Query query = new Query(Criteria.where("referalCode").is(promocode));
		return mongoTemplate.findOne(query, ReferralFriend.class);
		
	}

	private Customer customerProfilePersist(Customer customer) {
		customer.setRole(AutherityRoles.ROLE_USER.name());
		String uuid = UUID.randomUUID().toString();
		customer.setCustId(uuid);
		customer.getUserCredentials().setUuid(uuid);
		customer.setActiveStatus(ProfileStatus.NEW.name());
		customer.getUserCredentials().setStatus(ProfileStatus.NEW.name());
		// create weblink for acivation
		String url = activateProfleURL(customer);
		customer.getUserCredentials().setWebLinkForActivation(url);
		customer.getUserCredentials().setUserName(customer.getEmailId());
		customer.getUserCredentials().setPassword(customer.getPassword());
		customer.setAddress(customer.getCityName());
		CustomerProfileDOA.createCustomerProfile(customer);
		return customer;
	}

	private void sendMailForActivateProfile(Customer customer, String subject, String url) {
		HashMap<String, String> bodyMap = new HashMap<>();
		bodyMap.put("name", customer.getCustName());
		bodyMap.put("url",  url);
		String body = EmailServiceUtility.emailBodyBuilder(bodyMap);
		HashMap<String, String> emailMap = new HashMap<>();
		emailMap.put(EmailServiceUtility.EMAILFROM, serviceConfig.getAdminEmail());
		emailMap.put(EmailServiceUtility.EMAILTO, customer.getEmailId());
		emailMap.put(EmailServiceUtility.SUBJECT, subject);
		emailMap.put(EmailServiceUtility.EMAIL_BODY, body);

		logger.info(" Email ====================="+ body );
		try {
			emailServiceUtility.sendMail(emailMap);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			// FIXME Inegration to handle failure
			logger.error("Failed to send Mail for Activation", e);
		}
	}

	private String activateProfleURL(Customer customer) {
		StringBuilder strBld = new StringBuilder(serviceConfig.getActivateLinkURL())
				.append(customer.getUserCredentials().getUuid()).append("?key=").append(customer.getMobileNo());
		logger.info(" Activate PRofile " + strBld.toString());
		return strBld.toString();
	}

	@Override
	public Customer getCustomerDetailByPhone(String emailId) {
		Query query = new Query();
		Criteria crit1 = new Criteria("emailId").is(emailId);
		Criteria crit2 = new Criteria("role").is(Roles.CONTRIBUTOR.name());
		Criteria criteria = new Criteria().andOperator(crit1, crit2);
		query.addCriteria(criteria);
		return mongoTemplate.findOne(query, Customer.class);
		
	}

	@Override
	public String forgetProfile(ResetProfile resetProfile) {
		// get the profile from Cust
		Query query = new Query(new Criteria().where("emailId").is(resetProfile.getEmail()));
		Customer customer = mongoTemplate.findOne(query, Customer.class);

		if (customer == null) {
			throw new InvalidDataException("incorrect EamilId");
		}

		ResetProfile resetP = saveForgetPassword(customer);
		// send email to User
		Map<String, String> emailMap = new HashMap<>();
		try {
			emailServiceUtility.sendMail(emailMap);
		} catch (IOException e) {
			// TODO Auto-generated catchs block
			e.printStackTrace();
			throw new EmailSenderException("Failed to Send Eamil");
		}
		return "SUCCESS";
	}

	private ResetProfile saveForgetPassword(Customer customer) {

		String token = UUID.randomUUID().toString();
		token = token.replaceAll("-", "");

		ResetProfile resetP = new ResetProfile();
		StringBuilder resetURL = new StringBuilder(serviceConfig.getResetPasswordAPIURL()).append(token)
				.append("?emailId=").append(customer.getEmailId());
		resetP.setCustId(customer.getCustId());
		resetP.setCreatedOn(System.currentTimeMillis());
		resetP.setEmail(customer.getEmailId());
		resetP.setResetURL(resetURL.toString());

		resetP.setUserName(customer.getUserCredentials().getUserName());
		resetP.setUsed(false);
		resetP.setTokenStr(token);

		mongoTemplate.save(resetP);
		// save the profile
		// send email

		return resetP;

	}

	// Change the Password
	@Override
	@Transactional
	public String resetPassword(Map<String, String> map, String token) {
		// TODO Auto-generated method stub
		// Verify input from ResteProfile Table
		String updateStatus = validateResetProfileFromDB(map);
		if (updateStatus.equalsIgnoreCase(ServiceStatus.FAILED.name())) {
			throw new InvalidDataException(" Does not Exist");
		}
		// update Customer table
		updatePassword(map.get("username"), map.get("newpassword"));
		return ServiceStatus.SUCCESS.name();
	}

	@Override
	@Transactional
	public String resetPasswordByOTP( String mobileNo, String newPassword) {
					//CommonUtility.randomString(8);
		
		logger.info("======= Mobile =========:"+ mobileNo);
		Query query = new Query(new Criteria().where("mobileNo").is(mobileNo));
		//query.fields().include("emailId");
		Customer customer = mongoTemplate.findOne(query, Customer.class);
		if(customer == null) {
			throw new NoRecordFoundException("No record Exist");
		}
		  String emailId = customer.getEmailId();
		    System.out.println("==== User Name "+ emailId);
			BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
			String hashedPassword = "{bcrypt}".concat(passwordEncoder.encode(newPassword));
			
			Session session =entityManager.unwrap(Session.class);
			
			org.hibernate.Query qry = session.createQuery("update UserCredentialOauth set password =:passowrd, enabled=1 where username=:username");
			qry.setParameter("username", emailId);
			qry.setParameter("passowrd", hashedPassword);
				int updateCount = qry.executeUpdate();
			if(updateCount>0) {
				return ServiceStatus.SUCCESS.name();
			}else {
				throw new NoRecordFoundException("No record Exist");
			}
		
		
	}

	private String validateResetProfileFromDB(Map<String, String> map) {
		try {
		String userName = map.get("username");
		@SuppressWarnings("unchecked")
		List<UserCredentialOauth> list = entityManager
				.createQuery("select u from UserCredentialOauth u where u.username = :username")
				.setParameter("username", userName)
				.setMaxResults(1)
				.getResultList();
	    if(list.isEmpty()) {
	    	logger.info(" No Record Exist ");
	    	return ServiceStatus.FAILED.name();
	    }
	    // FIXME security: the old-password check is still disabled (as before), so anyone who knows a
	    // username can reset that password. To enable it:
	    // if (!new BCryptPasswordEncoder().matches(map.get("oldpassword"), list.get(0).getPassword().replace("{bcrypt}", ""))) {
	    //     return ServiceStatus.FAILED.name();
	    // }
		}catch (Exception e) {
			logger.error("Failed to update passowrd", e);
			return ServiceStatus.FAILED.name();
		}
		return ServiceStatus.SUCCESS.name();
	}

	@Override
	public String activateProfileByEmail(String token, String mobile) {
		// TODO Auto-generated method stub
		// token number
		try {
			logger.debug("Token Id {}", token);
			Query query = new Query(Criteria.where("userCredentials.uuid").is(token));
			Customer user = mongoTemplate.findOne(query, Customer.class);
			logger.debug("========= User {}", user);
			// TO-Do Logic Expire Activation Link
			if (user == null | user.getActiveStatus().equals(ProfileStatus.BLOCKED.name())) {
				throw new InvalidDataException("User Does not exist or Blocked Can not enable");
			}
			
			if(user.getActiveStatus() == ProfileStatus.ACTIVE.name()) {
				throw new InvalidDataException("Profile already Activated");
			}
			user.setActiveStatus(ProfileStatus.ACTIVE.name());
			Update update = new Update();
			update.set("activeStatus", ProfileStatus.ACTIVE.name());
			update.set("webLinkForActivation", "");
			mongoTemplate.updateFirst(query, update, Customer.class);
			enableProfileInAuthService(user.getUserCredentials().getUserName());
			
			// To-Do Add refercode in DB It should be Asynch using kafka 
			// This code persist referralCode and Verify Promocode and add Amount in Wallet
			// referral amount after activation			
			referralCodeProfileAndPromocodeVerification(user);
			
			// add referral referral balance
			
		} catch (Exception e) {
			logger.error(" Failed to activate Profile ", e);
			return "FAIL";
		}
		return "SUCCESS";
	}

	@Override
	public HashMap<String, String> activateAccountbyOTP(String mobileNo, String sessionId, String otp, String pwd) {
		// https://2factor.in/API/V1/0188b88c-0a2d-11ea-9fa5-0200cd936042/SMS/VERIFY/290a3a31-b024-4812-b015-da31cb527cb8/796491
		// send OTP resp
		// {"Status":"Success","Details":"bd519723-4227-11e7-94da-0200cd936042"}

		StringBuilder urlBuilder = new StringBuilder(serviceConfig.getFactor2baseURL());
		urlBuilder.append(serviceConfig.getFactorAPIKEY()).append("/SMS/VERIFY/").append(sessionId).append("/")
				.append(otp);
		logger.info("OTP Number :" + urlBuilder.toString());
		RestTemplate restTemplate = new RestTemplate();
		// String url =
		// "https://2factor.in/API/V1/0188b88c-0a2d-11ea-9fa5-0200cd936042/SMS/VERIFY/7ee2e11e-3224-4bca-a516-ce0af9e926ae/810034";
		//
		ResponseEntity<String> response = restTemplate.exchange(urlBuilder.toString(), HttpMethod.GET, null,
				String.class);

		if (response.getStatusCodeValue() != 200) {
			throw new OTPAuthenticationException("Authentication Service Unavailable");
		}

		logger.info("======OTP Verification Status=== " + response.getBody());
		if (response.getStatusCodeValue() == 200) {
			HashMap<String, String> otpResultMap = gson.fromJson(response.getBody(), HashMap.class);
			System.out.println("================= " + otpResultMap.toString());
			// {"Status":"Success","Details":"OTP Matched"}

			if (!otpResultMap.get("Status").equals("Success") || !otpResultMap.get("Details").equals("OTP Matched")) {
				throw new OTPAuthenticationException("Invalid OTP");
			}
                        
			logger.info("======pwd ========= "+pwd);
			if(!pwd.equals("NOPWD")) {
				// change Password by OTP
				resetPasswordByOTP(mobileNo, pwd);
				 otpResultMap.put("pwdUpdate", "true");
				 return otpResultMap;
			}else {
			// Activate account in MongoDB by OTP
			   activateProfileInMongoDB(mobileNo);
			   otpResultMap.put("activateProfile", "true");
			   return otpResultMap;
			}
			
			
						
			// To-do
			// Update in OAuth Database
			//return otpResultMap;
		}

		return new HashMap<String, String>();

	}

	private void activateProfileInMongoDB(String mobileNo) {
		// TODO Auto-generated method stub
		Query query = new Query(new Criteria().where("mobileNo").is(mobileNo));
		Update update = new Update();
		update.set("activeStatus", ProfileStatus.ACTIVE.name());
		update.set("userCredentials.status", ProfileStatus.ACTIVE.name());
		UpdateResult updateResult = mongoTemplate.updateFirst(query, update, Customer.class);
		
		Customer customer = mongoTemplate.findOne(query, Customer.class);
		// Enable User profile 
		String userName = customer.getUserCredentials().getUserName();
		enableProfileInAuthService(userName);
		// To-Do Add refercode in DB It should be Asynch using kafka 
		// This code persist referralCode and Verify Promocode and add Amount in Wallet
		// referral amount after activation	

		referralCodeProfileAndPromocodeVerification(customer);


	}

	
	private void enableProfileInAuthService(String userName) {
		try {
		Session session =entityManager.unwrap(Session.class);
		org.hibernate.Query qry = session.createQuery("update UserCredentialOauth set enabled = true where  username=:username");
		qry.setParameter("username", userName);
		qry.executeUpdate();
		} catch (Exception e) {
			// TODO: handle exception
			logger.error(" Failed to update", e);
		}
		
	}


	@Override
	//@Cacheable(value = "profiles", key = "#emailOrMobile", cacheManager="cacheManager1Hour")
	public Customer getProfileDetail(String emailOrMobile) {
		
		// check profile exist in cache
	/*	Customer customerRedis = hashOps.get(ZeroxConstants.CUSTOMER_PROFILE, emailOrMobile);
		if(customerRedis != null) {
			System.out.println("Reading from Cache===================");
			return customerRedis;
		}
	*/	String mobileRegEx =  ".*[^0-9].*";
		
		Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$",
				Pattern.CASE_INSENSITIVE);
		Pattern VALID_MOBILE_REGEX = Pattern.compile(mobileRegEx);
		
		Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailOrMobile);
		Matcher matcherMobile = VALID_MOBILE_REGEX.matcher(mobileRegEx);
		boolean isMail = matcher.find();
		boolean isMobile = matcherMobile.find();
		logger.info("This is mail "+ isMail);
		Criteria criteria = null;
		
		if(isMail) {
			criteria = new Criteria().where("emailId").is(emailOrMobile);
		}else {
			logger.info("This is mobile formate  "+ isMobile);
			if(!isMobile) {
				logger.error("incorrect formate ");
				throw new InvalidDataException("Email or Mobile Formate is incorrect");
			}
			criteria = new Criteria().where("mobileNo").is(emailOrMobile);
		}
		Query query = new Query(criteria);
		Customer customer = mongoTemplate.findOne(query, Customer.class);
		logger.info("  Customer Details :{}" ,customer.toString());
		if(customer != null) {
		customer.setUserCredentials(null);
		customer.setCustId(null);
		customer.setId(null);
			if (customer.getEducation() == null) {
				customer.setEducation("Graduate");
			}
		}
		
		Query query2 = new Query(Criteria.where("wid").is(customer.getWid()));
		WalletModel wallet = mongoTemplate.findOne(query2, WalletModel.class);
		customer.setWid(wallet.getWid());
		customer.setBalance(wallet.getBalance());
		// persist in Redis
		//hashOps.put(ZeroxConstants.CUSTOMER_PROFILE, customer.getEmailId(), customer);
				
		return customer;
	}

	@Override
	public String forgetProfileByEmailOrMobile(String userDetail) {
		 // To-Do remove record from Redis 
		String emailORmobile = userDetail;
		logger.info("@@@@@@@@@@@=========== New password shared on Email ============="+userDetail);
		
		if(CommonUtility.validMobile(userDetail)) {
			logger.info(" New password shared on Mobile =============");
			Query query = new Query(Criteria.where("mobileNo").is(userDetail));
			Customer customer = mongoTemplate.findOne(query, Customer.class);
			if(customer == null) {
				throw new NoRecordFoundException("No User Exist");
			}
			String newPassword = updatePassword(customer.getEmailId(), null);
			Factor2OTPResponse factor2OTPResponse = smsSendService.sendOTPForActivateAccount(userDetail);
			return factor2OTPResponse.getStatus().concat("@@").concat(factor2OTPResponse.getDetails());
		}
		
		return ServiceStatus.FAILED.name();
	}

	private String updatePassword(String emailId, String newPassword) {
		// TODO Auto-generated method stub
		if(newPassword == null ) {
		 newPassword = RandomStringUtils.randomAlphabetic(7);
		}
				//CommonUtility.randomString(8);
		System.out.println("==== User Name "+ emailId);
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String hashedPassword = "{bcrypt}".concat(passwordEncoder.encode(newPassword));
		
		Session session =entityManager.unwrap(Session.class);
		
		org.hibernate.Query qry = session.createQuery("update UserCredentialOauth set password =:passowrd where username=:username");
		qry.setParameter("username", emailId);
		qry.setParameter("passowrd", hashedPassword);
		int updateCount = qry.executeUpdate();
		if(updateCount>0) {
			return newPassword;
		}else {
			throw new NoRecordFoundException("No record Exist");
		}
		
	}

	
		
	

	@Override
	public ContrbutorMachines getContrbutorMachines(String emailId) {
		Query query = new Query(Criteria.where("emailId").is(emailId));
		query.fields().include("emailId").include("machineIds");
		return mongoTemplate.findOne(query, ContrbutorMachines.class);
		
	}

	@Override
	public Factor2OTPResponse activateForExistingProfile(String emailIdOrMobileNo) {
		logger.info("============ Activate Profile: {} ============= ",emailIdOrMobileNo);
		Factor2OTPResponse factor2OTPResponse = null;
		Query query = null;
		if(CommonUtility.validMobile(emailIdOrMobileNo)) {
			logger.info("============ Mobile ============= ");
		 query = new Query(Criteria.where("mobileNo").is(emailIdOrMobileNo));
		}else {
			logger.info("============ EmailID ============= ");
			 query = new Query(Criteria.where("emailId").is(emailIdOrMobileNo));
		}
		Customer customer = mongoTemplate.findOne(query, Customer.class);
		
		if(customer == null) {
			throw new NoRecordFoundException("Profile does not exist");
		}
		
		if( customer.getActiveStatus().equals(ProfileStatus.NEW.name())) {
			
			// Send mail
			String url = activateProfleURL(customer);
			sendMailForActivateProfile(customer, serviceConfig.getActivateProfileSubject(), url);

			// send OTP
			 factor2OTPResponse = smsSendService.sendOTPForActivateAccount(customer.getMobileNo());
			
		}
			
		
		return factor2OTPResponse;
	}

	@Override
	public void referralToFriend(List<String> friendList, String emailId) {
	
		// If already exist in customer then do'nt insert in DB
		if(friendList.size()>5) {
		friendList = friendList.subList(0, 4);
		}
		Query query = new Query(Criteria.where("emailId").in(friendList));
		List<Customer> custList = mongoTemplate.find(query, Customer.class);
		if(custList.size()>0) {
			for(Customer cust: custList) {
				friendList.remove(cust.getEmailId());
				
				
			}
		}
		
		ReferralFriend rf = null;
		for(String  friendemail: friendList) {
			rf = new ReferralFriend();
			rf.setReferalByEmailId(emailId);
			rf.setReferalEmailId(friendemail);
			// referral code should be random generated
			
			rf.setReferalCode(RandomStringGenerator.randomStringGenerator(6));
			mongoTemplate.save(rf);
		}
	}

	@Override
	public Customer getProfileByEmailORMobile(String email, String mobileNo) {
		// TODO Auto-generated method stub
		Criteria criteria = new Criteria();
        criteria.orOperator(Criteria.where("emailId").is(email),Criteria.where("mobileNo").is(mobileNo));
		Query query = new Query(criteria);
		return mongoTemplate.findOne(query, Customer.class);
		
	}

	@Override
	public Map<String, String> getLoginDetails(Map<String, String> map) {
		
		// call Auth API from Rest Client
		
		// Evalute Key
		// Put in Cache
			
		return map;
		// 
		
	}

	@Override
	public void logout(String username) {
		// TODO Auto-generated method stub
		
		// evict from cache
	}

	@Override
	public String isValidUser(String userName) {
		// TODO Auto-generated method stub
		// Evaluate Key
		// check in DB
		return ServiceStatus.FAILED.name();
	}

	@Override
	public WalletModel getWalletDetails(String emailId) {
		// TODO Auto-generated method stub
		try {
			Query query = new Query(Criteria.where("emailId").is(emailId));
			query.fields().exclude("custId").exclude("securityKey");
			WalletModel model = mongoTemplate.findOne(query, WalletModel.class);
			logger.info("==================={} ",model);
			return model;
		}catch (Exception e) {
			// TODO: handle exception
			logger.error("Failed to fetch Walle Details ", e);
			throw new NoRecordFoundException("Failed to find record");
			
		}
	}

	@Override
	public Map<String, Object> existprofile(String data, String type) {
		Query query = null;
		if(type.equalsIgnoreCase("EMAILID")) {
			query = new Query(Criteria.where("emailId").is(data));
		}else if(type.equalsIgnoreCase("mobile")) {
			query = new Query(Criteria.where("mobileNo").is(data));
		}
		Customer customer = mongoTemplate.findOne(query, Customer.class);
		Map<String, Object> map = new HashMap<>();
		if (customer == null) {
			map.put("exist", false);
		} else {
			map.put("exist", true);
			if (!customer.getActiveStatus().equals("ACTIVE")) {
				map.put("ACTIVE", false);
			} else {
				map.put("ACTIVE", true);
			}
		}

		return map;
		
	}
	
}
