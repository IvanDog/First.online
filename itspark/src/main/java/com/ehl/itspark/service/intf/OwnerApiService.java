package com.ehl.itspark.service.intf;

import java.util.List;

import com.ehl.itspark.data.entity.OwnerEntity;

public interface OwnerApiService {

	List<OwnerEntity> findOwners(OwnerEntity entity);
	
	OwnerEntity findOwnerByAccount(String account);
	
	OwnerEntity findOwnerByOwnerNo(String ownerNo);
	
	int save(OwnerEntity entity)throws Exception;
	
	int update(OwnerEntity entity)throws Exception;

}
