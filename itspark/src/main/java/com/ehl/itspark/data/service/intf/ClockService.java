package com.ehl.itspark.data.service.intf;

import com.ehl.itspark.data.entity.ClockEntity;

public interface ClockService {
	
	int saveClock(ClockEntity parkLot)throws Exception;
	
}
