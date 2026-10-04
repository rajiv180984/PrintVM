package com.first.test.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.first.test.controller.SmartOfficeServiceController;
import com.first.test.model.UserCouponOffer;

@Service
public class OfferServiceImpl implements OfferService {
	private Logger logger = LoggerFactory.getLogger(OfferServiceImpl.class);
	@Autowired
	MongoTemplate mongoTemplate;

	@Override
	public int getOfferValueAndValidateOffer(String promoCode) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String addCopoun(List<UserCouponOffer> userOfferList) {
		mongoTemplate.insertAll(userOfferList);
		return "SUCCESS";
	}

	@Override
	public String deleteCopounForUser(String mobileNo, String promoCode) {
		// TODO Auto-generated method stub
		Query query = new Query(new Criteria().where("mobileNo").is(mobileNo).andOperator(new Criteria("couponscode").is(promoCode)));
		logger.info(" Query "+ query.toString());
		mongoTemplate.remove(query, UserCouponOffer.class);
		logger.info(" Coupon deleted Successfully ");
		return "SUCCESS";
	}

	@Override
	public float getValidatePromocodeAndValue(String mobileNo, String promoCode) {

		logger.info("========== Customer Id :" + mobileNo + " and promo code " + promoCode);
		Query query = new Query();
		query.addCriteria(Criteria.where("custId").is(mobileNo).andOperator(new Criteria("couponscode").is(promoCode)));
		UserCouponOffer userCouponOffer = mongoTemplate.findOne(query, UserCouponOffer.class);
		logger.info(" Fetched Value :" + userCouponOffer);
		if (userCouponOffer != null && !userCouponOffer.getUsed()) {
			long currentTime = System.currentTimeMillis();
			logger.info(" Current Timestamp"+currentTime);
			if (userCouponOffer.getExpireon() > currentTime) {

				return userCouponOffer.getOfferValue();
			} else {
				logger.info("Promocode Expire on" + userCouponOffer.getExpireon());
			}
		}

		return 0;
	}

}
