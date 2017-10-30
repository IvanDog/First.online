package com.ehl.itspark.data.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.ehl.itspark.common.MyBatisRepository;
import com.ehl.itspark.data.entity.CouponEntity;


@MyBatisRepository
public interface CouponDao {

	List<CouponEntity> findAll(Map<String, Object> para);
	
	List<CouponEntity> findRecentCouponIDs(Map<String, Object> para);
	
	int save(CouponEntity entity);
	
	int update(CouponEntity entity);
	
	int editor(CouponEntity entity);
	
	long count(Map<String, Object> para);
	
	int deleteByCouponID(@Param("couponID")String couponID);
}
