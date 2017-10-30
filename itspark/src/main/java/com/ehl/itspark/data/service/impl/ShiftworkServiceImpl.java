package com.ehl.itspark.data.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ehl.itspark.common.PageDTO;
import com.ehl.itspark.data.dao.ShiftworkDao;
import com.ehl.itspark.data.entity.ShiftworkEntity;
import com.ehl.itspark.data.service.intf.ShiftworkService;


@Service
public class ShiftworkServiceImpl implements ShiftworkService{

	@Autowired
	private ShiftworkDao shiftworkDao;
	
	@Override
	public int saveShiftwork(ShiftworkEntity entity) throws Exception {
		// TODO Auto-generated method stub
		return shiftworkDao.save(entity);
	}

	@Override
	public int updateShiftwork(ShiftworkEntity entity) throws Exception {
		// TODO Auto-generated method stub
		return shiftworkDao.update(entity);
	}

	@Override
	public int deleteShiftworkById(Long id) throws Exception {
		// TODO Auto-generated method stub
		return shiftworkDao.deleteById(id);
	}

	@Override
	public List<ShiftworkEntity> findShiftworks(ShiftworkEntity entity) {
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
		return shiftworkDao.findList(para);
	}

	@Override
	public PageDTO<ShiftworkEntity> findByPages(ShiftworkEntity entity, Integer pageIndex, Integer pageSize) {
		// TODO Auto-generated method stub
		PageDTO<ShiftworkEntity> pageDTO=new PageDTO<>(pageIndex, pageSize);
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
		long count=shiftworkDao.count(para);
		List<ShiftworkEntity> entities=shiftworkDao.findList(para);
		pageDTO.setData(entities);
		pageDTO.setRowSize(count);
		return pageDTO;
	}

	@Override
	public ShiftworkEntity findById(Long id) {
		// TODO Auto-generated method stub
		return shiftworkDao.findById(id);
	}

}
