package com.ehl.itspark.data.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.ehl.itspark.common.MyBatisRepository;
import com.ehl.itspark.data.entity.ModifyCalendarEntity;


@MyBatisRepository
public interface ModifyCalendarDao {

	int save(ModifyCalendarEntity entity)throws Exception;
	
	int update(ModifyCalendarEntity entity)throws Exception;
	
	List<ModifyCalendarEntity> findModifyCalendars(Map<String, Object> para);
	
	int delete(@Param("persionId")Long persionId,@Param("modifyTime")Date modifyTime)throws Exception;
}
