package com.ehl.itspark.service.intf;

import java.util.Date;
import java.util.List;

import com.ehl.itspark.data.entity.ParkEntity;

public interface ParkApiService {

	List<ParkEntity> findParks(ParkEntity park,Date startUpdateTime,Date endUpdateTime,Integer pageIndex,Integer pageSize);
	ParkEntity findParkByNo(String no);
	boolean isInLocation(String parkNo,double lon,double lat, int addrRadius)throws Exception;
	int updatePark(ParkEntity park) throws Exception;
}
