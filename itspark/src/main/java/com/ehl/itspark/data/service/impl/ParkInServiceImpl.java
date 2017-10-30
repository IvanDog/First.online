package com.ehl.itspark.data.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ehl.itspark.common.PageDTO;
import com.ehl.itspark.data.dao.ParkInDao;
import com.ehl.itspark.data.entity.ParkEntity;
import com.ehl.itspark.data.service.intf.ParkInService;
@Service
@Transactional
public class ParkInServiceImpl implements ParkInService{

	@Autowired
	private ParkInDao parkInDao;
	
	@Override
	public List<ParkEntity> findParks(ParkEntity park, Integer pageIndex, Integer pageSize) {
		// TODO Auto-generated method stub
		return findParks(park,null,null,pageIndex,pageSize);
	}
	
	@Override
	public List<ParkEntity> findParks(ParkEntity park,Date startUpdateTime,Date endUpdateTime, Integer pageIndex, Integer pageSize) {
		// TODO Auto-generated method stub
		Map<String, Object> para = convertEntityToMap(park, startUpdateTime, endUpdateTime, pageIndex, pageSize);
		return parkInDao.findAll(para);
	}

	private Map<String, Object> convertEntityToMap(ParkEntity park, Date startUpdateTime, Date endUpdateTime,
			Integer pageIndex, Integer pageSize) {
		Map<String, Object> para=new HashMap<>();
		if(park!=null){
			if(park.getNo()!=null&&!"".equals(park.getNo())){
				para.put("no", park.getNo());
			}
			if(park.getName()!=null&&!"".equals(park.getName())){
				para.put("name", park.getName());
			}
			if(park.getArea()!=null&&park.getArea().getNo()!=null&&!"".equals(park.getArea().getNo())){
				para.put("areaNo", park.getArea().getNo());
			}
			if(park.getAreaType()!=null&&park.getAreaType().getNo()!=null&&!"".equals(park.getAreaType().getNo())){
				para.put("areaTypeNo", park.getAreaType().getNo());
			}
			if(park.getDepartment()!=null&&park.getDepartment().getNo()!=null&&!"".equals(park.getDepartment().getNo())){
				para.put("departmentNo", park.getDepartment().getNo());
			}
			if(park.getType()>0){
				para.put("type", park.getType());
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
	public int savePark(ParkEntity park) throws Exception{
		// TODO Auto-generated method stub
		if(park==null){
			throw new Exception("保存数据为空！");
		}
//		if(park.getPayMode()!=null&&!"".equals(park.getPayMode())){
//			park.setPayMode(InfoConvert.getPayModeValue(park.getPayMode()));
//		}
		return parkInDao.save(park);
	}

	@Override
	public int updatePark(ParkEntity park) throws Exception{
		// TODO Auto-generated method stub
		if(park==null){
			throw new Exception("更新的数据为空！");
		}
//		if(park.getPayMode()!=null&&!"".equals(park.getPayMode())){
//			park.setPayMode(InfoConvert.getPayModeValue(park.getPayMode()));
//		}
		return parkInDao.update(park);
	}

	@Override
	public PageDTO<ParkEntity> findParkByPage(ParkEntity park, Date startUpdateTime, Date endUpdateTime,
			Integer pageIndex, Integer pageSize) {
		// TODO Auto-generated method stub
		Map<String, Object> para = convertEntityToMap(park, startUpdateTime, endUpdateTime, pageIndex, pageSize);
		List<ParkEntity> parkEntities= parkInDao.findAll(para);
		long count=parkInDao.count(para);
		PageDTO<ParkEntity> result=new PageDTO<>(pageIndex, pageSize);
		result.setData(parkEntities);
		result.setRowSize(count);
		return result;
	}

}
