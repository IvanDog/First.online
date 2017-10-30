package com.ehl.itspark.data.service.intf;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.ehl.itspark.common.PageDTO;
import com.ehl.itspark.data.entity.CarEntity;

public interface CarService {

	PageDTO<CarEntity> findCarsByPage(CarEntity entity,Integer pageIndex,Integer pageSize);
	
	List<CarEntity> findCarsByOwnerNo(String ownerNo);
	
	List<CarEntity> findCarsByPlateAndType(String plate,String carType);
	
	int save(CarEntity entity)throws Exception;
	
	int updateByPlate(CarEntity entity)throws Exception;
	
	int deleteByPlate(@Param("plate")String plate);
}
