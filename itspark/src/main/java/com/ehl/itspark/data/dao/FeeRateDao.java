package com.ehl.itspark.data.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.ehl.itspark.common.MyBatisRepository;
import com.ehl.itspark.data.entity.FeeRateEntity;
@MyBatisRepository
public interface FeeRateDao {

	int save(FeeRateEntity entity)throws Exception;
	
	int update(FeeRateEntity entity)throws Exception;
	
	int delete(@Param("id")Long id)throws Exception;
	
	List<FeeRateEntity> findAll(Map<String, Object> para);
	
	long count(Map<String, Object> para);
}
