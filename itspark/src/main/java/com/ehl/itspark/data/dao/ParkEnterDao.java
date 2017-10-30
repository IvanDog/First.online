package com.ehl.itspark.data.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.ehl.itspark.common.MyBatisRepository;
import com.ehl.itspark.data.entity.ParkEnterEntity;

@MyBatisRepository
public interface ParkEnterDao {

	int save(ParkEnterEntity entity)throws Exception;

	List<ParkEnterEntity> findByPlateAndCarType(@Param("plate") String plate,@Param("carType")String plateType);
	
	List<ParkEnterEntity> findByParkNoAndPlate(@Param("parkNo") String parkNo,@Param("plate")String plate,@Param("carType")String carType);
	
	List<ParkEnterEntity> findByParkNoAndFlowNo(@Param("parkNo") String parkNo,@Param("flowNo")String flowNo);
	
	List<ParkEnterEntity> findByParkNoAndParkLot(@Param("parkNo") String parkNo,@Param("parkLot")String parkLot);
	
	List<ParkEnterEntity> findByPlate(@Param("plate") String plate);
	
	int deleteByPlateAndCarType(@Param("plate") String plate,@Param("plateType")String carType);
	
	int deleteByParkNoAndPlate(@Param("parkNo") String parkNo,@Param("plate")String plate);
	
	int deleteByParkNoAndFlowNo(@Param("parkNo") String parkNo,@Param("flowNo")String flowNo);
	
	int deleteByParkNoAndParkLot(@Param("parkNo") String parkNo,@Param("parkLot")String parkLot);
}
