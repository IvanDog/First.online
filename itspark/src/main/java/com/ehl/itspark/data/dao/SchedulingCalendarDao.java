package com.ehl.itspark.data.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.ehl.itspark.common.MyBatisRepository;
import com.ehl.itspark.data.entity.SchedulingCalendarEntity;
import com.ehl.itspark.data.entity.ShiftworkSchedulingCalendarEntity;

@MyBatisRepository
public interface SchedulingCalendarDao {

	List<SchedulingCalendarEntity> findCalendarByPersionView(Map<String, Object> para);
	
	long countCalendarByPersionView(Map<String, Object> para);
	
	ShiftworkSchedulingCalendarEntity findCalendarByShiftworkView(@Param("shiftworkId") Long shiftworkId);
	
}
