package com.ehl.itspark.common;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ehl.itspark.data.entity.AccountEntity;
import com.ehl.itspark.data.entity.CarEntity;
import com.ehl.itspark.data.entity.CouponEntity;
import com.ehl.itspark.data.entity.OwnerEntity;
import com.ehl.itspark.data.entity.ParkRecordEntity;
import com.ehl.itspark.data.entity.TradeRecordEntity;
import com.ehl.itspark.service.intf.AccountApiService;
import com.ehl.itspark.service.intf.CarApiService;
import com.ehl.itspark.service.intf.CouponApiService;
import com.ehl.itspark.service.intf.OwnerApiService;
import com.ehl.itspark.service.intf.ParkRecordApiService;
import com.ehl.itspark.service.intf.TradeRecordApiService;

@Service
public class DbUtils {
	
	@Autowired
	private TradeRecordApiService tradeRecordApiService;
	@Autowired
	private ParkRecordApiService parkRecordApiService;
	@Autowired
	private AccountApiService accountApiService;
	@Autowired
	private CouponApiService couponApiService;	
	@Autowired
	private OwnerApiService ownerApiService;
	@Autowired
	private CarApiService carApiService;
	
	/*
	 * 支付成功后更新交易记录、停车记录、优惠券信息
	 */
	@Transactional(rollbackFor={RuntimeException.class, Exception.class})
	public  int updatePaymentResult(TradeRecordEntity tradeRecordEntity, ParkRecordEntity parkRecordEntity){
		try{
			
/*			if(tradeRecordEntity!=null){
				TradeRecordEntity tradeRecord = new TradeRecordEntity();
				tradeRecord.setPayMoney(tradeRecordEntity.getPayMoney());
				tradeRecord.setPaidMoney(tradeRecordEntity.getPaidMoney());
				tradeRecord.setCouponID(tradeRecordEntity.getCouponID());
				tradeRecord.setPayMode(tradeRecordEntity.getPayMode());
				tradeRecord.setPaymentTime(tradeRecordEntity.getPaymentTime());
				tradeRecord.setBillID(tradeRecordEntity.getBillID());
				tradeRecord.setResult(tradeRecordEntity.getResult());
				tradeRecord.setFlowNo(tradeRecordEntity.getFlowNo());
				
				
				if(tradeRecordApiService.updateTradeRecord(tradeRecordEntity)==-1){
					return -1;
				}
			}*/
			if(tradeRecordApiService.updateTradeRecord(tradeRecordEntity)==-1){
				return -1;
			}
			if(parkRecordEntity!=null){
				ParkRecordEntity parkRecord = new ParkRecordEntity();
				parkRecord.setPaymentFlag(parkRecordEntity.getPaymentFlag());
				parkRecord.setFlowNo(parkRecordEntity.getFlowNo());
				if(parkRecordApiService.updateParkRecord(parkRecord)==-1){
					return -1;
				}
			}
			return 1;
		}catch(Exception e){
			e.printStackTrace();
			return -1;
		}
	}
	
	
	/*
	 * 更新账户余额、优惠券数量
	 */
	@Transactional(rollbackFor={RuntimeException.class, Exception.class})
	public int updateAccount(String accountNo,Double balance, Integer couponNum,CouponEntity couponEntity){
		try{
			if("".equals(accountNo) || accountNo==null){
				return -1;
			}
			if(balance!=null){
				if(accountApiService.updateBalance(accountNo, balance)==-1){
					return -1;
				}
			}
			if(couponNum!=null){
				if(accountApiService.updateCoupon(accountNo, couponNum)==-1){
					return -1;
				}
			}
			if(couponEntity!=null){
			     if(couponApiService.updateCoupon(couponEntity)==-1){
			    	 return -1;
			     }
			}
			return 1;
		}catch(Exception e){
			e.printStackTrace();
			return -1;
		}
	}
	
	
	/*
	 * 注册新车主用户
	 */
	@Transactional(rollbackFor={RuntimeException.class, Exception.class})
	public int createAccount(OwnerEntity ownerEntity){
		try{
			if(ownerEntity!=null){
				if(ownerApiService.save(ownerEntity)!=1){
					return -1;
				}
			}
			if(ownerEntity.getNo()!=null && !"".equals(ownerEntity.getNo())){
				AccountEntity accountEntity = new AccountEntity();
				accountEntity.setAccountNo(ownerEntity.getNo());//暂设"账户编号"与“车主编号”一致
				accountEntity.setBalance(1000.0);//暂设初始余额为"1000"进行测试
				accountEntity.setCoupon(100);//暂设初始优惠券为"100张"进行测试
				accountEntity.setType(1);//暂时设定为"1"
				accountEntity.setOwnerNo(ownerEntity.getNo());
				if(accountApiService.save(accountEntity)!=1){
					return -1;
				}
			}
			return 1;
		}catch(Exception e){
			e.printStackTrace();
			return -1;
		}
	}
	
	
	/*
	 * 解绑车辆
	 */
	@Transactional(rollbackFor={RuntimeException.class, Exception.class})
	public int unbindLicensePlate(OwnerEntity ownerEntity, String licensePlateDismiss){
		try{
			if(ownerEntity!=null){
				List<CarEntity> carEntities = carApiService.findCarsByOwnerNo(ownerEntity.getNo());
				if(carEntities!=null && carEntities.size()>0){
					for(int i=0;i < carEntities.size(); i++){
						System.out.println(carEntities.get(i).getPlate() + " " +  licensePlateDismiss);
						if(licensePlateDismiss.equals(carEntities.get(i).getPlate())){
							CarEntity carEntity = carEntities.get(i);
							carEntity.setOwnerNo("");
							if(carApiService.updateByPlate(carEntity)==-1){
								return -1;
							}
							if(ownerApiService.update(ownerEntity)==-1){
								return -1;
							}
						}
					}
				}
			}else{
				return -1;
			}
			return 1;
		}catch(Exception e){
			e.printStackTrace();
			return -1;
		}
	}
	
	
	/*
	 * 绑定车辆
	 */
	@Transactional(rollbackFor={RuntimeException.class, Exception.class})
	public int bindLicensePlate(OwnerEntity ownerEntity, String licensePlateBind,String carType){
		try{
			if(ownerEntity!=null){
				List<CarEntity> carEntities = carApiService.findCarsByPlateAndType(licensePlateBind, carType);
				CarEntity carEntity = new CarEntity();
				if(carEntities!=null && carEntities.size()>0){
					for(int i=0;i < carEntities.size(); i++){
						carEntity = carEntities.get(i);
						if(carEntity.getOwnerNo()==null || "".equals(carEntity.getOwnerNo())){
							carEntity.setOwnerNo(ownerEntity.getNo());
							if(carApiService.updateByPlate(carEntity)==-1){
							    return -1;
							}
							if(ownerApiService.update(ownerEntity)==-1){
								return -1;
							}
						}else{
							return -1;
						}
					}
				}else{
					carEntity.setPlate(licensePlateBind);
					carEntity.setCarType(carType);
					carEntity.setOwnerNo(ownerEntity.getNo());
					if(carApiService.save(carEntity)==-1){
					    return -1;
					}
					if(ownerApiService.update(ownerEntity)==-1){
						return -1;
					}
				}
			}else{
				return -1;
			}
			return 1;
		}catch(Exception e){
			e.printStackTrace();
			return -1;
		}
	}
}
