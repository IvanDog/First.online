package com.ehl.itspark.service.intf;

import com.ehl.itspark.data.entity.ClockEntity;

public interface ClockApiService {

	int saveClock(ClockEntity clock)throws Exception;

}
