package com.ehl.itspark.data.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ehl.itspark.common.PageDTO;
import com.ehl.itspark.data.dao.AreaDao;
import com.ehl.itspark.data.entity.AreaEntity;
import com.ehl.itspark.data.service.intf.AreaService;

@Service
@Transactional
public class AreaServiceImpl implements AreaService{

	@Autowired
	private AreaDao areaDao;
	
	@Override
	public PageDTO<AreaEntity> findAreaByPage(AreaEntity area, Integer pageIndex, Integer pageSize) {
		// TODO Auto-generated method stub
		PageDTO<AreaEntity> result=new PageDTO<>(pageIndex, pageSize);
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
		List<AreaEntity> areaEntities= areaDao.findAll(para);
		long count=areaDao.count(para);
		result.setData(areaEntities);
		result.setRowSize(count);
		return result;
	}
	
	

	@Override
	public List<AreaEntity> findAll() {
		// TODO Auto-generated method stub
		return areaDao.findAll(null);
	}

	@Override
	public int save(AreaEntity entity) throws Exception {
		// TODO Auto-generated method stub
		return areaDao.save(entity);
	}

	@Override
	public int update(AreaEntity entity) throws Exception{
		// TODO Auto-generated method stub
		return areaDao.update(entity);
	}



	@Override
	public List<AreaEntity> findAreas(AreaEntity area) {
		// TODO Auto-generated method stub
		Map<String, Object> para=new HashMap<>();
		if(area!=null){
			if(area.getNo()!=null&&!"".equals(area.getNo())){
				para.put("no", area.getNo());
			}
			if(area.getName()!=null&&!"".equals(area.getName())){
				para.put("name", area.getName());
			}
		}
		return areaDao.findAll(para);
	}



	@Override
	public AreaEntity findAreaByNo(String no) {
		// TODO Auto-generated method stub
		Map<String, Object> para=new HashMap<>();
		if(no!=null&&!"".equals(no)){
			para.put("no", no);
		}
		List<AreaEntity> areaEntities= areaDao.findAll(para);
		if(areaEntities!=null&&areaEntities.size()>0){
			return areaEntities.get(0);
		}else {
			return null;
		}
	}
}
