package com.ehl.itspark.data.service.intf;

import java.util.List;

import com.ehl.itspark.common.PageDTO;
import com.ehl.itspark.data.entity.AreaEntity;

public interface AreaService {

	PageDTO<AreaEntity> findAreaByPage(AreaEntity area,Integer pageIndex,Integer pageSize);
	
	List<AreaEntity> findAreas(AreaEntity area);
	
	AreaEntity findAreaByNo(String no);
	
	List<AreaEntity> findAll();
	
	int save(AreaEntity entity) throws Exception;
	
	int update(AreaEntity entity) throws Exception;
}
