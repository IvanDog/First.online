package com.ehl.itspark.data.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.ehl.itspark.common.MyBatisRepository;
import com.ehl.itspark.data.entity.TradeRecordEntity;

@MyBatisRepository
public interface TradeRecordDao {

	long count(Map<String, Object> para);
	
	List<TradeRecordEntity> findAll(Map<String, Object> para);
	
	int deleteByFlowNo(@Param("flowNo")String flowNo);
	
	int save(TradeRecordEntity entity)throws Exception;
	
	int updateExpense(TradeRecordEntity entity);
	
	int updateResult(TradeRecordEntity entity);
	
	int update(TradeRecordEntity entity)throws Exception;
	
	int updateRecord(TradeRecordEntity entity);
	
}
