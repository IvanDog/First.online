package com.ehl.itspark.service.intf;

import com.ehl.itspark.data.entity.PersonAccessEntity;

public interface PersonAccessApiService {

	PersonAccessEntity findPersionAccess(String account);
	
	int savePersionAccess(PersonAccessEntity entity)throws Exception;
	
	int updatePersionAccessName(PersonAccessEntity entity)throws Exception;
	
	int updatePersionAccessPword(PersonAccessEntity entity)throws Exception;
}
