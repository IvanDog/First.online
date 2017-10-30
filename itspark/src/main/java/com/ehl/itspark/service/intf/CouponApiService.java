package com.ehl.itspark.service.intf;

import java.util.Date;
import java.util.List;

import com.ehl.itspark.data.entity.CouponEntity;


public interface CouponApiService {

	List<CouponEntity> findCoupons(CouponEntity coupon,Double expense,Date queryTime);
	
	String findRecentCouponID(Date queryTime);
	
	int updateCoupon(CouponEntity coupon) throws Exception;
	
	int editorCoupon(CouponEntity coupon) throws Exception;
	
	int saveCoupon(CouponEntity entity) throws Exception;
	
	int deleteCoupon(String couponID) throws Exception;
}
