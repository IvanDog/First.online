package com.ehl.itspark.service.intf;

import java.util.List;

import com.ehl.itspark.data.entity.CarEntity;


public interface CarApiService {

	List<CarEntity> findCarsByPlateAndType(String plate,String carType);
	
	List<CarEntity> findCarsByOwnerNo(String ownerNo);
	
	int save(CarEntity entity)throws Exception;
	
	int updateByPlate(CarEntity entity)throws Exception;
}
