package com.ehl.itspark.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ehl.itspark.data.entity.ParkEnterEntity;
import com.ehl.itspark.data.service.intf.ParkEnterService;
import com.ehl.itspark.service.intf.ParkEnterApiService;


@Service
public class ParkEnterApiServiceImpl implements ParkEnterApiService {

	@Autowired
	private ParkEnterService parkEnterService;

	@Override
	public int saveParkEnter(ParkEnterEntity parkEnterEntity) throws Exception {
		// TODO Auto-generated method stub
		return parkEnterService.save(parkEnterEntity);
	}

	@Override
	public List<ParkEnterEntity> findByParkNoAndPlate(String parkNo, String plate,String carType) {
		// TODO Auto-generated method stub
		return parkEnterService.findByParkNoAndPlate(parkNo, plate, carType);
	}
	
	@Override
	public List<ParkEnterEntity> findByParkNoAndFlowNo(String parkNo, String flowNo) {
		// TODO Auto-generated method stub
		return parkEnterService.findByParkNoAndFlowNo(parkNo, flowNo);
	}
	
	@Override
	public List<ParkEnterEntity> findByParkNoAndParkLot(String parkNo, String parkLot) {
		// TODO Auto-generated method stub
		return parkEnterService.findByParkNoAndParkLot(parkNo, parkLot);
	}
	
	@Override
	public List<ParkEnterEntity> findByPlate(String plate) {
		// TODO Auto-generated method stub
		return parkEnterService.findByPlate(plate);
	}
	
	@Override
	public int deleteByParkNoAndFlowNo(String parkNo,String flowNo) {
		// TODO Auto-generated method stub
		return parkEnterService.deleteByParkNoAndFlowNo(parkNo, flowNo);
	}

}
