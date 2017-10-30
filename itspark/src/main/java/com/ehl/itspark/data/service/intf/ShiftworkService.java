package com.ehl.itspark.data.service.intf;

import java.util.List;

import com.ehl.itspark.common.PageDTO;
import com.ehl.itspark.data.entity.ShiftworkEntity;

public interface ShiftworkService {

	int saveShiftwork(ShiftworkEntity entity)throws Exception;
	
	int updateShiftwork(ShiftworkEntity entity)throws Exception;
	
	int deleteShiftworkById(Long id)throws Exception;
	
	List<ShiftworkEntity> findShiftworks(ShiftworkEntity entity);
	
	PageDTO<ShiftworkEntity> findByPages(ShiftworkEntity entity,Integer pageIndex,Integer pageSize);
	
	ShiftworkEntity findById(Long id);
}
