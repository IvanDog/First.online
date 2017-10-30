package com.ehl.itspark.data.service.intf;

import java.util.List;

import com.ehl.itspark.data.entity.ParkEnterEntity;

public interface ParkEnterService {

	int save(ParkEnterEntity entity)throws Exception;
	
	List<ParkEnterEntity> findByPlateAndCarType(String plate,String carType);
	
    List<ParkEnterEntity> findByParkNoAndPlate(String parkNo,String plate,String carType);
    
    List<ParkEnterEntity> findByParkNoAndFlowNo(String parkNo,String flowNo);
    
    List<ParkEnterEntity> findByParkNoAndParkLot(String parkNo,String parkLot);
    
    List<ParkEnterEntity> findByPlate(String plate);
    
	int deleteByPlateAndCarType(String plate,String carType);
	
	int deleteByParkNoAndPlate(String parkNo,String plate);
	
	int deleteByParkNoAndFlowNo(String parkNo,String flowNo);
	
	int deleteByParkNoAndParkLot(String parkNo,String parkLot);
}
