package com.ehl.itspark.data.service.intf;

import java.util.List;

import com.ehl.itspark.common.PageDTO;
import com.ehl.itspark.data.entity.AreaTypeEntity;

public interface AreaTypeService {

	PageDTO<AreaTypeEntity> findAreaTypeByPage(AreaTypeEntity area,Integer pageIndex,Integer pageSize);
	
	List<AreaTypeEntity> findAll();
	
	int save(AreaTypeEntity entity) throws Exception;
	
	int update(AreaTypeEntity entity) throws Exception;
}
