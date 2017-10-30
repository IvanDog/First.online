package com.ehl.itspark.data.service.intf;

import java.util.List;

import com.ehl.itspark.common.PageDTO;
import com.ehl.itspark.data.entity.FeeRateEntity;

public interface FeeService {

	List<FeeRateEntity> findByParkNo(String parkNo);
	
	PageDTO<FeeRateEntity> findByPage(FeeRateEntity entity,Integer pageIndex,Integer pageSize);
	
	int saveFeeRate(FeeRateEntity entity)throws Exception;
	
	int updateFeeRate(FeeRateEntity entity) throws Exception;
	
	int deleteFeeRateById(Long id)throws Exception;
}
