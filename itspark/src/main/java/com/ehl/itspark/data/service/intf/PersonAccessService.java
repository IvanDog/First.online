package com.ehl.itspark.data.service.intf;

import java.util.List;

import com.ehl.itspark.common.PageDTO;
import com.ehl.itspark.data.entity.PersonAccessEntity;

public interface PersonAccessService {

	PageDTO<PersonAccessEntity> findByPage(PersonAccessEntity entity,Integer pageIndex,Integer pageSize);
	
	List<PersonAccessEntity> findPesionAccess(PersonAccessEntity entity);
	
	int savePersionAccess(PersonAccessEntity entity)throws Exception;
	
	int updatePersionAccessName(PersonAccessEntity entity)throws Exception;
	
	int updatePersionAccessPword(PersonAccessEntity entity)throws Exception;
}
