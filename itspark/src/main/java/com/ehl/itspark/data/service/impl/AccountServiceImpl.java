package com.ehl.itspark.data.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ehl.itspark.data.dao.AccountDao;
import com.ehl.itspark.data.entity.AccountEntity;
import com.ehl.itspark.data.service.intf.AccountService;


@Service
@Transactional
public class AccountServiceImpl implements AccountService{

	@Autowired
	private AccountDao accountDao;
	@Override
	public List<AccountEntity> findAccounts(AccountEntity entity) {
		// TODO Auto-generated method stub
		Map<String, Object> para=new HashMap<>();
		if(entity != null){
			para.put("accountNo", entity.getAccountNo());
			para.put("ownerNo", entity.getOwnerNo());
		}
		List<AccountEntity> entities= accountDao.findAll(para);
		if(entities==null||entities.size()==0){
			return null;
		}
		return entities;
	}
	
	@Override
	public AccountEntity findAccountByAccountNo(String accountNo) {
		// TODO Auto-generated method stub
		Map<String, Object> para=new HashMap<>();
		para.put("accountNo", accountNo);
		List<AccountEntity> entities= accountDao.findAll(para);
		if(entities==null||entities.size()==0){
			return null;
		}
		return entities.get(0);
	}

	@Override
	public AccountEntity findAccountByOwnerNo(String ownerNo) {
		// TODO Auto-generated method stub
		Map<String, Object> para=new HashMap<>();
		para.put("ownerNo", ownerNo);
		List<AccountEntity> entities= accountDao.findAll(para);
		if(entities==null||entities.size()==0){
			return null;
		}
		return entities.get(0);
	}

	@Override
	public int save(AccountEntity entity) throws Exception {
		// TODO Auto-generated method stub
		return accountDao.save(entity);
	}

	@Override
	public int update(AccountEntity entity) throws Exception {
		// TODO Auto-generated method stub
		return accountDao.updateByAccountNo(entity);
	}

	@Override
	public int updateBalance(String accountNo, double balance) throws Exception {
		// TODO Auto-generated method stub
		return accountDao.updateBalance(accountNo, balance);
	}

	@Override
	public int updateCoupon(String accountNo, Integer coupon) throws Exception {
		// TODO Auto-generated method stub
		return accountDao.updateCoupon(accountNo, coupon);
	}

}
