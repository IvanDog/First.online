package com.ehl.itspark.data.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ehl.itspark.common.PageDTO;
import com.ehl.itspark.data.dao.ParkRecordDao;
import com.ehl.itspark.data.entity.ParkRecordEntity;
import com.ehl.itspark.data.service.intf.ParkRecordService;

@Service
@Transactional
public class ParkRecordServiceImpl implements ParkRecordService{

	@Autowired
	private ParkRecordDao parkRecordDao;
	
	@Override
	public int save(ParkRecordEntity entity) throws Exception {
		// TODO Auto-generated method stub
		return parkRecordDao.save(entity);
	}

	@Override
	public PageDTO<ParkRecordEntity> findParkRecordsByPage(ParkRecordEntity entity, Date startTime, Date endTime,
			Integer pageIndex, Integer pageSize) {
		// TODO Auto-generated method stub
		PageDTO<ParkRecordEntity> result=new PageDTO<>(pageIndex, pageSize);
		Map<String, Object> para=new HashMap<>();
		if(entity!=null){
			if(entity.getPlate()!=null&&!"".equals(entity.getPlate())){
				para.put("plate", entity.getPlate());
			}
			if(entity.getCarType()!=null&&!"".equals(entity.getCarType())){
				para.put("carType", entity.getCarType());
			}
			if(entity.getParkNo()!=null&&!"".equals(entity.getParkNo())){
				para.put("parkNo", entity.getParkNo());
			}
			if(entity.getParkLot()!=null&&!"".equals(entity.getParkLot())){
				para.put("parkLot", entity.getParkLot());
			}
			if(entity.getPaymentFlag()!=null){
				para.put("paymentFlag", entity.getPaymentFlag());
			}
		}
		if(startTime!=null){
			para.put("startTime", startTime);
		}
		if(endTime!=null){
			para.put("endTime", endTime);
		}
		List<ParkRecordEntity> areaEntities= parkRecordDao.findAll(para);
		long count=parkRecordDao.count(para);
		result.setData(areaEntities);
		result.setRowSize(count);
		return result;
	}

	@Override
	public List<ParkRecordEntity> findParkRecord(ParkRecordEntity entity,Date startTime, Date endTime) {
		// TODO Auto-generated method stub
		Map<String, Object> para=new HashMap<>();
		if(entity!=null){
			if(entity.getPlate()!=null&&!"".equals(entity.getPlate())){
				para.put("plate", entity.getPlate());
			}
			if(entity.getCarType()!=null&&!"".equals(entity.getCarType())){
				para.put("carType", entity.getCarType());
			}
			if(entity.getParkNo()!=null&&!"".equals(entity.getParkNo())){
				para.put("parkNo", entity.getParkNo());
			}
			if(entity.getParkLot()!=null&&!"".equals(entity.getParkLot())){
				para.put("parkLot", entity.getParkLot());
			}
			if(entity.getPaymentFlag()!=null){
				para.put("paymentFlag", entity.getPaymentFlag());
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
		List<ParkRecordEntity> areaEntities= parkRecordDao.findAll(para);
		return areaEntities;
	}

	@Override
	public int updateParkRecord(ParkRecordEntity parkRecord) throws Exception{
		// TODO Auto-generated method stub
		if(parkRecord.getPaymentFlag()!=null){
			return parkRecordDao.updatePayment(parkRecord);
		}else if(!"".equals(parkRecord.getTradeFlowNo()) && parkRecord.getTradeFlowNo()!=null){
			return parkRecordDao.updateTradeFlow(parkRecord);
		}else{
			return -1;
		}
	}

}
