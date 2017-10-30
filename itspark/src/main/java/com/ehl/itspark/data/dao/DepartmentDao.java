package com.ehl.itspark.data.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.ehl.itspark.common.MyBatisRepository;
import com.ehl.itspark.data.entity.DepartmentEntity;

@MyBatisRepository
public interface DepartmentDao {

	List<DepartmentEntity> findAll(Map<String, Object> para);
	
	int save(DepartmentEntity record) throws Exception;
	
	int update(DepartmentEntity record)throws Exception;
	
	long count(Map<String, Object> para);
	
	int delete(@Param("no")String no);
}
