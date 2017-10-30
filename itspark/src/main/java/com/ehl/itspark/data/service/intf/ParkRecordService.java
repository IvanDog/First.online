package com.ehl.itspark.data.service.intf;

import java.util.Date;
import java.util.List;

import com.ehl.itspark.common.PageDTO;
import com.ehl.itspark.data.entity.ParkRecordEntity;

public interface ParkRecordService {

	int save(ParkRecordEntity entity)throws Exception;
	
	PageDTO<ParkRecordEntity> findParkRecordsByPage(ParkRecordEntity entity,Date startTime,Date endTime,Integer pageIndex,Integer pageSize);
	
	List<ParkRecordEntity> findParkRecord(ParkRecordEntity parkRecordEntity,Date startTime, Date endTime);
	
	int updateParkRecord(ParkRecordEntity parkRecord)throws Exception;
}
