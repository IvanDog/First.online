package com.ehl.itspark.data.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ehl.itspark.common.PageDTO;
import com.ehl.itspark.data.dao.TradeRecordDao;
import com.ehl.itspark.data.entity.TradeRecordEntity;
import com.ehl.itspark.data.service.intf.TradeRecordService;

@Service
@Transactional
public class TradeRecordServiceImpl implements TradeRecordService{

	private Logger logger=LoggerFactory.getLogger(TradeRecordServiceImpl.class);
	@Autowired
	private TradeRecordDao tradeRecordDao;
	
	@Override
	public int save(TradeRecordEntity entity) throws Exception {
		// TODO Auto-generated method stub
		return tradeRecordDao.save(entity);
	}

	@Override
	public PageDTO<TradeRecordEntity> findTradeRecordsByPage(TradeRecordEntity entity,Date startTime,Date endTime, Integer pageIndex,
			Integer pageSize) {
		// TODO Auto-generated method stub
		PageDTO<TradeRecordEntity> result=new PageDTO<>(pageIndex, pageSize);
		Map<String, Object> para=new HashMap<>();
		if(entity!=null){
			if(entity.getIndustryFlag()!=null&&!"".equals(entity.getIndustryFlag())){
				para.put("industryFlag", entity.getIndustryFlag());
			}
			if(entity.getTradeFlag()!=null&&!"".equals(entity.getTradeFlag())){
				para.put("tradeFlag", entity.getTradeFlag());
			}
			if(entity.getResult()!=null){
				para.put("result", entity.getResult());
			}
			if(entity.getPayMode()!=null){
				para.put("payMode", entity.getPayMode());
			}
			if(entity.getFlowNo()!=null && !"".equals(entity.getFlowNo())){
				para.put("flowNo", entity.getFlowNo());
			}
		}
		if(startTime!=null){
			para.put("startTime", startTime);
		}
		if(endTime!=null){
			para.put("endTime", endTime);
		}
		List<TradeRecordEntity> areaEntities= tradeRecordDao.findAll(para);
		long count=tradeRecordDao.count(para);
		result.setData(areaEntities);
		result.setRowSize(count);
		return result;
	}

	@Override
	public int updateTradeRecord(TradeRecordEntity tradeRecord){
		int flag = -1;
        if(tradeRecord.getPayMoney()!=null && tradeRecord.getPaidMoney()!=null){//couponID可以为空
        	flag = tradeRecordDao.updateExpense(tradeRecord); 
        }
        if(tradeRecord.getPayMode()!=null && tradeRecord.getResult()!=null){//paymentTime可以为空
        	flag =  tradeRecordDao.updateResult(tradeRecord);
        }
        return flag;
	}

