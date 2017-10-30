package com.ehl.itspark.data.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ehl.itspark.common.PageDTO;
import com.ehl.itspark.data.dao.CarDao;
import com.ehl.itspark.data.entity.CarEntity;
import com.ehl.itspark.data.service.intf.CarService;

@Service
@Transactional
public class CarServiceImpl implements CarService{

	@Autowired
	private CarDao carDao;
	
	@Override
	public PageDTO<CarEntity> findCarsByPage(CarEntity entity, Integer pageIndex, Integer pageSize) {
		// TODO Auto-generated method stub
		Map<String, Object> para=new HashMap<>();
		if(entity!=null){
			if(entity.getPlate()!=null&&!"".equals(entity.getPlate())){
				para.put("plate", entity.getPlate());
			}
			if(entity.getOwnerNo()!=null&&!"".equals(entity.getOwnerNo())){
				para.put("ownerNo", entity.getOwnerNo());
			}
		}
		if(pageIndex!=null&&pageSize!=null){
			para.put("start", pageIndex.intValue());
			para.put("end", pageSize);
		}
		PageDTO<CarEntity> result=new PageDTO<>(pageIndex, pageSize);
		List<CarEntity> areaEntities= carDao.findAll(para);
		long count=carDao.count(para);
		result.setData(areaEntities);
		result.setRowSize(count);
		return result;
	}

	@Override
	public List<CarEntity> findCarsByOwnerNo(String ownerNo) {
		// TODO Auto-generated method stub
		Map<String, Object> para=new HashMap<>();
		para.put("ownerNo", ownerNo);
		return carDao.findAll(para);
	}

	@Override
	public List<CarEntity> findCarsByPlateAndType(String plate,String carType) {
		// TODO Auto-generated method stub
		Map<String, Object> para=new HashMap<>();
		para.put("plate", plate);
		para.put("carType", carType);
		return carDao.findAll(para);
	}

	@Override
	public int save(CarEntity entity) throws Exception {
		// TODO Auto-generated method stub
		return carDao.save(entity);
	}

	@Override
	public int updateByPlate(CarEntity entity) throws Exception {
		// TODO Auto-generated method stub
		return carDao.updateByPlate(entity);
	}

	@Override
	public int deleteByPlate(String plate) {
		// TODO Auto-generated method stub
		return carDao.deleteByPlate(plate);
	}

}
