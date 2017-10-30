package com.ehl.itspark.service.intf;

import java.util.List;

import com.ehl.itspark.common.PageDTO;
import com.ehl.itspark.data.entity.PersonEntity;

public interface PersonApiService {

	PageDTO<PersonEntity> findPersionByPage(PersonEntity persion,Integer pageIndex,Integer pageSize);
	
	List<PersonEntity> findByParkNo(String parkNo);
	
	PersonEntity findPersionsByNo(String persinNo);
	
	int savePersion(PersonEntity persion)throws Exception;
}
