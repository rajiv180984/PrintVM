package com.first.test.service;

import java.util.List;

import com.first.test.model.UserCouponOffer;

public interface OfferService {
  int getOfferValueAndValidateOffer(String promoCode);
  float getValidatePromocodeAndValue(String mobileNo, String promoCode);
  String addCopoun(List<UserCouponOffer> userOfferList);
  String deleteCopounForUser(String mobileNo, String promoCode);
}

