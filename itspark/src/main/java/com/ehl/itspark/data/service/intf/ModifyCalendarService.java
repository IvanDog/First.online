package com.ehl.itspark.data.service.intf;

import java.util.Date;
import java.util.List;

import com.ehl.itspark.data.entity.ModifyCalendarEntity;


public interface ModifyCalendarService {

	int saveModifyCalendarRecord(ModifyCalendarEntity entity) throws Exception;
	
	int saveModifyCalendarRecords(List<ModifyCalendarEntity> entities) throws Exception;
	
	int updateModifyCalendarRecord(ModifyCalendarEntity entity)throws Exception;
	
	int updateModifyCalendarRecords(List<ModifyCalendarEntity> entities)throws Exception;
	
	List<ModifyCalendarEntity> findModifyCalendarsByPersionId(Long persionId);
	
	List<ModifyCalendarEntity> findModifyCalendarsByPersionIdAndModifyTime(Long persionId,Date modifyTime);
	
	int deleteByPersionIdAndModifyTime(Long persionId,Date modifyTime)throws Exception;
}
