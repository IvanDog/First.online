package com.ehl.itspark.data.service.intf;

import java.util.List;

import com.ehl.itspark.common.PageDTO;
import com.ehl.itspark.data.entity.SchedulingEntity;

public interface SchedulingService {

	int saveScheduling(SchedulingEntity entity)throws Exception;
	
	int updateScheduling(SchedulingEntity entity)throws Exception;
	
	List<SchedulingEntity> findList(SchedulingEntity entity);
	
	PageDTO<SchedulingEntity> findByPages(SchedulingEntity entity,Integer pageIndex,Integer pageSize);
}
