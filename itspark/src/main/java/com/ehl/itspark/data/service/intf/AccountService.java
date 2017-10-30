package com.ehl.itspark.data.service.intf;

import java.util.List;

import com.ehl.itspark.data.entity.AccountEntity;


public interface AccountService {

	List<AccountEntity> findAccounts(AccountEntity entity);
	
	AccountEntity findAccountByAccountNo(String accountNo);
	
	AccountEntity findAccountByOwnerNo(String ownerNo);
	
	int save(AccountEntity entity)throws Exception;
	
	int update(AccountEntity entity)throws Exception;
	
	int updateBalance(String accountNo,double balance)throws Exception;
	
	int updateCoupon(String accountNo,Integer coupon)throws Exception;
}
