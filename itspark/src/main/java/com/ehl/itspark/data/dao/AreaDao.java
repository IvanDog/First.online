package com.ehl.itspark.data.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.ehl.itspark.common.MyBatisRepository;
import com.ehl.itspark.data.entity.AreaEntity;

@MyBatisRepository
public interface AreaDao {

	List<AreaEntity> findAll(Map<String, Object> para);
	
	int save(AreaEntity record) throws Exception;
	
	int update(AreaEntity record)throws Exception;
	
	int delete(@Param("no")String no);
	
	long count(Map<String, Object> para);
}
