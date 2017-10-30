package com.ehl.itspark.data.service.intf;

import java.util.List;

import com.ehl.itspark.common.PageDTO;
import com.ehl.itspark.data.entity.PersonEntity;


public interface PersonService {

	PageDTO<PersonEntity> findPersionByPage(PersonEntity persion,Integer pageIndex,Integer pageSize);
	
	List<PersonEntity> findPersions(PersonEntity persion,Integer pageIndex,Integer pageSize);
	
	int savePersion(PersonEntity persion)throws Exception;
	
	int updatePersion(PersonEntity persion)throws Exception;
	
}
