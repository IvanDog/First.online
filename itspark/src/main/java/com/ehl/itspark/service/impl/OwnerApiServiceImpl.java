package com.ehl.itspark.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ehl.itspark.data.entity.OwnerEntity;
import com.ehl.itspark.data.service.intf.OwnerService;
import com.ehl.itspark.service.intf.OwnerApiService;


@Service
public class OwnerApiServiceImpl implements OwnerApiService{

	@Autowired
	private OwnerService ownerService; 
	@Override
	public int save(OwnerEntity entity) throws Exception {
		// TODO Auto-generated method stub
		return ownerService.save(entity);
	}
	
	@Override
	public int update(OwnerEntity entity) throws Exception {
		// TODO Auto-generated method stub
		return ownerService.update(entity);
	}
	
	@Override
	public List<OwnerEntity> findOwners(OwnerEntity entity) {
		// TODO Auto-generated method stub
		List<OwnerEntity> ownerEntities= ownerService.findOwnersByPage(entity, null, null).getData();
		if(ownerEntities==null||ownerEntities.size()==0){
			return null;
		}
		return ownerEntities;
	}
	
	@Override
	public OwnerEntity findOwnerByAccount(String account) {
		// TODO Auto-generated method stub
		OwnerEntity entity=new OwnerEntity();
		entity.setPhone(account);
		List<OwnerEntity> ownerEntities= ownerService.findOwnersByPage(entity, null, null).getData();
		if(ownerEntities==null||ownerEntities.size()==0){
			return null;
		}
		return ownerEntities.get(0);
	}

	@Override
	public OwnerEntity findOwnerByOwnerNo(String ownerNo) {
		// TODO Auto-generated method stub
		OwnerEntity entity=new OwnerEntity();
		entity.setNo(ownerNo);
		List<OwnerEntity> ownerEntities= ownerService.findOwnersByPage(entity, null, null).getData();
		if(ownerEntities==null||ownerEntities.size()==0){
			return null;
		}
		return ownerEntities.get(0);
	}
}
