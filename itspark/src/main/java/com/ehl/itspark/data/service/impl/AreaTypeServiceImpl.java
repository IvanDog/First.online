package com.ehl.itspark.data.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ehl.itspark.common.PageDTO;
import com.ehl.itspark.data.dao.AreaTypeDao;
import com.ehl.itspark.data.entity.AreaTypeEntity;
import com.ehl.itspark.data.service.intf.AreaTypeService;

@Service
@Transactional
public class AreaTypeServiceImpl implements AreaTypeService{

	@Autowired
	private AreaTypeDao areaTypeDao;
	
	@Override
	public PageDTO<AreaTypeEntity> findAreaTypeByPage(AreaTypeEntity area, Integer pageIndex, Integer pageSize) {
		// TODO Auto-generated method stub
		PageDTO<AreaTypeEntity> result=new PageDTO<>(pageIndex, pageSize);
		Map<String, Object> para=new HashMap<>();
		if(area!=null){
			if(area.getNo()!=null&&!"".equals(area.getNo())){
				para.put("no", area.getNo());
			}
			if(area.getName()!=null&&!"".equals(area.getName())){
				para.put("name", area.getName());
			}
		}
		if(pageIndex!=null&&pageSize!=null){
			para.put("start", pageIndex.intValue());
			para.put("end", pageSize);
		}
		List<AreaTypeEntity> areaEntities= areaTypeDao.findAll(para);
		long count=areaTypeDao.count(para);
		result.setData(areaEntities);
		result.setRowSize(count);
		return result;
	}

	@Override
	public List<AreaTypeEntity> findAll() {
		// TODO Auto-generated method stub
		return areaTypeDao.findAll(null);
	}

	@Override
	public int save(AreaTypeEntity entity) throws Exception {
		// TODO Auto-generated method stub
		return areaTypeDao.save(entity);
	}

	@Override
	public int update(AreaTypeEntity entity) throws Exception {
		// TODO Auto-generated method stub
		return areaTypeDao.update(entity);
	}

}
