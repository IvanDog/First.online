package com.ehl.itspark.data.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ehl.itspark.common.PageDTO;
import com.ehl.itspark.data.dao.PersonAccessDao;
import com.ehl.itspark.data.entity.PersonAccessEntity;
import com.ehl.itspark.data.service.intf.PersonAccessService;

@Service
@Transactional
public class PersonAccessServiceImpl implements PersonAccessService{

	@Autowired
	private PersonAccessDao persionAccessDao;
	
	@Override
	public PageDTO<PersonAccessEntity> findByPage(PersonAccessEntity entity, Integer pageIndex, Integer pageSize) {
		// TODO Auto-generated method stub
		PageDTO<PersonAccessEntity> result=new PageDTO<>(pageIndex, pageSize);
		Map<String, Object> para = convertMap(entity, pageIndex, pageSize);
		List<PersonAccessEntity> areaEntities= persionAccessDao.findAll(para);
		long count=persionAccessDao.count(para);
		result.setData(areaEntities);
		result.setRowSize(count);
		return result;
	}

	private Map<String, Object> convertMap(PersonAccessEntity entity, Integer pageIndex, Integer pageSize) {
		Map<String, Object> para=new HashMap<>();
		if(entity!=null){
			if(entity.getNo()!=null&&!"".equals(entity.getNo())){
				para.put("no", entity.getNo());
			}
			if(entity.getName()!=null&&!"".equals(entity.getName())){
				para.put("name", entity.getName());
			}
		}
		if(pageIndex!=null&&pageSize!=null){
			para.put("start", pageIndex.intValue());
			para.put("end", pageSize);
		}
		return para;
	}

	@Override
	public int savePersionAccess(PersonAccessEntity entity) throws Exception {
		// TODO Auto-generated method stub
		return persionAccessDao.save(entity);
	}

	@Override
	public int updatePersionAccessName(PersonAccessEntity entity) throws Exception {
		// TODO Auto-generated method stub
		return persionAccessDao.updateName(entity);
	}

	@Override
	public int updatePersionAccessPword(PersonAccessEntity entity) throws Exception {
		// TODO Auto-generated method stub
		return persionAccessDao.updatePword(entity);
	}

	@Override
	public List<PersonAccessEntity> findPesionAccess(PersonAccessEntity entity) {
		// TODO Auto-generated method stub
		Map<String, Object> para = convertMap(entity, null, null);
		return persionAccessDao.findAll(para);
	}


}
