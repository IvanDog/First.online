package com.ehl.itspark.data.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ehl.itspark.common.PageDTO;
import com.ehl.itspark.data.dao.DepartmentDao;
import com.ehl.itspark.data.entity.DepartmentEntity;
import com.ehl.itspark.data.service.intf.DepartmentService;
@Service
@Transactional
public class DepartmentServiceImpl implements DepartmentService{

	@Autowired
	private DepartmentDao departmentDao;
	
	@Override
	public PageDTO<DepartmentEntity> findDepartmentByPage(DepartmentEntity area, Integer pageIndex, Integer pageSize) {
		// TODO Auto-generated method stub
		PageDTO<DepartmentEntity> result=new PageDTO<>(pageIndex, pageSize);
		Map<String, Object> para=new HashMap<>();
		if(area!=null){
			if(area.getNo()!=null&&!"".equals(area.getNo())){
				para.put("no", area.getNo());
			}
			if(area.getName()!=null&&!"".equals(area.getName())){
				para.put("name", area.getName());
			}
			if(area.getParentNo()!=null&&!"".equals(area.getParentNo())){
				para.put("parentNo", area.getParentNo());
			}
			if(area.getParentName()!=null&&!"".equals(area.getParentName())){
				para.put("parentName", area.getParentName());
			}
		}
		if(pageIndex!=null&&pageSize!=null){
			para.put("start", pageIndex.intValue());
			para.put("end", pageSize);
		}
		List<DepartmentEntity> areaEntities= departmentDao.findAll(para);
		long count=departmentDao.count(para);
		result.setData(areaEntities);
		result.setRowSize(count);
		return result;
	}

	@Override
	public List<DepartmentEntity> findAll() {
		// TODO Auto-generated method stub
		return departmentDao.findAll(null);
	}

	@Override
	public List<DepartmentEntity> findByParentNo(String parentNo) {
		// TODO Auto-generated method stub
		Map<String, Object> para=new HashMap<>();
		if(parentNo!=null&&!"".equals(parentNo)){
			para.put("parentNo", parentNo);
		}
		return departmentDao.findAll(para);
	}

	@Override
	public int save(DepartmentEntity entity) throws Exception {
		// TODO Auto-generated method stub
		return departmentDao.save(entity);
	}

	@Override
	public int update(DepartmentEntity entity) throws Exception {
		// TODO Auto-generated method stub
		return departmentDao.update(entity);
	}

}
