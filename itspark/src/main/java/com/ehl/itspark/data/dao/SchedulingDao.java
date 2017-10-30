package com.ehl.itspark.data.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.ehl.itspark.common.MyBatisRepository;
import com.ehl.itspark.data.entity.SchedulingEntity;


@MyBatisRepository
public interface SchedulingDao {

	int save(SchedulingEntity entity)throws Exception;
	
	int update(SchedulingEntity entity)throws Exception;
	
	int deleteById(@Param("id")Long id)throws Exception;
	
	List<SchedulingEntity> findList(Map<String, Object> para);
	
	long count(Map<String, Object> para);
	
}
