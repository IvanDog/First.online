package com.ehl.itspark.data.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ehl.itspark.common.PageDTO;
import com.ehl.itspark.data.dao.PersonDao;
import com.ehl.itspark.data.entity.PersonEntity;
import com.ehl.itspark.data.service.intf.PersonService;
@Service
@Transactional
public class PersonServiceImpl implements PersonService{

	@Autowired
	private PersonDao persionDao;
	
	@Override
	public List<PersonEntity> findPersions(PersonEntity persion, Integer pageIndex, Integer pageSize) {
		// TODO Auto-generated method stub
		Map<String, Object> para = convertEntityToMap(persion, pageIndex, pageSize);
		return persionDao.findAll(para);
	}

	private Map<String, Object> convertEntityToMap(PersonEntity persion, Integer pageIndex, Integer pageSize) {
		Map<String, Object> para=new HashMap<>();
		if(persion!=null){
			if(Long.valueOf(persion.getId())!=null&&persion.getId()!=0){
				para.put("id", persion.getId());
			}
			if(persion.getNo()!=null&&!"".equals(persion.getNo())){
				para.put("no", persion.getNo());
			}
			if(persion.getName()!=null&&!"".equals(persion.getName())){
				para.put("name", persion.getName());
			}
			if(persion.getParkNo()!=null&&!"".equals(persion.getParkNo())){
				para.put("parkNo", persion.getParkNo());
			}
			if(persion.getParkName()!=null&&!"".equals(persion.getParkName())){
				para.put("parkName", persion.getParkName());
			}
			if(persion.getPostNo()!=null&&!"".equals(persion.getPostNo())){
				para.put("postNo", persion.getPostNo());
			}
			if(persion.getPostName()!=null&&!"".equals(persion.getPostName())){
				para.put("postName", persion.getPostName());
			}
		}
		if(pageIndex!=null&&pageSize!=null){
			para.put("start", pageIndex.intValue());
			para.put("end", pageSize);
		}
		return para;
	}

	@Override
	public int savePersion(PersonEntity persion) throws Exception{
		// TODO Auto-generated method stub
		return persionDao.save(persion);
	}

	@Override
	public int updatePersion(PersonEntity persion) throws Exception{
		// TODO Auto-generated method stub
		return persionDao.update(persion);
	}

	@Override
	public PageDTO<PersonEntity> findPersionByPage(PersonEntity persion, Integer pageIndex, Integer pageSize) {
		// TODO Auto-generated method stub
		Map<String, Object> para = convertEntityToMap(persion, pageIndex, pageSize);
		List<PersonEntity> persionEntities= persionDao.findAll(para);
		long count=persionDao.count(para);
		PageDTO<PersonEntity> result=new PageDTO<>(pageIndex, pageSize);
		result.setData(persionEntities);
		result.setRowSize(count);
		return result;
	}

}
