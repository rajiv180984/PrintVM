package com.first.test.doa;

import java.util.UUID;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.lang3.RandomStringUtils;
/*
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.Session;
*/import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
//import org.springframework.data.redis.core.HashOperations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.first.test.exception.ProfileException;
import com.first.test.model.AutherityRoles;
import com.first.test.model.Customer;
import com.first.test.model.OauthAutherities;
//import com.first.test.model.OauthAutherities;
import com.first.test.model.UserCredentialOauth;
import com.first.test.model.WalletModel;
import com.first.test.util.ErrorCodeNo;
import com.first.test.util.ZeroxConstants;
import com.mongodb.DuplicateKeyException;

@Repository
public class CustomerProfileDOAImpl implements CustomerProfileDOA{


	private Logger logger = LoggerFactory.getLogger(CustomerProfileDOAImpl.class);
	
	@PersistenceContext
	private EntityManager entityManager;
	
	@Autowired
	MongoTemplate mongoTemplate;

	private final String passwordPrefix = "{bcrypt}";
	
	/*@Autowired
	RedisTemplate<String, Object> redisTemplate;
	*/
/*	@Resource(name="redisTemplate")
    private HashOperations<String, String, Customer> hashOps;
	*/
	/*
	 * Delete Customer API for testing 
	 * 
	 */
	
	@Override
	@Transactional
	public Customer createCustomerProfile(Customer customer) {
		try {
			String refferalCode = RandomStringUtils.randomAlphanumeric(6);
			UUID uuid = UUID.randomUUID();
			String randomUUIDString = uuid.toString();
			customer.setCustId(randomUUIDString);
			customer.setReferralCode(refferalCode);
			// To-Do encryption for UserName and Password
			boolean isPersist = persistUserCredentialForAutherization(customer);
			logger.info("Profile saving is :{}", isPersist);
			if (isPersist) {
				logger.info(" wallet creation initiaed===========");
				WalletModel walletModel = createWalletForNewUser(customer);
				logger.debug("Profile creation started :{}", walletModel.toString());
				mongoTemplate.save(walletModel);

				logger.info(" wallet created Success fully===========");

				customer.setWid(walletModel.getWid());
				customer.setBalance(0.0f);

				logger.info("Profile creation started ");
				// FIXME
				// decode password
				customer.setPassword(null);
				if (walletModel == null || walletModel.getWid() == null || customer.getWid() == null) {
					logger.error("Wallet Setup failed");
					throw new ProfileException(" Profile failed create", 501);
				}
				mongoTemplate.save(customer);
				logger.info("Profile Created");
				mongoTemplate.save(customer.getUserCredentials());
				logger.info("Profile Created save credential ");
				// Add profile in Redis
				/*
				 * try { hashOps.put(ZeroxConstants.CUSTOMER_PROFILE, customer.getEmailId(),
				 * customer); }catch (Exception e) {
				 * logger.error("Caching failed to cache data ", e); }
				 */

			}else {
				//check already exist 
				throw new ProfileException("Failed to create Profile");
			}
		} catch (Exception e) {
		    logger.info("failed", e);
			throw new ProfileException("Unable to create Profile", ErrorCodeNo.DUPLICATE_PROFILE.getValue());
		}
		return customer;
	}

	private WalletModel createWalletForNewUser(Customer customer) {
		// TODO Auto-generated method stub
		UUID uuid = UUID.randomUUID();
       
		String randomUUIDString = uuid.toString();
		WalletModel walletModel = new WalletModel();
		
		walletModel.setWid(randomUUIDString); 
		
		walletModel.setCustId(customer.getCustId());
		walletModel.setEmailId(customer.getEmailId());
		walletModel.setMobileNo(customer.getMobileNo());
		
		
		walletModel.setCurrencyName("INR");
	    walletModel.setBalance(0.0f);
	    walletModel.setCreateDate(System.currentTimeMillis());
	    walletModel.setEnable(true);
	    walletModel.setLastTransactiotime(System.currentTimeMillis());
	    walletModel.setRedeemPoint(0);
	    // FIXME
	    walletModel.setSecurityKey("YETTODECIDE");
		
	    return walletModel;
	}


	private boolean persistUserCredentialForAutherization(Customer customer) {
		// TODO Auto-generated method stub
		try {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String hashedPassword = passwordEncoder.encode(customer.getUserCredentials().getPassword());
		UserCredentialOauth userCredentialOauth = new UserCredentialOauth();
		userCredentialOauth.setEnabled(false);
		userCredentialOauth.setUsername(customer.getUserCredentials().getUserName());
		userCredentialOauth.setPassword("{bcrypt}"+hashedPassword);
		entityManager.persist(userCredentialOauth);
		
		// to-do add logic for Partner registration, Contribution registration
		
		OauthAutherities oauthAutherities = new OauthAutherities();
		oauthAutherities.setAuthority(AutherityRoles.ROLE_USER.name());
		oauthAutherities.setUsername(customer.getUserCredentials().getUserName());
		entityManager.persist(oauthAutherities);
		return true;
		} catch (Exception e) {
			// TODO: handle exception
			logger.error(" Failed to persist ", e);
		}

		return false;
	}

	
}
