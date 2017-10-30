package com.ehl.itspark.data.service.intf;

import com.ehl.itspark.common.PageDTO;
import com.ehl.itspark.data.entity.OwnerEntity;

public interface OwnerService {

	PageDTO<OwnerEntity> findOwnersByPage(OwnerEntity entity,Integer pageInex,Integer pageSize);
	
	int save(OwnerEntity entity)throws Exception;
	
	int update(OwnerEntity entity)throws Exception;
	
}
