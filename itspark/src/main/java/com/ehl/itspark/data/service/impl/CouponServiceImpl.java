package com.ehl.itspark.data.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ehl.itspark.data.dao.CouponDao;
import com.ehl.itspark.data.entity.CouponEntity;
import com.ehl.itspark.data.service.intf.CouponService;

@Service
@Transactional
public class CouponServiceImpl implements CouponService{

	@Autowired
	private CouponDao couponDao;
	
	@Override
	public List<CouponEntity> findCoupons(CouponEntity coupon,Double expense,Date queryTime) {
		// TODO Auto-generated method stub
		Map<String, Object> para = convertEntityToMap(coupon,expense,queryTime);
		return couponDao.findAll(para);
	}

	@Override
	public String findRecentCouponID(Date queryTime) {
		// TODO Auto-generated method stub
		Map<String, Object> para = convertEntityToMap(null,null,queryTime);
		List<CouponEntity> couponEntites = couponDao.findRecentCouponIDs(para);
		if(couponEntites!=null){
			return couponEntites.get(0).getCouponID();
		}else{
			return null;
		}
	}
	
	private Map<String, Object> convertEntityToMap(CouponEntity coupon,Double expense,Date queryTime) {
		Map<String, Object> para=new HashMap<>();
		if(coupon!=null){
			if(coupon.getCouponID()!=null&&!"".equals(coupon.getCouponID())){
				para.put("couponID", coupon.getCouponID());
			}
			if(coupon.getCouponOwner()!=null&&!"".equals(coupon.getCouponOwner())){
				para.put("couponOwner", coupon.getCouponOwner());
			}
			if(coupon.getCouponState()!=null){
				para.put("couponState", coupon.getCouponState());
			}
			if(expense != null){
				para.put("expense", expense);
			}
			if(queryTime != null){
				para.put("queryTime", queryTime);
			}
		}
		return para;
	}


	@Override
	public int updateCoupon(CouponEntity coupon) throws Exception{
		// TODO Auto-generated method stub
		if(coupon==null){
			throw new Exception("更新的数据为空！");
		}
		return couponDao.update(coupon);
	}

	@Override
	public int editorCoupon(CouponEntity coupon) throws Exception{
		// TODO Auto-generated method stub
		if(coupon==null){
			throw new Exception("更新的数据为空！");
		}
		return couponDao.editor(coupon);
	}
	
	@Override
	public int saveCoupon(CouponEntity coupon) throws Exception{
		// TODO Auto-generated method stub
		if(coupon==null){
			throw new Exception("保存的数据为空！");
		}
		return couponDao.save(coupon);
	}
	
	@Override
	public int deleteCoupon(String couponID) throws Exception{
		// TODO Auto-generated method stub
		if(couponID==null || "".equals(couponID)){
			throw new Exception("删除的数据为空！");
		}
		return couponDao.deleteByCouponID(couponID);
	}
}
