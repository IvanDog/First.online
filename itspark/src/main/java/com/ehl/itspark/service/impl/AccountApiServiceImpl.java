package com.ehl.itspark.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ehl.itspark.data.entity.AccountEntity;
import com.ehl.itspark.data.service.intf.AccountService;
import com.ehl.itspark.service.intf.AccountApiService;



@Service
public class AccountApiServiceImpl implements AccountApiService{

	@Autowired
	private AccountService accountService;
	
	@Override
	public List<AccountEntity> findAccounts(AccountEntity entity) {
		// TODO Auto-generated method stub
		return accountService.findAccounts(entity);
	}
	
	@Override
	public AccountEntity findAccountByAccountNo(String accountNo) {
		// TODO Auto-generated method stub
		return accountService.findAccountByAccountNo(accountNo);
	}
	
	@Override
	public AccountEntity findAccountByOwnerNo(String ownerNo) {
		// TODO Auto-generated method stub
		return accountService.findAccountByOwnerNo(ownerNo);
	}

	@Override
	public int updateBalance(String accountNo,double balance)throws Exception{
		// TODO Auto-generated method stub
		return accountService.updateBalance(accountNo, balance);
	}
	
	@Override
	public int updateCoupon(String accountNo,Integer coupon)throws Exception{
		// TODO Auto-generated method stub
		return accountService.updateCoupon(accountNo, coupon);
	}
	
	@Override
	public int save(AccountEntity entity)throws Exception{
		return accountService.save(entity);
	}

}
