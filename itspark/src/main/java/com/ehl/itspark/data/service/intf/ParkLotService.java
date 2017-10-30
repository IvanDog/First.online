package com.ehl.itspark.data.service.intf;

import java.util.Date;
import java.util.List;

import com.ehl.itspark.common.PageDTO;
import com.ehl.itspark.data.entity.ParkLotEntity;

public interface ParkLotService {

	PageDTO<ParkLotEntity> findParkLotByPage(ParkLotEntity parkLot,Date startUpdateTime,Date endUpdateTime,Integer pageIndex,Integer pageSize);
	
	List<ParkLotEntity> findParkLots(ParkLotEntity parkLot,Integer pageIndex,Integer pageSize);
	
	List<ParkLotEntity> findParkLots(ParkLotEntity parkLot,Date startUpdateTime,Date endUpdateTime,Integer pageIndex,Integer pageSize);
	
	int saveParkLot(ParkLotEntity parkLot)throws Exception;
	
	int updateParkLot(ParkLotEntity parkLot)throws Exception;
	
}
