package com.ehl.itspark.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ehl.itspark.data.entity.AreaTypeEntity;
import com.ehl.itspark.data.service.intf.AreaTypeService;
import com.ehl.itspark.service.intf.AreaTypeApiService;

@Service
public class AreaTypeApiServiceImpl implements AreaTypeApiService{

	@Autowired
	private AreaTypeService areaTypeService;
	
	@Override
	public List<AreaTypeEntity> findAll() {
		// TODO Auto-generated method stub
		return areaTypeService.findAll();
	}

}
