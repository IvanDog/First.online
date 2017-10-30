package com.ehl.itspark.service.intf;

import java.util.List;

import com.ehl.itspark.data.entity.AreaEntity;

public interface AreaApiService {

	List<AreaEntity> findAll();
	
	AreaEntity findAreaByNo(String no);
}
