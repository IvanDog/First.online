package com.ehl.itspark.data.service.intf;

import java.util.List;

import com.ehl.itspark.common.PageDTO;
import com.ehl.itspark.data.entity.SchedulingCalendarEntity;
import com.ehl.itspark.data.entity.ShiftworkSchedulingCalendarEntity;

public interface SchedulingCalendarService {

	List<SchedulingCalendarEntity> findCalendarByPersionView(String persionName);
	
	PageDTO<SchedulingCalendarEntity> findCalendarPagesByPersionView(String persionName,Integer pageIndex,Integer pageSize);
	
	PageDTO<ShiftworkSchedulingCalendarEntity> findCalendarPagesByShiftworkView(String shiftworkName,Integer pageIndex,Integer pageSize);
}
