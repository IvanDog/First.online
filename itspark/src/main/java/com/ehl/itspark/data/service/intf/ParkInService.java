package com.ehl.itspark.data.service.intf;

import java.util.Date;
import java.util.List;

import com.ehl.itspark.common.PageDTO;
import com.ehl.itspark.data.entity.ParkEntity;

public interface ParkInService {

	PageDTO<ParkEntity> findParkByPage(ParkEntity park,Date startUpdateTime,Date endUpdateTime,Integer pageIndex,Integer pageSize);
	
	List<ParkEntity> findParks(ParkEntity park,Integer pageIndex,Integer pageSize);
	
	List<ParkEntity> findParks(ParkEntity park,Date startUpdateTime,Date endUpdateTime,Integer pageIndex,Integer pageSize);
	
	int savePark(ParkEntity park) throws Exception;
	
	int updatePark(ParkEntity park) throws Exception;
	
}
