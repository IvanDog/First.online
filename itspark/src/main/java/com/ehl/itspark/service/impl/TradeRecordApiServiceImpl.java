package com.ehl.itspark.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ehl.itspark.common.PageDTO;
import com.ehl.itspark.data.entity.TradeRecordEntity;
import com.ehl.itspark.data.service.intf.TradeRecordService;
import com.ehl.itspark.service.intf.TradeRecordApiService;


@Service
public class TradeRecordApiServiceImpl implements TradeRecordApiService{

	@Autowired
	private TradeRecordService tradeRecordService;
	
	@Override
    public int save(TradeRecordEntity entity)throws Exception{
    	return tradeRecordService.save(entity);
    }
	
	@Override
	public PageDTO<TradeRecordEntity> findTradeRecordsByPage(TradeRecordEntity entity,Date startTime,Date endTime,Integer pageIndex,Integer pageSize){
		return tradeRecordService.findTradeRecordsByPage(entity, startTime, endTime, pageIndex, pageSize);
	}
	
	@Override
	public int updateTradeRecord(TradeRecordEntity tradeRecord) throws Exception {
		// TODO Auto-generated method stub
		return tradeRecordService.updateTradeRecord(tradeRecord);
	}

	/*@Override
	public List<TradeRecordEntity> getAllTradeRecord(TradeRecordEntity entity, Date startTime, Date endTime) {
		// TODO Auto-generated method stub
		return tradeRecordService.getAllTradeRecord(entity, startTime, endTime);
	}*/
}
