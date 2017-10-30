package com.ehl.itspark.data.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ehl.itspark.data.dao.ModifyCalendarDao;
import com.ehl.itspark.data.entity.ModifyCalendarEntity;
import com.ehl.itspark.data.service.intf.ModifyCalendarService;

@Service
public class ModifyCalendarServiceImpl implements ModifyCalendarService{

	@Autowired
	private ModifyCalendarDao modifyCalendarDao;
	
	@Override
	public int saveModifyCalendarRecord(ModifyCalendarEntity entity) throws Exception {
		// TODO Auto-generated method stub
		return modifyCalendarDao.save(entity);
	}

	@Override
	public int updateModifyCalendarRecord(ModifyCalendarEntity entity) throws Exception {
		// TODO Auto-generated method stub
		return modifyCalendarDao.update(entity);
	}

	@Override
	public List<ModifyCalendarEntity> findModifyCalendarsByPersionId(Long persionId) {
		// TODO Auto-generated method stub
		Map<String, Object> para=new HashMap<>();
		if(persionId!=null){
			para.put("persionId", persionId);
		}
		return modifyCalendarDao.findModifyCalendars(para);
	}

	@Override
	public List<ModifyCalendarEntity> findModifyCalendarsByPersionIdAndModifyTime(Long persionId, Date modifyTime) {
		// TODO Auto-generated method stub
		Map<String, Object> para=new HashMap<>();
		if(persionId!=null){
			para.put("persionId", persionId);
		}
		if(modifyTime!=null){
			para.put("modifyTime", modifyTime);
		}
		return modifyCalendarDao.findModifyCalendars(para);
	}

	@Override
	@Transactional(rollbackFor={Exception.class})
	public int saveModifyCalendarRecords(List<ModifyCalendarEntity> entities) throws Exception {
		// TODO Auto-generated method stub
		if(entities!=null&&entities.size()>0){
			for (ModifyCalendarEntity modifyCalendarEntity : entities) {
				modifyCalendarDao.save(modifyCalendarEntity);
			}
		}
		return 1;
	}

	@Override
	@Transactional(rollbackFor={Exception.class})
	public int updateModifyCalendarRecords(List<ModifyCalendarEntity> entities) throws Exception {
		// TODO Auto-generated method stub
		if(entities!=null&&entities.size()>0){
			for (ModifyCalendarEntity modifyCalendarEntity : entities) {
				modifyCalendarDao.update(modifyCalendarEntity);
			}
		}
		return 1;
	}

	@Override
	public int deleteByPersionIdAndModifyTime(Long persionId, Date modifyTime) throws Exception {
		// TODO Auto-generated method stub
		return modifyCalendarDao.delete(persionId, modifyTime);
	}
}
