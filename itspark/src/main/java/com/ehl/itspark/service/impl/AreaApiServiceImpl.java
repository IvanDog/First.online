package com.ehl.itspark.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ehl.itspark.data.entity.AreaEntity;
import com.ehl.itspark.data.service.intf.AreaService;
import com.ehl.itspark.service.intf.AreaApiService;

@Service
public class AreaApiServiceImpl implements AreaApiService{

	@Autowired
	private AreaService areaService;
	
	@Override
	public List<AreaEntity> findAll() {
		// TODO Auto-generated method stub
		return areaService.findAll();
	}

	@Override
	public AreaEntity findAreaByNo(String no) {
		// TODO Auto-generated method stub
		return areaService.findAreaByNo(no);
	}

}
