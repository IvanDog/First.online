package com.ehl.itspark.data.service.intf;

import java.util.Date;

import com.ehl.itspark.common.PageDTO;
import com.ehl.itspark.data.entity.TradeRecordEntity;

public interface TradeRecordService {

	int save(TradeRecordEntity entity)throws Exception;
	
	void saveTradeRecord(TradeRecordEntity entity);
	
	PageDTO<TradeRecordEntity> findTradeRecordsByPage(TradeRecordEntity entity,Date startTime,Date endTime,Integer pageIndex,Integer pageSize);
	
	int updateTradeRecord(TradeRecordEntity tradeRecord);
	
	int updateRecord(TradeRecordEntity tradeRecord);
	
/*	List<TradeRecordEntity> getSuccessTradeRecord(String thirdType,Date billDate);
	
	List<TradeRecordEntity> getAllTradeRecord(String thirdType,Date billDate);
	
	List<TradeRecordEntity> getAllTradeRecord(TradeRecordEntity entity,Date startTime,Date endTime);*/
	
	TradeRecordEntity getTradeRecordByOrder(String orderNo);
	
}
