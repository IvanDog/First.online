package com.ehl.itspark.data.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ehl.itspark.common.PageDTO;
import com.ehl.itspark.data.dao.FeeRateDao;
import com.ehl.itspark.data.entity.FeeRateEntity;
import com.ehl.itspark.data.service.intf.FeeService;

@Service
@Transactional
public class FeeServiceImpl implements FeeService{

	@Autowired
	private FeeRateDao feeRateDao;
	
	@Override
	public List<FeeRateEntity> findByParkNo(String parkNo) {
		// TODO Auto-generated method stub
		Map<String, Object> para=new HashMap<>();
		if(parkNo==null&&"".equals(parkNo)){
			throw new RuntimeException("传入的参数为空！");
		}
		para.put("parkNo", parkNo);
		return feeRateDao.findAll(para);
	}

	@Override
	public int saveFeeRate(FeeRateEntity entity) throws Exception {
		// TODO Auto-generated method stub
		return feeRateDao.save(entity);
	}

	@Override
	public int updateFeeRate(FeeRateEntity entity) throws Exception {
		// TODO Auto-generated method stub
		return feeRateDao.update(entity);
	}

	@Override
	public int deleteFeeRateById(Long id) throws Exception {
		// TODO Auto-generated method stub
		return feeRateDao.delete(id);
	}

	@Override
	public PageDTO<FeeRateEntity> findByPage(FeeRateEntity entity, Integer pageIndex, Integer pageSize) {
		// TODO Auto-generated method stub
		PageDTO<FeeRateEntity> result=new PageDTO<>(pageIndex, pageSize);
		Map<String, Object> para=new HashMap<>();
		if(entity!=null){
			if(entity.getParkNo()!=null||!"".equals(entity.getParkNo())){
				para.put("parkNo", entity.getParkNo());
			}
			if(entity.getPlateType()>0){
				para.put("plateType", entity.getPlateType());
			}
			if(entity.getNo()!=null||!"".equals(entity.getNo())){
				para.put("no", entity.getNo());
			}
		}
		if(pageIndex!=null&&pageSize!=null){
			para.put("start", pageIndex.intValue());
			para.put("end", pageSize);
		}
		long count=feeRateDao.count(para);
		result.setRowSize(count);
		List<FeeRateEntity> entities= feeRateDao.findAll(para);
		result.setData(entities);
		return result;
	}
}
