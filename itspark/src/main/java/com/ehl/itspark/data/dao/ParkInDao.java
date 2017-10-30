package com.ehl.itspark.data.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.ehl.itspark.common.MyBatisRepository;
import com.ehl.itspark.data.entity.ParkEntity;

@MyBatisRepository
public interface ParkInDao {

	List<ParkEntity> findAll(Map<String, Object> para);
	
	int save(ParkEntity entity);
	
	int update(ParkEntity entity);
	
	long count(Map<String, Object> para);
	
	int delete(@Param("no")String no);
}
