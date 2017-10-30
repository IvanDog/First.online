package com.ehl.itspark.service.intf;

import java.util.List;

import com.ehl.itspark.data.entity.AccountEntity;


public interface AccountApiService {

	List<AccountEntity> findAccounts(AccountEntity entity);
	
	AccountEntity findAccountByAccountNo(String accountNo);
	
	AccountEntity findAccountByOwnerNo(String ownerNo);
	
	int updateBalance(String accountNo,double balance)throws Exception;
	
	int updateCoupon(String accountNo,Integer coupon)throws Exception;

	int save(AccountEntity entity)throws Exception;
}
