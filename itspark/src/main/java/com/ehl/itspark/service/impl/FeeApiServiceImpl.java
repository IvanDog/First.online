package com.ehl.itspark.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ehl.itspark.data.entity.FeeRateEntity;
import com.ehl.itspark.data.service.intf.FeeService;
import com.ehl.itspark.service.intf.FeeApiService;

@Service
public class FeeApiServiceImpl implements FeeApiService{

	@Autowired
	private FeeService feeService;
	
	@Override
	public FeeRateEntity findByParkNo(String parkNo) {
		// TODO Auto-generated method stub
		if(feeService.findByParkNo(parkNo).isEmpty()){
			return null;
		}else{
			return feeService.findByParkNo(parkNo).get(0);
		}
	}
}
