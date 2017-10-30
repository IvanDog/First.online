package com.ehl.itspark.service.intf;

import java.util.List;

import com.ehl.itspark.data.entity.ParkEnterEntity;


public interface ParkEnterApiService {
	
	List<ParkEnterEntity> findByParkNoAndPlate(String parkNo, String plate, String carType);
	
	List<ParkEnterEntity> findByParkNoAndFlowNo(String parkNo, String flowNo);
	
    List<ParkEnterEntity> findByParkNoAndParkLot(String parkNo,String parkLot);
    
    List<ParkEnterEntity> findByPlate(String plate);
	
	int deleteByParkNoAndFlowNo(String parkNo,String flowNo);
	
	int saveParkEnter(ParkEnterEntity parkEnter) throws Exception;
}
