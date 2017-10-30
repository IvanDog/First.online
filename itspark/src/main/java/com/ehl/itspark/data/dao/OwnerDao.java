package com.ehl.itspark.data.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.ehl.itspark.common.MyBatisRepository;
import com.ehl.itspark.data.entity.OwnerEntity;

@MyBatisRepository
public interface OwnerDao {

	int save(OwnerEntity entity)throws Exception;
	
	int updateByNo(OwnerEntity entity)throws Exception;
	
	int deleteByNo(@Param("no")String no);
	
	List<OwnerEntity> findAll(Map<String, Object> para);
	
	long count(Map<String, Object> para);
}
