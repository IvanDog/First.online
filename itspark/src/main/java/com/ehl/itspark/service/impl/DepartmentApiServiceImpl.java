package com.ehl.itspark.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ehl.itspark.data.entity.DepartmentEntity;
import com.ehl.itspark.data.service.intf.DepartmentService;
import com.ehl.itspark.service.intf.DepartmentApiService;

@Service
public class DepartmentApiServiceImpl implements DepartmentApiService{

	@Autowired
	private DepartmentService departmentService;
	
	@Override
	public List<DepartmentEntity> findAll() {
		// TODO Auto-generated method stub
		return departmentService.findAll();
	}

}
