package com.ehl.itspark.data.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ehl.itspark.common.PageDTO;
import com.ehl.itspark.data.dao.OwnerDao;
import com.ehl.itspark.data.entity.OwnerEntity;
import com.ehl.itspark.data.service.intf.OwnerService;

@Service
@Transactional
public class OwnerServiceImpl implements OwnerService{

	@Autowired
	private OwnerDao ownerDao;
	
	@Override
	public PageDTO<OwnerEntity> findOwnersByPage(OwnerEntity entity, Integer pageIndex, Integer pageSize) {
		// TODO Auto-generated method stub
		Map<String, Object> para=new HashMap<>();
		if(entity!=null){
			if(entity.getNo()!=null&&!"".equals(entity.getNo())){
				para.put("no", entity.getNo());
			}
			if(entity.getName()!=null&&!"".equals(entity.getName())){
				para.put("name", entity.getName());
			}
			if(entity.getPhone()!=null&&!"".equals(entity.getPhone())){
				para.put("phone", entity.getPhone());
			}
		}
		if(pageIndex!=null&&pageSize!=null){
			para.put("start", pageIndex.intValue());
			para.put("end", pageSize);
		}
		List<OwnerEntity> entities= ownerDao.findAll(para);
		long count= ownerDao.count(para);
		PageDTO<OwnerEntity> result=new PageDTO<>(pageIndex, pageSize);
		result.setData(entities);
		result.setRowSize(count);
		return result;
	}

	@Override
	public int save(OwnerEntity entity) throws Exception {
		// TODO Auto-generated method stub
		return ownerDao.save(entity);
	}

	@Override
	public int update(OwnerEntity entity) throws Exception {
		// TODO Auto-generated method stub
		if(entity==null||"".equals(entity.getNo())){
			throw new Exception("输入的车主参数为空或车主编号为空！");
		}
		return ownerDao.updateByNo(entity);
	}

}
