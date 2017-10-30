package com.ehl.itspark.data.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.ehl.itspark.common.MyBatisRepository;
import com.ehl.itspark.data.entity.ShiftworkEntity;


@MyBatisRepository
public interface ShiftworkDao {

	int save(ShiftworkEntity entity)throws Exception;
	
	int update(ShiftworkEntity entity)throws Exception;
	
	int deleteById(@Param("id")Long id)throws Exception;
	
	List<ShiftworkEntity> findList(Map<String, Object> para);
	
	long count(Map<String, Object> para);
	
	ShiftworkEntity findById(@Param("id")Long id);
}
