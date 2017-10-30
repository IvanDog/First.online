package com.ehl.itspark.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ehl.itspark.data.entity.PersonAccessEntity;
import com.ehl.itspark.data.service.intf.PersonAccessService;
import com.ehl.itspark.service.intf.PersonAccessApiService;

@Service
public class PersonAccessApiServiceImpl implements PersonAccessApiService{

	@Autowired
	private PersonAccessService persionAccessService; 
	
	@Override
	public int savePersionAccess(PersonAccessEntity entity) throws Exception {
		// TODO Auto-generated method stub
		return persionAccessService.savePersionAccess(entity);
	}
	
	@Override
	public int updatePersionAccessName(PersonAccessEntity entity) throws Exception {
		// TODO Auto-generated method stub
		return persionAccessService.updatePersionAccessName(entity);
	}
	
	@Override
	public int updatePersionAccessPword(PersonAccessEntity entity) throws Exception {
		// TODO Auto-generated method stub
		return persionAccessService.updatePersionAccessPword(entity);
	}
	
	@Override
	public PersonAccessEntity findPersionAccess(String account) {
		// TODO Auto-generated method stub
		PersonAccessEntity entity=new PersonAccessEntity();
		entity.setNo(account);
		List<PersonAccessEntity> persionAccessEntities= persionAccessService.findPesionAccess(entity);
		if(persionAccessEntities==null||persionAccessEntities.size()==0){
			return null;
		}
		return persionAccessEntities.get(0);
	}

}
