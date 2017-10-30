package com.ehl.itspark.data.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.ehl.itspark.common.MyBatisRepository;
import com.ehl.itspark.data.entity.ParkLotEntity;

@MyBatisRepository
public interface ParkLotDao {

	List<ParkLotEntity> findAll(Map<String, Object> para);
	
	int save(ParkLotEntity entity);
	
	int update(ParkLotEntity entity);
	
	long count(Map<String, Object> para);
	
	int delete(@Param("no")String no);
}
