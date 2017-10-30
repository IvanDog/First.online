package com.ehl.itspark.service.intf;

import java.util.Date;
import java.util.List;

import com.ehl.itspark.common.PageDTO;
import com.ehl.itspark.data.entity.ParkRecordEntity;


public interface ParkRecordApiService {

	PageDTO<ParkRecordEntity> findByPage(ParkRecordEntity parkRecord, Integer pageIndex, Integer pageSize);
	
	PageDTO<ParkRecordEntity> findByPage(ParkRecordEntity parkRecord, Date startTime, Date endTime, Integer pageIndex, Integer pageSize);
	
	List<ParkRecordEntity> findByParkNoAndPlate(String parkNo, String plate, String carType, Integer paymentFlag, Date startTime, Date endTime);//参量中增加了paymentFlag
	
	List<ParkRecordEntity> findByParkNoAndFlowNo(String parkNo, String flowNo);//新增接口，基于流水号的查询
	
	List<ParkRecordEntity> findByParkNoAndPaymentPattern(String parkNo, Integer paymentFlag, Date startTime, Date endTime);//新增接口，基于车场、支付类别和时间戳的查询
	
	List<ParkRecordEntity> findByPlate(String plate, String carType, Date startTime, Date endTime);//新增接口，基于牌照、车辆类别、时间戳的查询
	
	List<ParkRecordEntity> findByPlateAndPaymentPattern(String plate, String carType, Integer paymentFlag, Date startTime, Date endTime);//新增接口，基于牌照、支付类别和时间戳的查询
	
	List<ParkRecordEntity> findByParkNoAndParkLot(String parkNo, String  parkLot, Date startTime, Date endTime);//新增接口，基于车位和时间戳的查询
	
	int saveParkRecord(ParkRecordEntity parkRecord) throws Exception;
	
	int updateParkRecord(ParkRecordEntity parkRecord)throws Exception;
}
