package com.ehl.itspark.data.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.ehl.itspark.common.MyBatisRepository;
import com.ehl.itspark.data.entity.PersonAccessEntity;
@MyBatisRepository
public interface PersonAccessDao {

	List<PersonAccessEntity> findAll(Map<String, Object> para);
	
	int save(PersonAccessEntity record) throws Exception;
	
	int updateName(PersonAccessEntity entity)throws Exception;
	
	int updatePword(PersonAccessEntity entity)throws Exception;
	
	int delete(@Param("no")String no);
	
	long count(Map<String, Object> para);
}
