package com.ehl.itspark.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ehl.itspark.data.entity.CarEntity;
import com.ehl.itspark.data.service.intf.CarService;
import com.ehl.itspark.service.intf.CarApiService;


@Service
public class CarApiServiceImpl implements CarApiService{

	@Autowired
	private CarService carService;
	
	@Override
	public List<CarEntity> findCarsByPlateAndType(String plate,String carType) {
		// TODO Auto-generated method stub
		return carService.findCarsByPlateAndType(plate,carType);
	}

	@Override
	public List<CarEntity> findCarsByOwnerNo(String ownerNo) {
		// TODO Auto-generated method stub
		return carService.findCarsByOwnerNo(ownerNo);
	}
	
	@Override
	public 	int save(CarEntity entity)throws Exception{
		// TODO Auto-generated method stub
		return carService.save(entity);
	}
	
	@Override
	public 	int updateByPlate(CarEntity entity)throws Exception{
		// TODO Auto-generated method stub
		return carService.updateByPlate(entity);
	}
}
