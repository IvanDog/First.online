package com.ehl.itspark.server;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ehl.itspark.common.AlipayPayment;
import com.ehl.itspark.common.CommonResponse;
import com.ehl.itspark.common.DateUtils;
import com.ehl.itspark.common.DbUtils;
import com.ehl.itspark.common.EhcacheUtil;
import com.ehl.itspark.common.LocationUtils;
import com.ehl.itspark.common.PaymentConvertUtils;
import com.ehl.itspark.common.WechatPayment;
import com.ehl.itspark.data.entity.AccountEntity;
import com.ehl.itspark.data.entity.CarEntity;
import com.ehl.itspark.data.entity.CouponEntity;
import com.ehl.itspark.data.entity.FeeRateEntity;
import com.ehl.itspark.data.entity.MessageEntity;
import com.ehl.itspark.data.entity.OwnerEntity;
import com.ehl.itspark.data.entity.ParkEnterEntity;
import com.ehl.itspark.data.entity.ParkEntity;
import com.ehl.itspark.data.entity.ParkLotEntity;
import com.ehl.itspark.data.entity.ParkRecordEntity;
import com.ehl.itspark.data.entity.TradeRecordEntity;
import com.ehl.itspark.info.AnalysisInfo;
import com.ehl.itspark.info.BindLicenseInfo;
import com.ehl.itspark.info.FeedBackInfo;
import com.ehl.itspark.info.LoginInfo;
import com.ehl.itspark.info.LogoutInfo;
import com.ehl.itspark.info.PaymentInfo;
import com.ehl.itspark.info.QueryBalanceInfo;
import com.ehl.itspark.info.QueryCouponInfo;
import com.ehl.itspark.info.QueryLicenseInfo;
import com.ehl.itspark.info.QueryRecordInfo;
import com.ehl.itspark.info.QueryResultInfo;
import com.ehl.itspark.info.QueryUserInfo;
import com.ehl.itspark.info.RechargeInfo;
import com.ehl.itspark.info.RecordSearchInfo;
import com.ehl.itspark.info.RegisterInfo;
import com.ehl.itspark.info.ResetPasswdInfo;
import com.ehl.itspark.info.SearchParkInfo;
import com.ehl.itspark.info.SetHeadPortraitInfo;
import com.ehl.itspark.info.SetNickNameInfo;
import com.ehl.itspark.info.SetPaymentPasswdInfo;
import com.ehl.itspark.info.SettleAccountInfo;
import com.ehl.itspark.info.TokenInfo;
import com.ehl.itspark.info.UnBindLicenseInfo;
import com.ehl.itspark.service.intf.AccountApiService;
import com.ehl.itspark.service.intf.CarApiService;
import com.ehl.itspark.service.intf.CouponApiService;
import com.ehl.itspark.service.intf.FeeApiService;
import com.ehl.itspark.service.intf.MessageApiService;
import com.ehl.itspark.service.intf.OwnerApiService;
import com.ehl.itspark.service.intf.ParkApiService;
import com.ehl.itspark.service.intf.ParkEnterApiService;
import com.ehl.itspark.service.intf.ParkLotApiService;
import com.ehl.itspark.service.intf.ParkRecordApiService;
import com.ehl.itspark.service.intf.TradeRecordApiService;


@Service
public class OwnerServer {
	@Autowired
	private OwnerApiService ownerApiService;
	@Autowired
	private AccountApiService accountApiService;
	@Autowired
	private CarApiService carApiService;
	@Autowired
	private ParkRecordApiService parkRecordApiService;
	@Autowired
	private ParkEnterApiService parkEnterApiService;
	@Autowired
	private ParkLotApiService parkLotApiService;
	@Autowired
	private TradeRecordApiService tradeRecordApiService;
	@Autowired
	private ParkApiService parkApiService;
	@Autowired
	private CouponApiService couponApiService;
	@Autowired
	private FeeApiService feeApiService;
	@Autowired
	private MessageApiService messageApiService;
	
	@Autowired
	private DbUtils dbUtils;
	
	private Logger logger=LoggerFactory.getLogger(OwnerServer.class);
	
