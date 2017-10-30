package com.ehl.itspark.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ehl.itspark.common.PageDTO;
import com.ehl.itspark.data.entity.PersonEntity;
import com.ehl.itspark.data.service.intf.PersonService;
import com.ehl.itspark.service.intf.PersonApiService;
@Service
public class PersonApiServiceImpl implements PersonApiService{

	@Autowired
	private PersonService persionService;
	
	@Override
	public PageDTO<PersonEntity> findPersionByPage(PersonEntity persion, Integer pageIndex, Integer pageSize) {
		// TODO Auto-generated method stub
		return persionService.findPersionByPage(persion, pageIndex, pageSize);
	}

	@Override
	public int savePersion(PersonEntity persion) throws Exception {
		// TODO Auto-generated method stub
		return persionService.savePersion(persion);
	}

	@Override
	public PersonEntity findPersionsByNo(String persinNo) {
		// TODO Auto-generated method stub
		PersonEntity entity=new PersonEntity();
		entity.setNo(persinNo);
		List<PersonEntity> persionEntities= persionService.findPersions(entity, null, null);
		if(persionEntities==null||persionEntities.size()==0){
			return null;
		}else{
			return persionEntities.get(0);
		}
	}

	@Override
	public List<PersonEntity> findByParkNo(String parkNo) {
		// TODO Auto-generated method stub
		PersonEntity entity=new PersonEntity();
		entity.setParkNo(parkNo);
		return persionService.findPersions(entity, null, null);
	}

}
