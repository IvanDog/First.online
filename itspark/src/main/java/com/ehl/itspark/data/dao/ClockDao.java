package com.ehl.itspark.data.dao;

import com.ehl.itspark.common.MyBatisRepository;
import com.ehl.itspark.data.entity.ClockEntity;

@MyBatisRepository
public interface ClockDao {
	
	int save(ClockEntity entity);

}
