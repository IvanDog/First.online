package com.ehl.itspark.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ehl.itspark.common.PageDTO;
import com.ehl.itspark.data.entity.ParkRecordEntity;
import com.ehl.itspark.data.service.intf.ParkRecordService;
import com.ehl.itspark.service.intf.ParkRecordApiService;


@Service
public class ParkRecordApiServiceImpl implements ParkRecordApiService {

	@Autowired
	private ParkRecordService parkRecordService;

	@Override
	public PageDTO<ParkRecordEntity> findByPage(ParkRecordEntity parkRecord, Integer pageIndex, Integer pageSize) {
		// TODO Auto-generated method stub
		return parkRecordService.findParkRecordsByPage(parkRecord, null, null, pageIndex, pageSize);
	}

	@Override
	public int saveParkRecord(ParkRecordEntity parkRecord) throws Exception {
		// TODO Auto-generated method stub
		return parkRecordService.save(parkRecord);
	}

	@Override
	public List<ParkRecordEntity> findByParkNoAndPlate(String parkNo, String plate, String carType, Integer paymentFlag, Date startTime, Date endTime) {
		// TODO Auto-generated method stub
		ParkRecordEntity entity = new ParkRecordEntity();
		entity.setParkNo(parkNo);
		entity.setPlate(plate);
		entity.setCarType(carType);
		entity.setPaymentFlag(paymentFlag);
		return parkRecordService.findParkRecord(entity, startTime, endTime);
	}

	@Override
	public List<ParkRecordEntity> findByParkNoAndFlowNo(String parkNo, String flowNo) {
		// TODO Auto-generated method stub
		ParkRecordEntity entity = new ParkRecordEntity();
		entity.setParkNo(parkNo);
		entity.setFlowNo(flowNo);
		return parkRecordService.findParkRecord(entity,null,null);
	}
	
	@Override
	public List<ParkRecordEntity> findByParkNoAndPaymentPattern(String parkNo, Integer paymentFlag, Date startTime, Date endTime) {
		// TODO Auto-generated method stub
		ParkRecordEntity entity = new ParkRecordEntity();
		entity.setParkNo(parkNo);
		entity.setPaymentFlag(paymentFlag);
		return parkRecordService.findParkRecord(entity, startTime, endTime);
	}
	
	@Override
	public List<ParkRecordEntity> findByPlate(String plate, String carType, Date startTime, Date endTime) {
		// TODO Auto-generated method stub
		ParkRecordEntity entity = new ParkRecordEntity();
		entity.setPlate(plate);
		entity.setCarType(carType);
		return parkRecordService.findParkRecord(entity, startTime, endTime);
	}
	
	@Override
	public List<ParkRecordEntity> findByPlateAndPaymentPattern(String plate, String carType, Integer paymentFlag, Date startTime, Date endTime) {
		// TODO Auto-generated method stub
		ParkRecordEntity entity = new ParkRecordEntity();
		entity.setPlate(plate);
		entity.setCarType(carType);
		entity.setPaymentFlag(paymentFlag);
		return parkRecordService.findParkRecord(entity, startTime, endTime);
	}
	
	@Override
	public List<ParkRecordEntity> findByParkNoAndParkLot(String parkNo, String parkLot, Date startTime, Date endTime) {
		// TODO Auto-generated method stub
		ParkRecordEntity entity = new ParkRecordEntity();
		entity.setParkNo(parkNo);
		entity.setParkLot(parkLot);
		return parkRecordService.findParkRecord(entity, startTime, endTime);
	}
	
	@Override
	public int updateParkRecord(ParkRecordEntity parkRecord) throws Exception {
		// TODO Auto-generated method stub
		return parkRecordService.updateParkRecord(parkRecord);
	}

	@Override
	public PageDTO<ParkRecordEntity> findByPage(ParkRecordEntity parkRecord, Date startTime, Date endTime,
			Integer pageIndex, Integer pageSize) {
		// TODO Auto-generated method stub
		return parkRecordService.findParkRecordsByPage(parkRecord, startTime, endTime, pageIndex, pageSize);
	}
}
