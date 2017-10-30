package com.ehl.itspark.data.service.intf;

import java.util.List;

import com.ehl.itspark.common.PageDTO;
import com.ehl.itspark.data.entity.DepartmentEntity;

public interface DepartmentService {

	PageDTO<DepartmentEntity> findDepartmentByPage(DepartmentEntity area,Integer pageIndex,Integer pageSize);
	
	List<DepartmentEntity> findAll();
	
	List<DepartmentEntity> findByParentNo(String parentNo);
	
	int save(DepartmentEntity entity) throws Exception;
	
	int update(DepartmentEntity entity) throws Exception;
}
