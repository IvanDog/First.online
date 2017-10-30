package com.ehl.itspark.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ehl.itspark.common.PageDTO;
import com.ehl.itspark.data.entity.ParkLotEntity;
import com.ehl.itspark.data.service.intf.ParkLotService;
import com.ehl.itspark.service.intf.ParkLotApiService;

@Service
public class ParkLotApiServiceImpl implements ParkLotApiService{

	@Autowired
	private ParkLotService parkLotService;
	
	@Override
	public PageDTO<ParkLotEntity> findParkLotByPage(ParkLotEntity parkLot, Date startUpdateTime, Date endUpdateTime,
			Integer pageIndex, Integer pageSize) {
		// TODO Auto-generated method stub
		return parkLotService.findParkLotByPage(parkLot, startUpdateTime, endUpdateTime, pageIndex, pageSize);
	}

	@Override
	public int saveParkLot(ParkLotEntity parkLot) throws Exception {
		// TODO Auto-generated method stub
		return parkLotService.saveParkLot(parkLot);
	}

	@Override
	public int updateParkLot(ParkLotEntity parkLot) throws Exception {
		// TODO Auto-generated method stub
		return parkLotService.updateParkLot(parkLot);
	}

}
