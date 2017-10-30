package com.ehl.itspark.data.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ehl.itspark.common.PageDTO;
import com.ehl.itspark.data.dao.SchedulingCalendarDao;
import com.ehl.itspark.data.entity.SchedulingCalendarEntity;
import com.ehl.itspark.data.entity.ShiftworkEntity;
import com.ehl.itspark.data.entity.ShiftworkSchedulingCalendarEntity;
import com.ehl.itspark.data.service.intf.SchedulingCalendarService;
import com.ehl.itspark.data.service.intf.ShiftworkService;

@Service
public class SchedulingCalendarServiceImpl implements SchedulingCalendarService{

	@Autowired
	private SchedulingCalendarDao calendarDao;
	@Autowired
	private ShiftworkService shiftworkService;
	
	@Override
	public List<SchedulingCalendarEntity> findCalendarByPersionView(String persionName) {
		// TODO Auto-generated method stub
		Map<String, Object> para=new HashMap<>();
		if(persionName!=null&&!"".equals(persionName)){
			para.put("persionName", persionName);
		}
		return calendarDao.findCalendarByPersionView(para);
	}
	@Override
	public PageDTO<SchedulingCalendarEntity> findCalendarPagesByPersionView(String persionName, Integer pageIndex,
			Integer pageSize) {
		// TODO Auto-generated method stub
		PageDTO<SchedulingCalendarEntity> result=new PageDTO<>(pageIndex,pageSize);
		Map<String, Object> para=new HashMap<>();
		if(persionName!=null&&!"".equals(persionName)){
			para.put("persionName", persionName);
		}
		if(pageIndex!=null){
			para.put("start", pageIndex);
		}
		if(pageSize!=null){
			para.put("end", pageSize);
		}
		List<SchedulingCalendarEntity> entities=calendarDao.findCalendarByPersionView(para);
		long count=calendarDao.countCalendarByPersionView(para);
		result.setData(entities);
		result.setRowSize(count);
		return result;
	}
	@Override
	public PageDTO<ShiftworkSchedulingCalendarEntity> findCalendarPagesByShiftworkView(String name,
			Integer pageIndex, Integer pageSize) {
		// TODO Auto-generated method stub
		PageDTO<ShiftworkSchedulingCalendarEntity> result=new PageDTO<>(pageIndex, pageSize);
		ShiftworkEntity entity=new ShiftworkEntity();
		if (name!=null&&!"".equals(name)) {
			entity.setName(name);
		}
		PageDTO<ShiftworkEntity> shiftworkPages= shiftworkService.findByPages(entity, pageIndex, pageSize);
		List<ShiftworkEntity> shiftworkEntities=shiftworkPages.getData();
		if(shiftworkEntities!=null&&shiftworkEntities.size()>0){
			List<ShiftworkSchedulingCalendarEntity> shiftworkSchedulingCalendarEntities=new ArrayList<>();
			for (ShiftworkEntity shiftworkEntity : shiftworkEntities) {
				ShiftworkSchedulingCalendarEntity shiftworkSchedulingCalendarEntity= calendarDao.findCalendarByShiftworkView(shiftworkEntity.getId());
				shiftworkSchedulingCalendarEntity.setShiftworkId(shiftworkEntity.getId());
				shiftworkSchedulingCalendarEntity.setShiftworkName(shiftworkEntity.getName());
				shiftworkSchedulingCalendarEntities.add(shiftworkSchedulingCalendarEntity);
			}
			result.setData(shiftworkSchedulingCalendarEntities);
			result.setRowSize(shiftworkPages.getRowSize());
		}
		return result;
	}

}
