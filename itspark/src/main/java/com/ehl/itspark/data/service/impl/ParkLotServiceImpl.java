package com.ehl.itspark.data.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ehl.itspark.common.PageDTO;
import com.ehl.itspark.data.dao.ParkLotDao;
import com.ehl.itspark.data.entity.ParkLotEntity;
import com.ehl.itspark.data.service.intf.ParkLotService;
@Service
@Transactional
public class ParkLotServiceImpl implements ParkLotService{

	@Autowired
	private ParkLotDao parkLotDao;
	
	@Override
	public List<ParkLotEntity> findParkLots(ParkLotEntity parkLot, Integer pageIndex, Integer pageSize) {
		// TODO Auto-generated method stub
		return findParkLots(parkLot, null, null, pageIndex, pageSize);
	}

	@Override
	public List<ParkLotEntity> findParkLots(ParkLotEntity parkLot, Date startUpdateTime, Date endUpdateTime,
			Integer pageIndex, Integer pageSize) {
		// TODO Auto-generated method stub
		Map<String, Object> para = convertEntityToMap(parkLot, startUpdateTime, endUpdateTime, pageIndex, pageSize);
		return parkLotDao.findAll(para);
	}

	private Map<String, Object> convertEntityToMap(ParkLotEntity parkLot, Date startUpdateTime, Date endUpdateTime,
			Integer pageIndex, Integer pageSize) {
		Map<String, Object> para=new HashMap<>();
		if(parkLot!=null){
			if(parkLot.getNo()!=null&&!"".equals(parkLot.getNo())){
				para.put("no", parkLot.getNo());
			}
			if(parkLot.getParkNo()!=null&&!"".equals(parkLot.getParkNo())){
				para.put("parkNo", parkLot.getParkNo());
			}
			if(parkLot.getStatus()!=null){
				para.put("status", parkLot.getStatus());
			}
			if(parkLot.getPlate()!=null&&!"".equals(parkLot.getPlate())){
				para.put("plate", parkLot.getPlate());
			}
		}
		if(startUpdateTime!=null){
			para.put("startTime", startUpdateTime);
		}
		if(endUpdateTime!=null){
			para.put("endTime", endUpdateTime);
		}
		if(pageIndex!=null&&pageSize!=null){
			para.put("start", pageIndex.intValue());
			para.put("end", pageSize);
		}
		return para;
	}

	@Override
	public int saveParkLot(ParkLotEntity parkLot) throws Exception{
		// TODO Auto-generated method stub
		return parkLotDao.save(parkLot);
	}

	@Override
	public int updateParkLot(ParkLotEntity parkLot) throws Exception{
		// TODO Auto-generated method stub
		return parkLotDao.update(parkLot);
	}

	@Override
	public PageDTO<ParkLotEntity> findParkLotByPage(ParkLotEntity parkLot, Date startUpdateTime, Date endUpdateTime,
			Integer pageIndex, Integer pageSize) {
		// TODO Auto-generated method stub
		Map<String, Object> para = convertEntityToMap(parkLot, startUpdateTime, endUpdateTime, pageIndex, pageSize);
		List<ParkLotEntity> parkLotEntities= parkLotDao.findAll(para);
		long count=parkLotDao.count(para);
		PageDTO<ParkLotEntity> result=new PageDTO<>(pageIndex, pageSize);
		result.setData(parkLotEntities);
		result.setRowSize(count);
		return result;
	}

}
