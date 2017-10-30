package com.ehl.itspark.data.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.ehl.itspark.common.MyBatisRepository;
import com.ehl.itspark.data.entity.AccountEntity;

@MyBatisRepository
public interface AccountDao {

int save(AccountEntity entity)throws Exception;
	
	int updateByAccountNo(AccountEntity entity)throws Exception;
	
	int updateBalance(@Param("accountNo")String accountNo,@Param("balance")double balance)throws Exception;
	
	int updateCoupon(@Param("accountNo")String accountNo,@Param("coupon")Integer coupon)throws Exception;
	
	int deleteByAccountNo(@Param("accountNo")String accountNo);
	
	List<AccountEntity> findAll(Map<String, Object> para);
	
	long count(Map<String, Object> para);
}