	@Override
	public void saveTradeRecord(TradeRecordEntity entity) {
		// TODO Auto-generated method stub
		try {
			tradeRecordDao.save(entity);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("保存交易记录失败",e);
			/*throw new ReconciliationHandleException("保存交易记录失败",e);*/
		}
	}

/*	@Override
	public List<TradeRecordEntity> getSuccessTradeRecord(String thirdType, Date billDate) {
		// TODO Auto-generated method stub
		List<TradeRecordEntity> result=new ArrayList<>();
		Map<String, Object> para=new HashMap<>();
		para.put("result", TradeStatusEnum.SUCCESS.getVal());
		para.put("tradeType", 1);
		if(billDate!=null){
			para.put("startTime", DateUtils.StringToDate(DateUtils.DateToString(billDate, "yyyy-MM-dd")+" 00:00:00", "yyyy-MM-dd HH:mm:ss"));
			para.put("endTime", DateUtils.StringToDate(DateUtils.DateToString(billDate, "yyyy-MM-dd")+" 23:59:59", "yyyy-MM-dd HH:mm:ss"));
		}
		if(thirdType!=null&&!"".equals(thirdType)){
			if (thirdType.equals("2")) {
				para.put("payMode", 2);
				List<TradeRecordEntity> entities2= tradeRecordDao.findAll(para);
				if(entities2!=null){
					result.addAll(entities2);
				}
				para.put("payMode", 4);
				List<TradeRecordEntity> entities4= tradeRecordDao.findAll(para);
				if(entities4!=null){
					result.addAll(entities4);
				}
				para.put("payMode", 6);
				List<TradeRecordEntity> entities6= tradeRecordDao.findAll(para);
				if(entities6!=null){
					result.addAll(entities6);
				}
			}else if (thirdType.equals("1")) {
				para.put("payMode", 3);
				List<TradeRecordEntity> entities3= tradeRecordDao.findAll(para);
				if(entities3!=null){
					result.addAll(entities3);
				}
				para.put("payMode", 5);
				List<TradeRecordEntity> entities5= tradeRecordDao.findAll(para);
				if(entities5!=null){
					result.addAll(entities5);
				}
				para.put("payMode", 7);
				List<TradeRecordEntity> entities7= tradeRecordDao.findAll(para);
				if(entities7!=null){
					result.addAll(entities7);
				}
			}else {
				para.put("payMode", Integer.valueOf(thirdType));
				List<TradeRecordEntity> entities= tradeRecordDao.findAll(para);
				if(entities!=null){
					result.addAll(entities);
				}
			}
		}
		return result;
	}

	@Override
	public List<TradeRecordEntity> getAllTradeRecord(String thirdType, Date billDate) {
		// TODO Auto-generated method stub
		List<TradeRecordEntity> result=new ArrayList<>();
		Map<String, Object> para=new HashMap<>();
		para.put("tradeType", 1);
		if(billDate!=null){
			para.put("startTime", DateUtils.StringToDate(DateUtils.DateToString(billDate, "yyyy-MM-dd")+" 00:00:00", "yyyy-MM-dd HH:mm:ss"));
			para.put("endTime", DateUtils.StringToDate(DateUtils.DateToString(billDate, "yyyy-MM-dd")+" 23:59:59", "yyyy-MM-dd HH:mm:ss"));
		}
		if(thirdType!=null&&!"".equals(thirdType)){
			if (thirdType.equals("2")) {
				para.put("payMode", 2);
				List<TradeRecordEntity> entities2= tradeRecordDao.findAll(para);
				if(entities2!=null){
					result.addAll(entities2);
				}
				para.put("payMode", 4);
				List<TradeRecordEntity> entities4= tradeRecordDao.findAll(para);
				if(entities4!=null){
					result.addAll(entities4);
				}
				para.put("payMode", 6);
				List<TradeRecordEntity> entities6= tradeRecordDao.findAll(para);
				if(entities6!=null){
					result.addAll(entities6);
				}
			}else if (thirdType.equals("1")) {
				para.put("payMode", 3);
				List<TradeRecordEntity> entities3= tradeRecordDao.findAll(para);
				if(entities3!=null){
					result.addAll(entities3);
				}
				para.put("payMode", 5);
				List<TradeRecordEntity> entities5= tradeRecordDao.findAll(para);
				if(entities5!=null){
					result.addAll(entities5);
				}
				para.put("payMode", 7);
				List<TradeRecordEntity> entities7= tradeRecordDao.findAll(para);
				if(entities7!=null){
					result.addAll(entities7);
				}
			}else {
				para.put("payMode", Integer.valueOf(thirdType));
				List<TradeRecordEntity> entities= tradeRecordDao.findAll(para);
				if(entities!=null){
					result.addAll(entities);
				}
			}
		}
		return result;
	}*/

	@Override
	public TradeRecordEntity getTradeRecordByOrder(String orderNo) {
		// TODO Auto-generated method stub
		Map<String, Object> para=new HashMap<>();
		para.put("tradeType", 1);
		para.put("flowNo", orderNo);
		List<TradeRecordEntity> entities= tradeRecordDao.findAll(para);
		if(entities==null||entities.size()==0){
			return null;
		}
		return entities.get(0);
	}

	/*@Override
	public List<TradeRecordEntity> getAllTradeRecord(TradeRecordEntity entity, Date startTime, Date endTime) {
		// TODO Auto-generated method stub
		Map<String, Object> para=new HashMap<>();
		para.put("tradeType", 1);
		if(entity!=null){
			if(entity.getServiceEntityFlow()!=null&&!"".equals(entity.getServiceEntityFlow())){
				para.put("serviceEntityFlow", entity.getServiceEntityFlow());
			}
			if(entity.getFlowNo()!=null&&!"".equals(entity.getFlowNo())){
				para.put("flowNo", entity.getFlowNo());
			}
			if(entity.getBillID()!=null&&!"".equals(entity.getBillID())){
				para.put("billNo", entity.getBillID());
			}
			if(entity.getResult()!=null){
				para.put("result", entity.getResult());
			}
		}
		
		if(startTime!=null){
			para.put("startTime", startTime);
			
		}
		if(endTime!=null){
			para.put("endTime", endTime);
		}
		return tradeRecordDao.findAll(para);
	}*/

	@Override
	public int updateRecord(TradeRecordEntity tradeRecord) {
		// TODO Auto-generated method stub
		return tradeRecordDao.updateRecord(tradeRecord);
	}

}
