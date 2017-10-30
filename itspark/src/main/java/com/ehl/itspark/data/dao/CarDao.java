package com.ehl.itspark.data.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.ehl.itspark.common.MyBatisRepository;
import com.ehl.itspark.data.entity.CarEntity;

@MyBatisRepository
public interface CarDao {

    int save(CarEntity entity)throws Exception;
	
	int updateByPlate(CarEntity entity)throws Exception;
	
	int deleteByPlate(@Param("plate")String plate);
	
	List<CarEntity> findAll(Map<String, Object> para);
	
	long count(Map<String, Object> para);
}
