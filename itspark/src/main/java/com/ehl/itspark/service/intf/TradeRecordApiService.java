package com.ehl.itspark.service.intf;

import java.util.Date;

import com.ehl.itspark.common.PageDTO;
import com.ehl.itspark.data.entity.TradeRecordEntity;



public interface TradeRecordApiService {

	int save(TradeRecordEntity entity)throws Exception;
	
	PageDTO<TradeRecordEntity> findTradeRecordsByPage(TradeRecordEntity entity,Date startTime,Date endTime,Integer pageIndex,Integer pageSize);
	
	/*List<TradeRecordEntity> getAllTradeRecord(TradeRecordEntity entity,Date startTime,Date endTime);*/
	
	int updateTradeRecord(TradeRecordEntity tradeRecord)throws Exception;
}
