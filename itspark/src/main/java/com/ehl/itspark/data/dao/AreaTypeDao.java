package com.ehl.itspark.data.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.ehl.itspark.common.MyBatisRepository;
import com.ehl.itspark.data.entity.AreaTypeEntity;

@MyBatisRepository
public interface AreaTypeDao {

	List<AreaTypeEntity> findAll(Map<String, Object> para);
	
	int save(AreaTypeEntity record) throws Exception;
	
	int update(AreaTypeEntity record)throws Exception;
	
	long count(Map<String, Object> para);
	
	int delete(@Param("no")String no);
}
