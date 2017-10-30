package com.ehl.itspark.data.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ehl.itspark.data.dao.ClockDao;
import com.ehl.itspark.data.entity.ClockEntity;
import com.ehl.itspark.data.service.intf.ClockService;

@Service
@Transactional
public class ClockServiceImpl implements ClockService{

	@Autowired
	private ClockDao clockDao;

	@Override
	public int saveClock(ClockEntity clock) throws Exception{
		// TODO Auto-generated method stub
		return clockDao.save(clock);
	}

}
