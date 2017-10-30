package com.ehl.itspark.data.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ehl.itspark.common.PageDTO;
import com.ehl.itspark.data.dao.SchedulingDao;
import com.ehl.itspark.data.entity.SchedulingEntity;
import com.ehl.itspark.data.entity.ShiftworkEntity;
import com.ehl.itspark.data.service.intf.SchedulingService;
import com.ehl.itspark.data.service.intf.ShiftworkService;

@Service
public class SchedulingServiceImpl implements SchedulingService{

	@Autowired
	private SchedulingDao schedulingDao;
	@Autowired
	private ShiftworkService shiftworkService;
	
	@Override
	public int saveScheduling(SchedulingEntity entity) throws Exception {
		// TODO Auto-generated method stub
		 return schedulingDao.save(entity);
	}

	@Override
	public int updateScheduling(SchedulingEntity entity) throws Exception {
		// TODO Auto-generated method stub
		return schedulingDao.update(entity);
	}

	@Override
	public List<SchedulingEntity> findList(SchedulingEntity entity) {
		// TODO Auto-generated method stub
		Map<String, Object> para=new HashMap<>();
		if(entity!=null){
			if(entity.getId()!=null){
				para.put("id", entity.getId());
			}
			if(entity.getName()!=null&&!"".equals(entity.getName())){
				para.put("name", entity.getName());
			}
		}
		List<SchedulingEntity> entities= schedulingDao.findList(para);
		if(entities!=null&&entities.size()>0){
			for (SchedulingEntity schedulingEntity : entities) {
				ShiftworkEntity mondayShiftworkEntity= shiftworkService.findById(schedulingEntity.getMondayId());
				schedulingEntity.setMondayName(mondayShiftworkEntity==null?"":mondayShiftworkEntity.getName());
				mondayShiftworkEntity= shiftworkService.findById(schedulingEntity.getTuesdayId());
				schedulingEntity.setTuesdayName(mondayShiftworkEntity==null?"":mondayShiftworkEntity.getName());
				mondayShiftworkEntity= shiftworkService.findById(schedulingEntity.getWednesdayId());
				schedulingEntity.setWednesdayName(mondayShiftworkEntity==null?"":mondayShiftworkEntity.getName());
				mondayShiftworkEntity= shiftworkService.findById(schedulingEntity.getThursdayId());
				schedulingEntity.setThursdayName(mondayShiftworkEntity==null?"":mondayShiftworkEntity.getName());
				mondayShiftworkEntity= shiftworkService.findById(schedulingEntity.getFridayId());
				schedulingEntity.setFridayName(mondayShiftworkEntity==null?"":mondayShiftworkEntity.getName());
				mondayShiftworkEntity= shiftworkService.findById(schedulingEntity.getSaturdayId());
				schedulingEntity.setSaturdayName(mondayShiftworkEntity==null?"":mondayShiftworkEntity.getName());
				mondayShiftworkEntity= shiftworkService.findById(schedulingEntity.getSundayId());
				schedulingEntity.setSundayName(mondayShiftworkEntity==null?"":mondayShiftworkEntity.getName());
			}
		}
		return entities;
	}

	@Override
	public PageDTO<SchedulingEntity> findByPages(SchedulingEntity entity, Integer pageIndex, Integer pageSize) {
		// TODO Auto-generated method stub
		PageDTO<SchedulingEntity> pageDTO=new PageDTO<>(pageIndex, pageSize);
		Map<String, Object> para=new HashMap<>();
		if(entity!=null){
			if(entity.getId()!=null){
				para.put("id", entity.getId());
			}
			if(entity.getName()!=null&&!"".equals(entity.getName())){
				para.put("name", entity.getName());
			}
		}
		if(pageIndex!=null){
			para.put("start", pageIndex);
		}
		if(pageSize!=null){
			para.put("end", pageSize);
		}
		long count=schedulingDao.count(para);
		List<SchedulingEntity> entities=schedulingDao.findList(para);
		if(entities!=null&&entities.size()>0){
			for (SchedulingEntity schedulingEntity : entities) {
				ShiftworkEntity mondayShiftworkEntity= shiftworkService.findById(schedulingEntity.getMondayId());
				schedulingEntity.setMondayName(mondayShiftworkEntity==null?"":mondayShiftworkEntity.getName());
				mondayShiftworkEntity= shiftworkService.findById(schedulingEntity.getTuesdayId());
				schedulingEntity.setTuesdayName(mondayShiftworkEntity==null?"":mondayShiftworkEntity.getName());
				mondayShiftworkEntity= shiftworkService.findById(schedulingEntity.getWednesdayId());
				schedulingEntity.setWednesdayName(mondayShiftworkEntity==null?"":mondayShiftworkEntity.getName());
				mondayShiftworkEntity= shiftworkService.findById(schedulingEntity.getThursdayId());
				schedulingEntity.setThursdayName(mondayShiftworkEntity==null?"":mondayShiftworkEntity.getName());
				mondayShiftworkEntity= shiftworkService.findById(schedulingEntity.getFridayId());
				schedulingEntity.setFridayName(mondayShiftworkEntity==null?"":mondayShiftworkEntity.getName());
				mondayShiftworkEntity= shiftworkService.findById(schedulingEntity.getSaturdayId());
				schedulingEntity.setSaturdayName(mondayShiftworkEntity==null?"":mondayShiftworkEntity.getName());
				mondayShiftworkEntity= shiftworkService.findById(schedulingEntity.getSundayId());
				schedulingEntity.setSundayName(mondayShiftworkEntity==null?"":mondayShiftworkEntity.getName());
			}
		}
		pageDTO.setData(entities);
		pageDTO.setRowSize(count);
		return pageDTO;
	}

}
