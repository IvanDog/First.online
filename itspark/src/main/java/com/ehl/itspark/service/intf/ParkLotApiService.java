package com.ehl.itspark.service.intf;

import java.util.Date;

import com.ehl.itspark.common.PageDTO;
import com.ehl.itspark.data.entity.ParkLotEntity;

public interface ParkLotApiService {

	PageDTO<ParkLotEntity> findParkLotByPage(ParkLotEntity parkLot,Date startUpdateTime,Date endUpdateTime,Integer pageIndex,Integer pageSize);

	int saveParkLot(ParkLotEntity parkLot)throws Exception;
	
	int updateParkLot(ParkLotEntity parkLot)throws Exception;
}