	public static final int TYPE_CURRENT_RECORD = 101;
	public static final int TYPE_HISTORY_RECORD = 102;
	
	
	/*
	 * 查询用户当前停车信息
	 */
	public CommonResponse queryRecordInfo(QueryRecordInfo info) throws Exception{
        System.out.println(info.toString());
		CommonResponse res=new CommonResponse();
		if(info==null||info.getheader()==null || info.getheader().getToken()==null||"".equals(info.getheader().getToken())){
			res.setResult("201", "参数为空");
			return res;
		}
		if(!EhcacheUtil.isValidKey(info.getheader().getToken())){
			res.setResult("202", "登陆超时，请重新登陆");
			return res;
		}
		if(info.getheader().getAccount()==null || "".equals(info.getheader().getAccount())){
			res.setResult("100", "获取车主订单成功");
			return res;
		}else{
			OwnerEntity ownerEntity= ownerApiService.findOwnerByAccount(info.getheader().getAccount());
			List<CarEntity> carEntities = carApiService.findCarsByOwnerNo(ownerEntity.getNo());
			if(carEntities!=null && carEntities.size()>0){
				String plate = null;
				String carType = null;
				for(int i=0;i<carEntities.size();i++){
					plate = carEntities.get(i).getPlate();
					carType = carEntities.get(i).getCarType();
					setParkRecord(res,plate,carType,0);//查询车主“未付”订单
					setParkRecord(res,plate,carType,9);//查询车主“逃费”订单
				}
			}else{
				res.setResult("100", "获取车主订单成功");
				return res;
			}
		}
		return res;
	}
	
	
	/*
	 * 查询应付金额、车辆类别、停车类别、开始时间
	 */
	public CommonResponse settleAccount(SettleAccountInfo info){
        System.out.println(info.toString());
		CommonResponse res = new CommonResponse();
		if(info==null||info.getheader()==null || info.getheader().getToken()==null||"".equals(info.getheader().getToken()) || info.getParkNumber()==null || "".equals(info.getParkNumber()) 
				|| info.getLicensePlateNumber()==null || "".equals(info.getLicensePlateNumber()) || info.getCarType()==null || "".equals(info.getCarType())){
			res.setResult("201", "参数为空");
			return res;
		}
		if(!EhcacheUtil.isValidKey(info.getheader().getToken())){
			res.setResult("202", "登陆超时，请重新登陆");
			return res;
		}
		
		class Inner{
			void setResponse(SettleAccountInfo info,CommonResponse res,ParkRecordEntity parkRecordEntity){
				res.getProperty().put("parkType", parkRecordEntity.getParkType());
				res.getProperty().put("startTime", DateUtils.DateToString(parkRecordEntity.getEnterTime(), "yyyy-MM-dd HH:mm:ss"));
			    res.getProperty().put("parkingRecordID", String.valueOf(parkRecordEntity.getFlowNo()));
			    res.getProperty().put("tradeRecordID", parkRecordEntity.getTradeFlowNo());
			    res.getProperty().put("expensePrimary", String.valueOf(parkRecordEntity.getPayMoney()));
			    Double discount = 0.0;
			    if(!"".equals(info.getCouponID()) && info.getCouponID()!=null){
			    	CouponEntity couponEntity = new CouponEntity();
			    	couponEntity.setCouponID(info.getCouponID());
			    	List<CouponEntity> couponEntities = couponApiService.findCoupons(couponEntity,null,null);
			    	if(couponEntities!=null && couponEntities.size()!=0){
			    		discount = couponEntities.get(0).getDenomination();
			    	}
			    }
			    res.getProperty().put("discount", String.valueOf(discount));
			    res.getProperty().put("expenseFinal", String.valueOf(parkRecordEntity.getPayMoney()-discount));
			    FeeRateEntity feeRateEntity=feeApiService.findByParkNo(parkRecordEntity.getParkNo());
			    if(feeRateEntity  != null){
	        		if(feeRateEntity.getType()==1){//按次收费
	    			    res.getProperty().put("feeScale", feeRateEntity.getFeeByCount().doubleValue() + "元/次");
	        		}else if(feeRateEntity.getType()==2){//按小时收费
	    			    res.getProperty().put("feeScale", feeRateEntity.getFeeByTime().doubleValue() + "元/时" + "(前" + feeRateEntity.getFreeTimeLen() + "分钟免费)");
	        		}else{
	        			res.getProperty().put("feeScale", "未知");
	        		}
			    }else{
			    	res.getProperty().put("feeScale", "未知");
			    }
				res.setResult("100", "结算信息获取成功");
			}
		}
		
		ParkRecordEntity parkRecordEntity = new ParkRecordEntity();
		Inner inner = new Inner();
		if(!"".equals(info.getParkingEnterID()) && info.getParkingEnterID()!=null){
			//将入场表中的信息转移至停车记录表中
			List<ParkEnterEntity> parkEnterEntities = parkEnterApiService.findByParkNoAndFlowNo(info.getParkNumber(), info.getParkingEnterID());
			System.out.println("settleAccount->parkEnterEntities is" + parkEnterEntities);
			if(parkEnterEntities!=null && parkEnterEntities.size()!=0){
				ParkEnterEntity parkEnterEntity = parkEnterEntities.get(0);
				parkRecordEntity.setFlag(0);//“0”表示正常，”1“表示无入场记录
				parkRecordEntity.setFlowNo(DateUtils.DateToString(new Date(), "yyyyMMddHHmmss"));
				parkRecordEntity.setParkNo(parkEnterEntity.getParkNo());
				parkRecordEntity.setPlate(parkEnterEntity.getPlate());
				parkRecordEntity.setParkLot(parkEnterEntity.getParkLot());
				parkRecordEntity.setCarType(parkEnterEntity.getCarType());
				parkRecordEntity.setParkType(parkEnterEntity.getParkType());
				parkRecordEntity.setEnterTime(parkEnterEntity.getEnterTime());
				parkRecordEntity.setEnterImg(parkEnterEntity.getImg());
				Date enterTime = parkRecordEntity.getEnterTime();
				Date leaveTime = DateUtils.StringToDate(info.getLeaveTime(), "yyyy-MM-dd HH:mm:ss");
				parkRecordEntity.setLeaveTime(leaveTime);
				parkRecordEntity.setLeaveImg(info.getLeaveImage());
				int duringHours = (int)( (leaveTime.getTime()-enterTime.getTime())/(1000*60*60)) + 1;
				parkRecordEntity.setPayMoney(5*duringHours);//停车单价暂时按5元/时计算
				parkRecordEntity.setPaymentFlag(0);
				    //"0"表示未支付，”1“表示pos机支付，"2"表示微信支付，”3“表示支付支付，"4"表示微信扫码支付，”5“表示支付宝扫码支付，"6"表示微信刷卡支付，”７“表示支付宝刷卡支付，”8“表示余额支付，“9”表示逃费
				try{
					if(parkRecordApiService.saveParkRecord(parkRecordEntity)==1){//如果插入成功
						//删除入场记录
						parkEnterApiService.deleteByParkNoAndFlowNo(info.getParkNumber(), info.getParkingEnterID());
						//更新泊位状态
						ParkLotEntity parkLotEntity = new ParkLotEntity();
						parkLotEntity.setParkNo(info.getParkNumber());
						parkLotEntity.setNo(parkEnterEntity.getParkLot());
						List<ParkLotEntity> parkLotEntities = parkLotApiService.findParkLotByPage(parkLotEntity, null, null, null, null).getData();
						System.out.println("settleAccount->parkLotEntities is " + parkLotEntities);
						if(parkLotEntities!=null && parkLotEntities.size()!=0){
							parkLotEntities.get(0).setStatus(0);
							parkLotEntities.get(0).setPlate(info.getLicensePlateNumber());
							parkLotEntities.get(0).setUpdateTime(DateUtils.StringToDate(info.getLeaveTime(), "yyyy-MM-dd HH:mm:ss"));
					    	parkLotApiService.updateParkLot(parkLotEntities.get(0));  
						}
						inner.setResponse(info, res, parkRecordEntity);
					}else{
						res.setResult("203", "结算信息获取失败");
					}
				}catch(Exception e){
					e.printStackTrace();
					res.setResult("203", "结算信息获取失败");
				}		
			}else{
				res.setResult("203", "结算信息获取失败");
			}
		}else if(!"".equals(info.getParkingRecordID()) && info.getParkingRecordID()!=null){
			    List<ParkRecordEntity> parkRecordEntities = parkRecordApiService.findByParkNoAndFlowNo(info.getParkNumber(), info.getParkingRecordID());
			    if(parkRecordEntities!=null && parkRecordEntities.size()!=0){
				    parkRecordEntity = parkRecordEntities.get(0);
					inner.setResponse(info, res, parkRecordEntity);
			    }else{
			    	res.setResult("203", "结算信息获取失败");
			    }
		}else{
			res.setResult("201", "参数为空");
		}
		return res;
	}
	
	
	/*
	 * 查询车主可用优惠券
	 */
	public  CommonResponse queryCoupon(QueryCouponInfo info){
		System.out.println(info.toString());
		CommonResponse res = new CommonResponse();
		if(info==null||info.getheader().getToken()==null||"".equals(info.getheader().getToken())){
			res.setResult("201", "参数为空");
			return res;
		}
		if(!EhcacheUtil.isValidKey(info.getheader().getToken())){
			res.setResult("202", "登陆超时，请重新登陆");
			return res;
		}
		int resultFlag = 0;
    	if(info.getheader().getAccount()==null || "".equals(info.getheader().getAccount())){
    		resultFlag = 1;
    	}else{
    		CouponEntity couponEntity = new CouponEntity();
        	OwnerEntity ownerEntity= ownerApiService.findOwnerByAccount(info.getheader().getAccount());
        	if(ownerEntity!=null){
        		couponEntity.setCouponOwner(ownerEntity.getNo());
        		List<CouponEntity> couponEntities;
        		if(info.getExpensePrimary()==null){
            	    couponEntities = couponApiService.findCoupons(couponEntity,null,new Date());
        		}else{
            		couponEntities = couponApiService.findCoupons(couponEntity,Double.valueOf(info.getExpensePrimary()),new Date());
        		}
        		HashMap<String,Object> map = new HashMap<String,Object>();
        		System.out.println("couponEntities is " + couponEntities);
        		if(couponEntities!=null){
            		for(int i=0;i<couponEntities.size();i++){
            			if(couponEntities.get(i).getCouponState()==1){
                			map.put("couponID",  couponEntities.get(i).getCouponID());
                            map.put("couponTitle",  couponEntities.get(i).getCouponTitle());
                            map.put("couponStartTime", DateUtils.DateToString(couponEntities.get(i).getStartTime(), "yyyy-MM-dd HH:mm:ss"));
                            map.put("couponEndTime", DateUtils.DateToString(couponEntities.get(i).getEndTime(), "yyyy-MM-dd HH:mm:ss"));
                            map.put("couponNotify", couponEntities.get(i).getCouponNotify());
                            map.put("couponDenomination", couponEntities.get(i).getDenomination());
                            map.put("couponDetail", couponEntities.get(i).getCouponDetail());
                            res.addListItem(map);
            			}
            		}
            		res.setResult("100", "查询优惠券信息成功");
            		resultFlag = 1;
        		}
        	}
    	}
		if(resultFlag == 1){
    		res.setResult("100", "查询优惠券信息成功");
		}else{
			res.setResult("203", "查询优惠券信息失败");
		}
		return res;
	}
	
	
	/*
	 * 生成支付订单
	 */
	public CommonResponse pay(PaymentInfo info){
        System.out.println(info.toString());
		CommonResponse res = new CommonResponse();
		if(info==null||info.getheader()==null || info.getheader().getToken()==null||"".equals(info.getheader().getToken()) || info.getParkNumber()==null || "".equals(info.getParkNumber()) || info.getLicensePlateNumber()==null || "".equals(info.getLicensePlateNumber())
				|| info.getParkingRecordID()==null || "".equals(info.getParkingRecordID()) || info.getPaidMoney()==null || "".equals(info.getPaidMoney()) || info.getPaymentPattern()==null){
			res.setResult("201", "参数为空");
			return res;
		}
		if(!EhcacheUtil.isValidKey(info.getheader().getToken())){
			res.setResult("202", "登陆超时，请重新登陆");
			return res;
		}
		
		class EscapeThread extends Thread{
			private String parkNo;
			private String parkingRecordID;
			private String tradeRecordID;
			public EscapeThread(String parkNo,String parkingRecordID, String tradeRecordID){
				this.parkNo = parkNo;
				this.parkingRecordID = parkingRecordID;
				this.tradeRecordID = tradeRecordID;
			}
			@Override
			public void run(){
				int excapeFlag;
				try{
					sleep(2*60*60*1000);
					List<ParkRecordEntity> parkRecordEntities = parkRecordApiService.findByParkNoAndFlowNo(parkNo, parkingRecordID);
					if(parkRecordEntities!=null && parkRecordEntities.size()>=0){
						 ParkRecordEntity parkRecordEntity = parkRecordEntities.get(0);
							if(parkRecordEntity.getPaymentFlag()==0){
								excapeFlag = 1;
								List<CarEntity> carEntities = carApiService.findCarsByPlateAndType(parkRecordEntity.getPlate(), parkRecordEntity.getCarType());
								if(carEntities!=null && carEntities.size()>0){
									CarEntity carEntity = carEntities.get(0);
									if(carEntity!=null){
										AccountEntity accountEntity = accountApiService.findAccountByOwnerNo(carEntity.getOwnerNo());
										if(accountEntity!=null){
											String accountNo = accountEntity.getAccountNo();
							   			    double balance = accountEntity.getBalance() - parkRecordEntity.getPayMoney();
			                                if(balance>=0){
					   					       if(dbUtils.updateAccount(accountNo,balance,null,null)==1){//更新成功
					   					    	    TradeRecordEntity updateTradeRecord = new TradeRecordEntity();
					   					    	    updateTradeRecord.setPayMoney(parkRecordEntity.getPayMoney());
					   					    	    updateTradeRecord.setPaidMoney(parkRecordEntity.getPayMoney());
					   					    	    updateTradeRecord.setCouponID(null);
					   					    	    updateTradeRecord.setPayMode(8);
					   					    	    updateTradeRecord.setResult(1);//"0"表示待收，”1“表示已收
					   					    	    updateTradeRecord.setFlowNo(tradeRecordID);
					   					    	    
					   					    	    ParkRecordEntity updateParkRecord =new ParkRecordEntity();
					   					    	    updateParkRecord.setParkNo(parkNo);
					   					    	    updateParkRecord.setFlowNo(parkingRecordID);
					   					    	    updateParkRecord.setPaymentFlag(8);
					   						        //"0"表示未支付，”1“表示pos机支付，"2"表示微信支付，”3“表示支付支付，"4"表示微信扫码支付，”5“表示支付宝扫码支付，"6"表示微信刷卡支付，”７“表示支付宝刷卡支付，”8“表示余额支付，“9”表示逃费，“10”表示其他支付
					   						        if(dbUtils.updatePaymentResult(updateTradeRecord,updateParkRecord)==1){
					   						        	excapeFlag = 0;
					   						        }
						   				        }
										    }
									    }
									}
								}
								if(excapeFlag == 1){
	   					    	    TradeRecordEntity updateTradeRecord = new TradeRecordEntity();
	   					    	    updateTradeRecord.setPayMoney(parkRecordEntity.getPayMoney());
	   					    	    updateTradeRecord.setPaidMoney(parkRecordEntity.getPayMoney());
	   					    	    updateTradeRecord.setCouponID(null);
	   					    	    updateTradeRecord.setPayMode(9);
	   					    	    updateTradeRecord.setResult(0);//"0"表示待收，”1“表示已收
	   					    	    updateTradeRecord.setFlowNo(tradeRecordID);
	   					    	    
	   					    	    ParkRecordEntity updateParkRecord =new ParkRecordEntity();
	   					    	    updateParkRecord.setParkNo(parkNo);
	   					    	    updateParkRecord.setFlowNo(parkingRecordID);
	   					    	    updateParkRecord.setPaymentFlag(9);//
				                    dbUtils.updatePaymentResult(updateTradeRecord,updateParkRecord);	
								}
							}
					}
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}
		List<ParkRecordEntity> parkRecordEntities = parkRecordApiService.findByParkNoAndFlowNo(info.getParkNumber(), info.getParkingRecordID());
		/*if(parkRecordEntities!=null){
			if(parkRecordEntities.get(0).getPayMoney()==0){
				try{
					parkRecordEntities.get(0).setPaymentFlag(10);//其他支付
					parkRecordApiService.updateParkRecord(parkRecordEntities.get(0));
					res.setResult("100", "无需支付");
					return res;
				}catch(Exception e){
					res.setResult("204", "生成支付订单失败");
					return res;
				}
			}
		}*/
		//生成新的交易记录
		TradeRecordEntity tradeRecordEntity = new TradeRecordEntity();
		boolean isFirstOrdered = false;
		if(!"".equals(info.getTradeRecordID()) && info.getTradeRecordID()!=null){
			TradeRecordEntity entity = new TradeRecordEntity();
			entity.setFlowNo(info.getTradeRecordID());
			List<TradeRecordEntity> tradeRecordEntities = tradeRecordApiService.findTradeRecordsByPage(tradeRecordEntity, null, null, null, null).getData();
			if(tradeRecordEntities!=null && tradeRecordEntities.size()>0){
				tradeRecordEntity= tradeRecordEntities.get(0);
				if(tradeRecordEntity.getResult()==1){
					res.setResult("100", "订单已支付");
				}
			}
			System.out.println("isFirstOrdered1 is" + isFirstOrdered);
		}else{
			tradeRecordEntity.setFlowNo(DateUtils.DateToString(new Date(), "yyyyMMddHHmmss"));
			tradeRecordEntity.setIndustryFlag("1");//暂时将行业标识记录为“１”
			tradeRecordEntity.setTradeType(1);//"支付"类别为"1"，“充值”类别为“２”
			tradeRecordEntity.setTradeFlag(info.getLicensePlateNumber());
			tradeRecordEntity.setServiceFlow(info.getParkingRecordID());
			tradeRecordEntity.setServiceEntityFlow(info.getParkNumber());
			tradeRecordEntity.setPayMode(0);
			tradeRecordEntity.setResult(0);//"0"表示待收，”1“表示已收
			tradeRecordEntity.setOrderTime(new Date());
			tradeRecordEntity.setOrderTimeStr(new Date().toString());
		    isFirstOrdered = true;
		    System.out.println("isFirstOrdered2  is" + isFirstOrdered);
		}
		tradeRecordEntity.setCouponID(info.getCouponID());//将目前选中的优惠券编号写入交易记录
		if(parkRecordEntities!=null && parkRecordEntities.size()==0){
			  res.setResult("203", "不存在对应停车记录");
		 }else{
		    ParkRecordEntity parkRecordEntity = parkRecordEntities.get(0);
		     try{
		    	 System.out.println("isFirstOrdered3 is" + isFirstOrdered);
					if(isFirstOrdered){
						tradeRecordApiService.save(tradeRecordEntity);
						ParkRecordEntity parkRecord = new  ParkRecordEntity();
						parkRecord.setFlowNo(info.getParkingRecordID());
						parkRecord.setTradeFlowNo(tradeRecordEntity.getFlowNo());
						parkRecordApiService.updateParkRecord(parkRecord);
					}
				    double paidMoney = Double.parseDouble(info.getPaidMoney());
				    double discount = 0.0;
				    if(!"".equals(info.getCouponID()) && info.getCouponID()!=null){
				    	CouponEntity couponEntity = new CouponEntity();
				    	couponEntity.setCouponID(info.getCouponID());
				    	List<CouponEntity> couponEntities = couponApiService.findCoupons(couponEntity,null,null);
				    	if(couponEntities!=null && couponEntities.size()!=0){
				    		discount = couponEntities.get(0).getDenomination();
				    	}    	    	
				    }
				    if(parkRecordEntity.getPayMoney()==paidMoney+discount){
						TradeRecordEntity updateEntity = new TradeRecordEntity();
						updateEntity.setPayMoney(parkRecordEntity.getPayMoney());
						updateEntity.setPaidMoney(paidMoney);
						updateEntity.setCouponID(info.getCouponID());
						updateEntity.setFlowNo(tradeRecordEntity.getFlowNo());
						tradeRecordApiService.updateTradeRecord(updateEntity);
						new EscapeThread(parkRecordEntity.getParkNo(), parkRecordEntity.getFlowNo(), tradeRecordEntity.getFlowNo()).start();//开启线程在２ｈ后处理逃费情况
						System.out.println("pay->EscapeThread starts !");
		                if(info.getPaymentPattern().equals(2)){//微信支付	
		                	WechatPayment wechatPayment = new WechatPayment();
		                	HashMap<String,Object> map = wechatPayment.pay(2, info.getIP(),(int)(Double.valueOf(info.getPaidMoney()).doubleValue()*100), "停车费", info.getTradeRecordID());
							res.getProperty().put("appid", map.get("appid"));
							res.getProperty().put("partnerid", map.get("partnerid"));
							res.getProperty().put("prepay_id", map.get("prepay_id"));
							res.getProperty().put("package", map.get("package"));
							res.getProperty().put("noncestr", map.get("noncestr"));
							res.getProperty().put("timestamp", map.get("timestamp"));
							res.getProperty().put("sign", map.get("sign"));
							res.setResult("100", "生成支付订单成功");
					    }else if(info.getPaymentPattern().equals(3)){//支付宝支付
					    	AlipayPayment alipayPayment = new AlipayPayment();
					    	String signedOrderInfo;
					    	if(isFirstOrdered){
					    		signedOrderInfo = alipayPayment.appPay(info.getPaidMoney(), "停车费",tradeRecordEntity.getFlowNo());
					    	}else{
					    		signedOrderInfo = alipayPayment.appPay(info.getPaidMoney(), "停车费", info.getTradeRecordID());
					    	}
					    	res.getProperty().put("signedOrderInfo", signedOrderInfo);
					    	res.setResult("100", "生成支付订单成功");
					    }else if(info.getPaymentPattern().equals(8)){//余额支付
					    	int accountStateFlag = 0;//"0"代表账户信息有误，“1”表示账户信息无误
							if(info.getheader().getAccount()!=null && !"".equals(info.getheader().getAccount())){
						    	OwnerEntity ownerEntity= ownerApiService.findOwnerByAccount(info.getheader().getAccount());
								if(ownerEntity!=null){
					   		        AccountEntity accountEntity = accountApiService.findAccountByOwnerNo(ownerEntity.getNo());
									if(accountEntity!=null){
										accountStateFlag = 1;
						   			    String accountNo = accountEntity.getAccountNo();
						   			    int couponNum = accountEntity.getCoupon();
						   			    if(!"".equals(info.getCouponID()) && info.getCouponID()!=null){
						   			    	couponNum = couponNum - 1;
						   			    }
						   			    double balance = accountEntity.getBalance() - paidMoney;
					   				    if(!info.getPassword().equals(ownerEntity.getPayPword())){
					   					    res.setResult("205", "支付密码错误");
					   				    }else if(balance<0){
					   					    res.setResult("206", "余额不足");
					   				    }else{
										    	CouponEntity couponEntity = new CouponEntity();
										    	couponEntity.setCouponID(info.getCouponID());
										    	couponEntity.setCouponState(2);//"1"表示未用，”2“表示已用，"3"表示过期
					   					    if(dbUtils.updateAccount(accountNo,balance,couponNum,couponEntity)==1){//更新成功
					   						    tradeRecordEntity.setResult(1);//"0"表示待收，”1“表示已收
					   						    tradeRecordEntity.setPayMode(8);
					   						    
					   						    tradeRecordEntity.setPaymentTime(new Date());
					   						    parkRecordEntity.setPaymentFlag(8);
					   						    //"0"表示未支付，”1“表示pos机支付，"2"表示微信支付，”3“表示支付支付，"4"表示微信扫码支付，”5“表示支付宝扫码支付，"6"表示微信刷卡支付，”７“表示支付宝刷卡支付，”8“表示余额支付，“9”表示逃费，“10”表示其他支付
					   						    if(dbUtils.updatePaymentResult(tradeRecordEntity,parkRecordEntity)==1){
					   						    	res.setResult("100", "余额支付成功");
					   						    }else{
					   						    	res.setResult("207", "余额支付失败");
					   						    }
					   					    }else{
					   						    res.setResult("207", "余额支付失败");
					   					    }
					   				    }
									}
								}
							}
							if(accountStateFlag==0){
								res.setResult("207", "余额支付失败");
							}
					    }
				     }else{
				    	 res.setResult("204", "生成支付订单失败");
				     }
		     }catch(Exception e){
		    	 e.printStackTrace();
		    	 res.setResult("204", "生成支付订单失败");
		     }
		  }
		return res;
	}
	
	
	/*
	 * 查询车主账户余额
	 */
	public  CommonResponse queryBalance(QueryBalanceInfo info){
		System.out.println(info.toString());
		CommonResponse res = new CommonResponse();
		if(info==null||info.getheader()==null || info.getheader().getToken()==null||"".equals(info.getheader().getToken())){
			res.setResult("201", "参数为空");
			return res;
		}
		if(!EhcacheUtil.isValidKey(info.getheader().getToken())){
			res.setResult("202", "登陆超时，请重新登陆");
			return res;
		}
		int resultFlag = 0;
		if(info.getheader().getAccount()!=null && !"".equals(info.getheader().getAccount())){
	    	OwnerEntity ownerEntity= ownerApiService.findOwnerByAccount(info.getheader().getAccount());
	    	if(ownerEntity!=null){
	    		AccountEntity accountEntity = accountApiService.findAccountByOwnerNo(ownerEntity.getNo());
	    		if(accountEntity!=null){
	    			res.getProperty().put("accountBalance", accountEntity.getBalance());
	    			resultFlag = 1;
	    		}
	    	}
		}else{
			res.getProperty().put("accountBalance", 0);
			resultFlag = 1;
		}
		if(resultFlag == 1){
    		res.setResult("100", "获取账户余额信息成功");
		}else{
			res.setResult("203", "获取账户余额信息失败");
		}
		return res;
	}
	
	
	/*
	 * 车主账户充值
	 */
	public  CommonResponse recharge(RechargeInfo info){
		System.out.println(info.toString());
		CommonResponse res = new CommonResponse();
		if(info==null||info.getheader()==null || info.getheader().getToken()==null||"".equals(info.getheader().getToken()) ||info.getheader().getAccount()==null || "".equals(info.getheader().getAccount())
				|| info.getPaymentPattern()==null || info.getRecharge()==null || "".equals(info.getRecharge())){
			res.setResult("201", "参数为空");
			return res;
		}
		if(!EhcacheUtil.isValidKey(info.getheader().getToken())){
			res.setResult("202", "登陆超时，请重新登陆");
			return res;
		}
		OwnerEntity ownerEntity= ownerApiService.findOwnerByAccount(info.getheader().getAccount());
		List<CarEntity> carEntities = carApiService.findCarsByOwnerNo(ownerEntity.getNo());
		if(carEntities!=null && carEntities.size()>0){
			String plate = null;
			String carType = null;
			for(int i=0;i<carEntities.size();i++){
				plate = carEntities.get(i).getPlate();
				carType = carEntities.get(i).getCarType();
				setParkRecord(res,plate,carType,9);//查询车主“逃费”订单
			}
			if(res.getList().size()>0){
				res.setResult("203", "存在逃费订单");
				return res;
			}
		}
		TradeRecordEntity tradeRecordEntity = new TradeRecordEntity();
		Date date = new Date();
		tradeRecordEntity.setFlowNo(DateUtils.DateToString(date, "yyyyMMddHHmmss"));
		tradeRecordEntity.setIndustryFlag("1");//暂时将行业标识记录为“１”
		tradeRecordEntity.setTradeType(2);//"支付"类别为"1"，“充值”类别为“２”
		tradeRecordEntity.setTradeFlag(info.getheader().getAccount());
		tradeRecordEntity.setPayMode(info.getPaymentPattern());
		tradeRecordEntity.setResult(0);//"0"表示待收，”1“表示已收
		tradeRecordEntity.setPayMoney(Double.valueOf(info.getRecharge()));
		tradeRecordEntity.setPaidMoney(Double.valueOf(info.getRecharge()));
		tradeRecordEntity.setOrderTime(date);
		tradeRecordEntity.setOrderTimeStr(date.toString());
		int resultFlag = 0;
		try{
			tradeRecordApiService.save(tradeRecordEntity);
			if(info.getPaymentPattern()==2){//微信支付
            	WechatPayment wechatPayment = new WechatPayment();
            	HashMap<String,Object> map = wechatPayment.pay(2, info.getIP(),(int)(tradeRecordEntity.getPaidMoney()*100), "账户充值", tradeRecordEntity.getFlowNo());
				res.getProperty().put("appid", map.get("appid"));
				res.getProperty().put("partnerid", map.get("partnerid"));
				res.getProperty().put("prepay_id", map.get("prepay_id"));
				res.getProperty().put("package", map.get("package"));
				res.getProperty().put("noncestr", map.get("noncestr"));
				res.getProperty().put("timestamp", map.get("timestamp"));
				res.getProperty().put("sign", map.get("sign"));
				res.setResult("100", "生成充值订单成功");
				resultFlag=1;
			}else if(info.getPaymentPattern()==3){//支付宝支付
		    	AlipayPayment alipayPayment = new AlipayPayment();
		    	String signedOrderInfo = alipayPayment.appPay(String.valueOf(tradeRecordEntity.getPaidMoney()), "账户充值", tradeRecordEntity.getFlowNo());
		    	res.getProperty().put("signedOrderInfo", signedOrderInfo);
		    	res.setResult("100", "生成支付订单成功");
		    	resultFlag=1;
			}
		}catch(Exception e){
		    e.printStackTrace();
		}
		if(resultFlag == 1){
    		res.setResult("100", "生成充值订单成功");
		}else{
			res.setResult("204", "生成充值订单失败");
		}
		return res;
	}
	
	
	/*
	 * 查询支付结果
	 */
	public  CommonResponse queryResult(QueryResultInfo info){
		System.out.println(info.toString());
		CommonResponse res = new CommonResponse();
		if(info==null||info.getheader()==null || info.getheader().getToken()==null||"".equals(info.getheader().getToken()) || info.getOrderID()==null || "".equals(info.getOrderID())){
			res.setResult("201", "参数为空");
			return res;
		}
		if(!EhcacheUtil.isValidKey(info.getheader().getToken())){
			res.setResult("202", "登陆超时，请重新登陆");
			return res;
		}
		int  resultFlag = 0;
		TradeRecordEntity entity = new TradeRecordEntity();
		entity.setFlowNo(info.getOrderID());
		List<TradeRecordEntity> tradeRecordEntities = tradeRecordApiService.findTradeRecordsByPage(entity, null, null, null, null).getData();
		System.out.println("queryResult->tradeRecordEntities is " + tradeRecordEntities );
		if(tradeRecordEntities!=null && tradeRecordEntities.size()>0){
			if(tradeRecordEntities.get(0).getResult()==1){
				res.setResult("100", "三方平台支付成功");
				resultFlag=1;
			}
		}
		if(resultFlag == 1){
    		res.setResult("100", "三方平台支付成功");
		}else{
			res.setResult("203", "三方平台支付失败");
		}
		return res;
	}
	
	
	/*
	 * 获取指定支付类型的订单
	 */
		public void setParkRecord(CommonResponse res,String plate,String carType,int paymentFlag){
			HashMap<String,Object> map = new HashMap<String,Object>();
			int  resultFlag = 1;
			try{
				List<ParkRecordEntity> parkRecordEntities =  parkRecordApiService.findByPlateAndPaymentPattern(plate, carType,paymentFlag, null, null);
				if(parkRecordEntities!=null){
					for(int i=0;i<parkRecordEntities.size();i++){
			    		map.put("parkingRecordID", parkRecordEntities.get(i).getFlowNo());
			    		map.put("parkNumber", parkRecordEntities.get(i).getParkNo());
			    		ParkEntity parkEntity = new ParkEntity();
			    		parkEntity.setNo(parkRecordEntities.get(i).getParkNo());
			    		List<ParkEntity> parkEntities = parkApiService.findParks(parkEntity, null, null, null, null);
			    		if(parkEntities!=null && parkEntities.size()!=0){
				    		map.put("parkName", parkEntities.get(0).getName());
			    		}
			    		map.put("licensePlateNumber", plate);
			    		map.put("parkingLocation", parkRecordEntities.get(i).getParkLot());
			    		map.put("carType", parkRecordEntities.get(i).getCarType());
			    		map.put("startTime", DateUtils.DateToString(parkRecordEntities.get(0).getEnterTime(), "yyyy-MM-dd HH:mm:ss"));
			    		map.put("leaveTime", DateUtils.DateToString(parkRecordEntities.get(0).getLeaveTime(), "yyyy-MM-dd HH:mm:ss"));
			    		res.addListItem(map);
					}
				}
			}catch(Exception e){
				e.printStackTrace();
	    		resultFlag = 0;
			}
			if(resultFlag == 1){
	    		res.setResult("100", "获取车主订单成功");
			}else{
				res.setResult("203", "获取车主订单失败");
			}
		}
		
		
		/*
		 * 车主登录
		 */
		public CommonResponse login(LoginInfo info){
	        System.out.println(info.toString());
			CommonResponse response=new CommonResponse();
			if(info==null||info.getheader().getAccount()==null||"".equals(info.getheader().getAccount()) || info.getPassword()==null || "".equals(info.getPassword())
					|| info.getAndroidID()==null || "".equals(info.getAndroidID()) || info.getVersion()==null || "".equals(info.getVersion())){
				response.setResCode("201");
				response.setResMsg("参数为空");
				return response;
			}
			OwnerEntity ownerEntity= ownerApiService.findOwnerByAccount(info.getheader().getAccount());
			if(ownerEntity==null){
				response.setResCode("202");
				response.setResMsg("用户名不存在");
				return response;
			}
			if(!ownerEntity.getPword().equals(info.getPassword())){
				response.setResCode("203");
				response.setResMsg("密码错误");
				return response;
			}
			String token=EhcacheUtil.getKey(info.getheader().getAccount());
			if(token==null){
				response.setResult("100", "登录成功,首次登录");
			}else{
				response.setResult("100", "登录成功");
			}
			token=info.getAndroidID()+DateUtils.DateToString(new Date(), "yyyyMMddHH");
			EhcacheUtil.putKeyValue(token,info.getheader().getAccount());
			response.getProperty().put("token", token);
			return response;
		}
		
		
		/*
		 * 判断此用户名是否已注册
		 */
		public CommonResponse analysis(AnalysisInfo info){
	        System.out.println(info.toString());
			CommonResponse response=new CommonResponse();
			if(info==null||info.getheader().getAccount()==null||"".equals(info.getheader().getAccount())){
				response.setResCode("201");
				response.setResMsg("参数为空");
				return response;
			}
			OwnerEntity ownerEntity= ownerApiService.findOwnerByOwnerNo(info.getheader().getAccount());
			if(ownerEntity==null){
				response.setResCode("100");
				response.setResMsg("用户名不存在");
			}else{
				response.setResCode("100");
				response.setResMsg("用户名存在");
			}
			return response;
		}
		
		
		/*
		 * 注册
		 */
		public CommonResponse register(RegisterInfo info) throws Exception{
	        System.out.println(info.toString());
			CommonResponse response=new CommonResponse();
			OwnerEntity ownerEntity = new OwnerEntity();
			ownerEntity.setPhone(info.getheader().getAccount());
			ownerEntity.setPword(info.getPassword());
			ownerEntity.setPayPword(info.getPassword());//暂时将支付密码与登录密码设为一致
			ownerEntity.setNo(info.getheader().getAccount());
			if("forget".equals(info.getRegisterType())){
				if(ownerApiService.update(ownerEntity)!=-1){
					response.setResCode("100");
					response.setResMsg("更改密码成功");
				}else{
					response.setResCode("201");
					response.setResMsg("更改密码失败");
				}
			}else if("register".equals(info.getRegisterType())){
				if(dbUtils.createAccount(ownerEntity)!=-1){
					response.setResCode("100");
					response.setResMsg("注册成功");
				}else{
					response.setResCode("201");
					response.setResMsg("注册失败");
				}
			}
			return response;
		}
		
		
		/*
		 * 查询用户中心基本信息
		 */
		public CommonResponse queryUserInfo(QueryUserInfo info) throws Exception{
	        System.out.println(info.toString());
			CommonResponse res = new CommonResponse();
			if(info==null||info.getheader().getToken()==null||"".equals(info.getheader().getToken())){
				res.setResult("201", "参数为空");
				return res;
			}
			if(!EhcacheUtil.isValidKey(info.getheader().getToken())){
				res.setResult("202", "登陆超时，请重新登陆");
				return res;
			}
			OwnerEntity ownerEntity= ownerApiService.findOwnerByAccount(info.getheader().getAccount());
			AccountEntity accountEntity = accountApiService.findAccountByOwnerNo(ownerEntity.getNo());
			if(accountEntity!=null){
				res.getProperty().put("accountBalance", accountEntity.getBalance());
				res.getProperty().put("parkingCoupon", accountEntity.getCoupon());
				if(ownerEntity.getName()==null || "".equals(ownerEntity.getName())){
					res.getProperty().put("nickName", ownerEntity.getPhone());
				}else{
					res.getProperty().put("nickName", ownerEntity.getName());
				}
				res.getProperty().put("headportrait", ownerEntity.getOwnerImg());
				res.setResult("100", "查询成功");
			}else{
				res.setResult("201", "该用户不存在");
			}
			return res;
		}
		
		
		/*
		 * 设置用户头像
		 */
		public CommonResponse setHeadPortrait(SetHeadPortraitInfo info) throws Exception{
	        System.out.println(info.toString());
			CommonResponse res = new CommonResponse();
			if(info==null||info.getheader().getToken()==null||"".equals(info.getheader().getToken())){
				res.setResult("201", "参数为空");
				return res;
			}
			if(!EhcacheUtil.isValidKey(info.getheader().getToken())){
				res.setResult("202", "登陆超时，请重新登陆");
				return res;
			}
			OwnerEntity ownerEntity= ownerApiService.findOwnerByAccount(info.getheader().getAccount());
			ownerEntity.setOwnerImg(info.getHeadPortrait());
			if(ownerApiService.update(ownerEntity)!=-1){
				res.setResult("100", "设置头像成功");
			}else{
				res.setResult("201", "设置头像失败");
			}
			return res;
		}
		
		/*
		 * 设置用户昵称
		 */
		public CommonResponse setNickName(SetNickNameInfo info) throws Exception{
	        System.out.println(info.toString());
			CommonResponse res = new CommonResponse();
			if(info==null||info.getheader().getToken()==null||"".equals(info.getheader().getToken())){
				res.setResult("201", "参数为空");
				return res;
			}
			if(!EhcacheUtil.isValidKey(info.getheader().getToken())){
				res.setResult("202", "登陆超时，请重新登陆");
				return res;
			}
			OwnerEntity ownerEntity= ownerApiService.findOwnerByAccount(info.getheader().getAccount());
			ownerEntity.setName(info.getNickName());
			if(ownerApiService.update(ownerEntity)!=-1){
				res.setResult("100", "设置昵称成功");
			}else{
				res.setResult("201", "设置昵称失败");
			}
			return res;
		}
		
		
		/*
		 * 记录查询
		 */
		public CommonResponse searchRecord(RecordSearchInfo info){
	        System.out.println(info.toString());
			CommonResponse res = new CommonResponse();
			if(info==null||info.getheader().getToken()==null||"".equals(info.getheader().getToken())){
				res.setResult("201", "参数为空");
				return res;
			}
			if(!EhcacheUtil.isValidKey(info.getheader().getToken())){
				res.setResult("202", "登陆超时，请重新登陆");
				return res;
			}
	        System.out.println(new Date().toString());
			String formatDate = "yyyy-MM-dd";
			String dateStr = DateUtils.DateToString(new Date(),formatDate);
			String startStr = dateStr + " " + "00:00:00";
			String endStr = dateStr + " " + "23:59:59";
			String formatTime = "yyyy-MM-dd HH:mm:ss";
	        Date startTime = null;
	        Date endTime = null;
	        if(info.getType() == TYPE_CURRENT_RECORD){
		        startTime = DateUtils.StringToDate(startStr,formatTime); 
		        endTime = DateUtils.StringToDate(endStr, formatTime); 
	        }
	        int recordNumber = 1;
	        boolean hasTitle = false;
			OwnerEntity ownerEntity= ownerApiService.findOwnerByAccount(info.getheader().getAccount());
			List<CarEntity> carEntities = carApiService.findCarsByOwnerNo(ownerEntity.getNo());
			for(int i=0;i<carEntities.size();i++){
		    	List<ParkRecordEntity> parkRecordEntities = parkRecordApiService.findByPlate(carEntities.get(i).getPlate(), carEntities.get(i).getCarType(), startTime, endTime);
		    	ParkRecordEntity parkRecordEntity = new ParkRecordEntity();
		    	if(parkRecordEntities.size()!=0){
		    		if(!hasTitle){
			    		HashMap<String,Object> mapTitle = new HashMap<String,Object> ();
			    		mapTitle.put("licensePlateNumber", "车辆牌照");
			    		mapTitle.put("startTime","入场时间");
			    		mapTitle.put("leaveTime", "离场时间");
			    		mapTitle.put("paymentPattern", "支付类型");
			    		mapTitle.put("expense", "费用");
			    		mapTitle.put("parkingName", "车场名称");
			        	res.getList().add(0, mapTitle);
			        	hasTitle = true;
		    		}
		        	for(int j = 0 ; j < parkRecordEntities.size() ; j++) {
		        		parkRecordEntity = parkRecordEntities.get(j);
		        		HashMap<String,Object> mapItem = new HashMap<String,Object> ();
		        		mapItem.put("licensePlateNumber", parkRecordEntity.getPlate());
		        		mapItem.put("startTime", DateUtils.DateToString(parkRecordEntity.getEnterTime(), formatTime));
		        		mapItem.put("leaveTime", DateUtils.DateToString(parkRecordEntity.getLeaveTime(), formatTime));
			    		ParkEntity parkEntity = new ParkEntity();
			    		parkEntity.setNo(parkRecordEntities.get(j).getParkNo());
			    		List<ParkEntity> parkEntities = parkApiService.findParks(parkEntity, null, null, null, null);
			    		if(parkEntities!=null && parkEntities.size()!=0){
			        		mapItem.put("parkingName", parkEntities.get(0).getName());
			    		}
		        		mapItem.put("paymentPattern", PaymentConvertUtils.covertToString(parkRecordEntity.getPaymentFlag()));
		        		mapItem.put("expense", String.valueOf(parkRecordEntity.getPayMoney() + "元"));//应付金额
		            	res.getList().add(recordNumber++, mapItem);
		            }
		    	}
			}
			res.setResult("100", "查询成功");     
	        return res;
		}
		
		
		/*
		 * 查询用户名下牌照
		 */
		public CommonResponse queryLicense(QueryLicenseInfo info) throws Exception{
	        System.out.println(info.toString());
			CommonResponse res = new CommonResponse();
			if(info==null||info.getheader().getToken()==null||"".equals(info.getheader().getToken())){
				res.setResult("201", "参数为空");
				return res;
			}
			if(!EhcacheUtil.isValidKey(info.getheader().getToken())){
				res.setResult("202", "登陆超时，请重新登陆");
				return res;
			}
			OwnerEntity ownerEntity= ownerApiService.findOwnerByAccount(info.getheader().getAccount());
			if(ownerEntity!=null){
				res.getProperty().put("licensePlateFirst", ownerEntity.getFirstLicense());
				res.getProperty().put("licensePlateSecond", ownerEntity.getSecondLicense());
				res.setResult("100", "查询成功");
			}else{
				res.setResult("203", "用户不存在");
			}
			return res;
		}
		
		
		/*
		 * 解绑指定牌照
		 */
		public CommonResponse unBindLicense(UnBindLicenseInfo info) throws Exception{
	        System.out.println(info.toString());
	        System.out.println(info.getLicensePlateDismiss());
			CommonResponse res = new CommonResponse();
			if(info==null||info.getheader().getToken()==null||"".equals(info.getheader().getToken())){
				res.setResult("201", "参数为空");
				return res;
			}
			if(!EhcacheUtil.isValidKey(info.getheader().getToken())){
				res.setResult("202", "登陆超时，请重新登陆");
				return res;
			}
			OwnerEntity ownerEntity= ownerApiService.findOwnerByAccount(info.getheader().getAccount());
			if(ownerEntity!=null){
				if( info.getLicensePlateDismiss().equals(ownerEntity.getFirstLicense() )){
					ownerEntity.setFirstLicense("");
				}else if(info.getLicensePlateDismiss().equals(ownerEntity.getSecondLicense())){
					ownerEntity.setSecondLicense("");
				}
				if(dbUtils.unbindLicensePlate(ownerEntity, info.getLicensePlateDismiss())==1){
					res.setResult("100", "解绑牌照成功");
				}else{
					res.setResult("204", "解绑牌照失败");
				}
			}else{
				res.setResult("203", "用户不存在");
			}
			return res;
		}
		
		
		/*
		 * 绑定指定牌照
		 */
		public CommonResponse bindLicense(BindLicenseInfo info) throws Exception{
			System.out.println(info.toString());
	        System.out.println(info.getLicensePlateBind());
			CommonResponse res = new CommonResponse();
			if(info==null||info.getheader().getToken()==null||"".equals(info.getheader().getToken())){
				res.setResult("201", "参数为空");
				return res;
			}
			if(!EhcacheUtil.isValidKey(info.getheader().getToken())){
				res.setResult("202", "登陆超时，请重新登陆");
				return res;
			}
			OwnerEntity ownerEntity= ownerApiService.findOwnerByAccount(info.getheader().getAccount());
			if(ownerEntity!=null){
				if(ownerEntity.getFirstLicense()==null || "".equals(ownerEntity.getFirstLicense())){
					ownerEntity.setFirstLicense(info.getLicensePlateBind());
				}else if(ownerEntity.getSecondLicense()==null || "".equals(ownerEntity.getSecondLicense()) ){
					ownerEntity.setSecondLicense(info.getLicensePlateBind());
				}else{
					res.setResult("204", "绑定牌照数量已达上限");
				}
				if(dbUtils.bindLicensePlate(ownerEntity, info.getLicensePlateBind(),info.getCarType())==1){
					res.setResult("100", "绑定牌照成功");
				}else{
					res.setResult("205", "绑定牌照失败");
				}
			}else{
				res.setResult("203", "用户不存在");
			}
			return res;
		}
		
		
		/*
		 * 重置密码
		 */
		public CommonResponse resetPasswd(ResetPasswdInfo info) throws Exception{
	        System.out.println(info.toString());
			CommonResponse res = new CommonResponse();
			if(info==null||info.getheader().getToken()==null||"".equals(info.getheader().getToken()) || info.getPassword()==null || "".equals(info.getPassword())  || info.getNewPassword()==null || "".equals(info.getNewPassword()) ){
				res.setResult("201", "参数为空");
				return res;
			}
			if(!EhcacheUtil.isValidKey(info.getheader().getToken())){
				res.setResult("202", "登陆超时，请重新登陆");
				return res;
			}
			OwnerEntity ownerEntity= ownerApiService.findOwnerByAccount(info.getheader().getAccount());
			if(ownerEntity==null){
				res.setResCode("203");
				res.setResMsg("用户名不存在");
				return res;
			}
			if(!ownerEntity.getPword().equals(info.getPassword())){
				res.setResCode("204");
				res.setResMsg("原密码错误");
				return res;
			}
			ownerEntity.setPword(info.getNewPassword());
			if(ownerApiService.update(ownerEntity) == -1){
				res.setResCode("205");
				res.setResMsg("重置密码失败");
				return res;
			}else{
				res.setResCode("100");
				res.setResMsg("重置密码成功");
				return res;
			}
		}
		
		/*
		 * 设置支付密码
		 */
		public CommonResponse setPaymentPasswd(SetPaymentPasswdInfo info) throws Exception{
	        System.out.println(info.toString());
			CommonResponse res = new CommonResponse();
			if(info==null||info.getheader().getToken()==null||"".equals(info.getheader().getToken()) || info.getPassword()==null || "".equals(info.getPassword())  || info.getPaymentPassword()==null || "".equals(info.getPaymentPassword()) ){
				res.setResult("201", "参数为空");
				return res;
			}
			if(!EhcacheUtil.isValidKey(info.getheader().getToken())){
				res.setResult("202", "登陆超时，请重新登陆");
				return res;
			}
			OwnerEntity ownerEntity= ownerApiService.findOwnerByAccount(info.getheader().getAccount());
			if(ownerEntity==null){
				res.setResCode("203");
				res.setResMsg("用户名不存在");
				return res;
			}
			if(!ownerEntity.getPword().equals(info.getPassword())){
				res.setResCode("204");
				res.setResMsg("登录密码错误");
				return res;
			}
			ownerEntity.setPayPword(info.getPaymentPassword());
			if(ownerApiService.update(ownerEntity) == -1){
				res.setResCode("205");
				res.setResMsg("支付密码设置失败");
				return res;
			}else{
				res.setResCode("100");
				res.setResMsg("支付密码设置成功");
				return res;
			}
		}
		
		/*
		 * 用户反馈
		 */
		public CommonResponse feedBack(FeedBackInfo info) throws Exception{
	        System.out.println(info.toString());
			CommonResponse res = new CommonResponse();
			if(info==null||info.getheader().getToken()==null||"".equals(info.getheader().getToken()) ){
				res.setResult("201", "参数为空");
				return res;
			}
			if(!EhcacheUtil.isValidKey(info.getheader().getToken())){
				res.setResult("202", "登陆超时，请重新登陆");
				return res;
			}
			System.out.println(info.getheader().getAccount() + ":" + info.getFeedContent());//暂以打印反馈信息代替反馈信息的处理流程
			res.setResCode("100");
			res.setResMsg("反馈成功");
			return res;
		}
		
		
		/*
		 * 用户登出
		 */
		public CommonResponse logout(LogoutInfo info) throws Exception{
	        System.out.println(info.toString());
			CommonResponse res = new CommonResponse();
			if(info==null||info.getheader().getToken()==null||"".equals(info.getheader().getToken()) ){
				res.setResult("201", "参数为空");
				return res;
			}
			if(!EhcacheUtil.isValidKey(info.getheader().getToken())){
				res.setResult("202", "登陆超时，请重新登陆");
				return res;
			}
			EhcacheUtil.putKeyValue(null,info.getheader().getAccount());//此处业务处理后续需优化
			res.setResCode("100");
			res.setResMsg("登出成功");
			return res;
		}
		
		
		/*
		 * 查询附近停车场信息
		 */
		public CommonResponse searchNearbyPark(SearchParkInfo info) throws Exception{
	        System.out.println(info.toString());
			CommonResponse res = new CommonResponse();
			if(info==null||info.getheader().getToken()==null||"".equals(info.getheader().getToken()) ){
				res.setResult("201", "参数为空");
				return res;
			}
			if(!EhcacheUtil.isValidKey(info.getheader().getToken())){
				res.setResult("202", "登陆超时，请重新登陆");
				return res;
			}
			
			List<ParkEntity> parkEntities = null;
			ParkEntity searchEntity = new ParkEntity();
           if(info.getParkingType()==1){
				searchEntity.setType(1);
			}else if(info.getParkingType()==2){
				searchEntity.setType(2);
			}
			parkEntities = parkApiService.findParks(searchEntity, null, null, null, null);
			if(parkEntities !=null && parkEntities.size()>0){
				ParkEntity parkEntity = new ParkEntity();
				FeeRateEntity feeRateEntity = new FeeRateEntity();
				int num = 0;
				for(int i=0;i<parkEntities.size();i++){
					parkEntity=parkEntities.get(i);
					feeRateEntity=feeApiService.findByParkNo(parkEntity.getNo());
					Double distance = LocationUtils.getDistance(info.getLatitude(), info.getLongitude(), parkEntity.getLat(), parkEntity.getLon());
					if(distance.intValue() <= info.getRange()){
		        		HashMap<String,Object> mapItem = new HashMap<String,Object> ();
		        		mapItem.put("longitude", parkEntity.getLon());
		        		mapItem.put("latitude", parkEntity.getLat());
		        		mapItem.put("parkName", parkEntity.getName());
		        		mapItem.put("location", parkEntity.getAddress());
		        		mapItem.put("distance", distance.intValue());     
		        		mapItem.put("idleLocationNumber", parkEntity.getRemainSpace());
		        		mapItem.put("totalLocationNumber", parkEntity.getTotalSpace());
		        		if(feeRateEntity!=null){
			        		if(feeRateEntity.getType()==1){//按次收费
				        		mapItem.put("feeScale", feeRateEntity.getFeeByCount() + "元/次");
			        		}else if(feeRateEntity.getType()==2){//按小时收费
			        			mapItem.put("feeScale", feeRateEntity.getFeeByTime() + "元/时");
			        		}
			        		mapItem.put("parkingFreeTime", feeRateEntity.getFreeTimeLen()  + "分");
		        		}else{
		        			mapItem.put("feeScale", "未知");
			        		mapItem.put("parkingFreeTime", "未知");
		        		}
		        		mapItem.put("certificated", parkEntity.getCertStatus());
		        		mapItem.put("charge", parkEntity.getOperateNature());
		        		mapItem.put("autoCharge", parkEntity.getChargeMode());
		        		mapItem.put("networkChartge", parkEntity.getSettleMode());
		        		mapItem.put("cashCharge", parkEntity.getPayMode().substring(0, 1));
		        		mapItem.put("posCharge", parkEntity.getPayMode().substring(1, 2));
		        		mapItem.put("alipayCharge", parkEntity.getPayMode().substring(2, 3));
		        		mapItem.put("wechatCharge", parkEntity.getPayMode().substring(3, 4));
		        		res.getList().add(num++, mapItem);
					}
				}
			}
			res.setResCode("100");
			res.setResMsg("查询成功");
			return res;
		}
		
		
		/*
		 * 获取消息
		 */
		public CommonResponse getMessage(TokenInfo info){
	        System.out.println(info.toString());
			CommonResponse res = new CommonResponse();
			if(info==null||info.getheader().getToken()==null||"".equals(info.getheader().getToken())){
				res.setResult("201", "参数为空");
				return res;
			}
			if(!EhcacheUtil.isValidKey(info.getheader().getToken())){
				res.setResult("202", "登陆超时，请重新登陆");
				return res;
			}
			MessageEntity messageEntity = new MessageEntity();
			messageEntity.setMessageState(1);
			messageEntity.setMessageOwner(info.getheader().getAccount());;
			List<MessageEntity> messageEntities = messageApiService.findMessages(messageEntity, null);
			if(messageEntities!=null){
				for(int i=0;i<messageEntities.size();i++){
					HashMap<String, Object> mapItem=new HashMap<String, Object>();
					mapItem.put("messageTitle", messageEntities.get(i).getMessageTitle());
					mapItem.put("messageAbstract", messageEntities.get(i).getMessageAbstract());
					mapItem.put("messageTime", DateUtils.DateToString(messageEntities.get(i).getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
					mapItem.put("messageDetail", messageEntities.get(i).getMessageDetail());
		            res.addListItem(mapItem);
				}
			}
			/*//暂未存储消息内容，故以此形式传递信息
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");       
            Date curDate = new Date(System.currentTimeMillis());//获取当前时间       
            String date = formatter.format(curDate); 
            HashMap<String, Object> map=new HashMap<String, Object>();
            map.put("messageTitle", "停车券通知");
            map.put("messageAbstract", "5元停车券");
            map.put("messageTime", date);
            map.put("messageDetail", "感谢您的支持，现送您一张5元停车券，请到'我的'-'停车券'中查看详情。");
            res.getList().add(0 ,map);*/
	        res.setResult("100", "获取信息成功");
			return res;
		}
		
		/*
		 * 查询是否设置支付密码
		 */
		public CommonResponse queryPaymentPasswd(TokenInfo info){
	        System.out.println(info.toString());
			CommonResponse res = new CommonResponse();
			if(info==null||info.getheader().getToken()==null||"".equals(info.getheader().getToken())){
				res.setResult("201", "参数为空");
				return res;
			}
			if(!EhcacheUtil.isValidKey(info.getheader().getToken())){
				res.setResult("202", "登陆超时，请重新登陆");
				return res;
			}
			int resultFlag = 0;
	    	OwnerEntity ownerEntity= ownerApiService.findOwnerByAccount(info.getheader().getAccount());
	    	if(ownerEntity!=null){
	    		if(ownerEntity.getPayPword()!=null && !"".equals(ownerEntity.getPayPword())){
	    			resultFlag = 1;
	    		}
	    	}
	        if(resultFlag==1){
	        	res.setResult("100", "已设置支付密码");
	        }else{
	        	res.setResult("203", "请设置支付密码");
	        }
			return res;
		}
}
	