package com.ehl.itspark.server;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ehl.itspark.common.DateUtils;
import com.ehl.itspark.common.DbUtils;
import com.ehl.itspark.data.entity.AccountEntity;
import com.ehl.itspark.data.entity.CouponEntity;
import com.ehl.itspark.data.entity.OwnerEntity;
import com.ehl.itspark.data.entity.ParkRecordEntity;
import com.ehl.itspark.data.entity.TradeRecordEntity;
import com.ehl.itspark.service.intf.AccountApiService;
import com.ehl.itspark.service.intf.CouponApiService;
import com.ehl.itspark.service.intf.OwnerApiService;
import com.ehl.itspark.service.intf.ParkRecordApiService;
import com.ehl.itspark.service.intf.TradeRecordApiService;


@Service
public class CommonServer {
	@Autowired
	private ParkRecordApiService parkRecordApiService;
	@Autowired
	private TradeRecordApiService tradeRecordApiService;
	@Autowired
	private CouponApiService couponApiService;
	@Autowired
	private AccountApiService accountApiService;
	@Autowired
	private OwnerApiService ownerApiService;
	
	@Autowired
	private DbUtils dbUtils;
	
	private Logger logger=LoggerFactory.getLogger(OwnerServer.class);
	
	/*
	 * 通知支付结果
	 */
	public  void setResult(String subject, Integer paymentPattern,String orderID, String paymentTime, String billID){
		TradeRecordEntity entity = new TradeRecordEntity();
		entity.setFlowNo(orderID);
		List<TradeRecordEntity> tradeRecordEntities = tradeRecordApiService.findTradeRecordsByPage(entity, null, null, null, null).getData();
		if(tradeRecordEntities!=null && tradeRecordEntities.size()>0){
			tradeRecordEntities.get(0).setPayMode(paymentPattern);
			tradeRecordEntities.get(0).setResult(1);
			tradeRecordEntities.get(0).setPaymentTime(DateUtils.StringToDate(paymentTime, "yyyy-MM-dd HH:mm:ss"));
			tradeRecordEntities.get(0).setBillID(billID);
			ParkRecordEntity parkRecordEntity = new ParkRecordEntity();
			if("停车费".equals(subject)){
				List<ParkRecordEntity> parkRecordEntities = parkRecordApiService.findByPage(parkRecordEntity, null, null).getData();
				int couponNum;
				if(parkRecordEntities!=null && parkRecordEntities.size()>0){
					parkRecordEntities.get(0).setPaymentFlag(paymentPattern);
					if(tradeRecordEntities.get(0).getCouponID()!=null && !"".equals(tradeRecordEntities.get(0).getCouponID())){
						CouponEntity couponEntity = new CouponEntity();
						couponEntity.setCouponID(tradeRecordEntities.get(0).getCouponID());
						List<CouponEntity> couponEntities = couponApiService.findCoupons(couponEntity, null, null);
						if(couponEntities!=null && couponEntities.size()>0){
							AccountEntity accountEntity = accountApiService.findAccountByOwnerNo(couponEntities.get(0).getCouponOwner());
							if(accountEntity!=null){
								couponNum = accountEntity.getCoupon()-1;
								couponEntity.setCouponState(2);//"1"表示未用，”2“表示已用，"3"表示过期
								try{
									if(dbUtils.updateAccount(accountEntity.getAccountNo(),null,couponNum,couponEntity)==1){
										dbUtils.updatePaymentResult(tradeRecordEntities.get(0), parkRecordEntities.get(0));
									}
								}catch(Exception e){
									e.printStackTrace();
								}
							}
						}
					}else{
						try{
							dbUtils.updatePaymentResult(tradeRecordEntities.get(0), parkRecordEntities.get(0));
						}catch(Exception e){
							e.printStackTrace();
						}
					}
				}
			}else if("账户充值".equals(subject)){
				OwnerEntity ownerEntity= ownerApiService.findOwnerByAccount(tradeRecordEntities.get(0).getTradeFlag());
				if(ownerEntity!=null){
	   		        AccountEntity accountEntity = accountApiService.findAccountByOwnerNo(ownerEntity.getNo());
					if(accountEntity!=null){
						try{
							accountApiService.updateBalance(accountEntity.getAccountNo(), accountEntity.getBalance()+tradeRecordEntities.get(0).getPaidMoney());
						}catch(Exception e){
							e.printStackTrace();
						}
					}
				}
			}
		}
	}
	
}
	