package com.ehl.itspark.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ehl.itspark.data.entity.CouponEntity;
import com.ehl.itspark.data.service.intf.CouponService;
import com.ehl.itspark.service.intf.CouponApiService;


@Service
public class CouponApiServiceImpl implements CouponApiService{

	@Autowired
	private CouponService couponService;
	
	@Override
	public List<CouponEntity> findCoupons(CouponEntity coupon,Double expense,Date queryTime) {
		return couponService.findCoupons(coupon,expense,queryTime);
	}

	@Override
	public String findRecentCouponID(Date queryTime) {
		return couponService.findRecentCouponID(queryTime);
	}
	
	@Override
	public int updateCoupon(CouponEntity coupon)  throws Exception{
		return couponService.updateCoupon(coupon);
	}
	
	@Override
	public int editorCoupon(CouponEntity coupon)  throws Exception{
		return couponService.editorCoupon(coupon);
	}
	
	@Override
	public int saveCoupon(CouponEntity coupon)  throws Exception{
		return couponService.saveCoupon(coupon);
	}
	
	@Override
	public int deleteCoupon(String couponID)  throws Exception{
		return couponService.deleteCoupon(couponID);
	}
}
