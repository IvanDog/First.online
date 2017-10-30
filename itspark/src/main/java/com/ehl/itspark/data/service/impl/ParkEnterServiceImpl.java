package com.ehl.itspark.data.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ehl.itspark.data.dao.ParkEnterDao;
import com.ehl.itspark.data.entity.ParkEnterEntity;
import com.ehl.itspark.data.service.intf.ParkEnterService;

@Service
@Transactional
public class ParkEnterServiceImpl implements ParkEnterService{

	@Autowired
	private ParkEnterDao parkEnterDao;
	
	@Override
	public int save(ParkEnterEntity entity) throws Exception {
		// TODO Auto-generated method stub
		return parkEnterDao.save(entity);
	}


	@Override
	public List<ParkEnterEntity> findByPlateAndCarType(String plate, String carType) {
		// TODO Auto-generated method stub
		return parkEnterDao.findByPlateAndCarType(plate, carType);
	}

	@Override
	public List<ParkEnterEntity> findByParkNoAndPlate(String parkNo, String plate, String carType) {
		// TODO Auto-generated method stub
		return parkEnterDao.findByParkNoAndPlate(parkNo,plate,carType);
	}
	
	@Override
	public List<ParkEnterEntity> findByParkNoAndFlowNo(String parkNo, String flowNo) {
		// TODO Auto-generated method stub
		return parkEnterDao.findByParkNoAndFlowNo(parkNo,flowNo);
	}
	
	@Override
	public List<ParkEnterEntity> findByParkNoAndParkLot(String parkNo, String parkLot) {
		// TODO Auto-generated method stub
		return parkEnterDao.findByParkNoAndParkLot(parkNo, parkLot);
	}
	
	@Override
	public List<ParkEnterEntity> findByPlate(String plate) {
		// TODO Auto-generated method stub
		return parkEnterDao.findByPlate(plate);
	}
	
	@Override
	public int deleteByPlateAndCarType(String plate,String carType){
		// TODO Auto-generated method stub
		return parkEnterDao.deleteByPlateAndCarType(plate, carType);
	}
	
	@Override
	public int deleteByParkNoAndPlate(String parkNo,String plate){
		// TODO Auto-generated method stub
		return parkEnterDao.deleteByParkNoAndPlate(parkNo, plate);
	}
	
	@Override
	public int deleteByParkNoAndFlowNo(String parkNo,String flowNo){
		// TODO Auto-generated method stub
		return parkEnterDao.deleteByParkNoAndFlowNo(parkNo, flowNo);
	}
	
	@Override
	public int deleteByParkNoAndParkLot(String parkNo,String parkLot){
		// TODO Auto-generated method stub
		return parkEnterDao.deleteByParkNoAndParkLot(parkNo, parkLot);
	}

}
