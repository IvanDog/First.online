package com.ehl.itspark.data.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.ehl.itspark.common.MyBatisRepository;
import com.ehl.itspark.data.entity.PersonEntity;

@MyBatisRepository
public interface PersonDao {

	List<PersonEntity> findAll(Map<String, Object> para);
	
	int save(PersonEntity entity);
	
	int update(PersonEntity entity);
	
	long count(Map<String, Object> para);
	
	int delete(@Param("no")String no);
}
