package com.ehl.itspark.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ehl.itspark.data.entity.ClockEntity;
import com.ehl.itspark.data.service.intf.ClockService;
import com.ehl.itspark.service.intf.ClockApiService;

@Service
public class ClockApiServiceImpl implements ClockApiService{

	@Autowired
	private ClockService clockService;
	
	@Override
	public int saveClock(ClockEntity clock) throws Exception {
		// TODO Auto-generated method stub
		return clockService.saveClock(clock);
	}

}
