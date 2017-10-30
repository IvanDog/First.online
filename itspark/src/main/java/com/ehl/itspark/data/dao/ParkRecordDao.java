package com.ehl.itspark.data.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.ehl.itspark.common.MyBatisRepository;
import com.ehl.itspark.data.entity.ParkRecordEntity;

@MyBatisRepository
public interface ParkRecordDao {

	List<ParkRecordEntity> findAll(Map<String, Object> para);
	
	long count(Map<String, Object> para);
	
	int deleteByPlate(@Param("plate")String plate);
	
	int save(ParkRecordEntity entity)throws Exception;
	
	int updatePayment(ParkRecordEntity entity);
	
	int updateTradeFlow(ParkRecordEntity entity);
}
