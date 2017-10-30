package com.ehl.itspark.service.intf;

import com.ehl.itspark.data.entity.FeeRateEntity;

public interface FeeApiService {

	 FeeRateEntity findByParkNo(String parkNo);
}
