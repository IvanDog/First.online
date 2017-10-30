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
import com.ehl.itspark.common.PaymentConvertUtils;
import com.ehl.itspark.common.WechatPayment;
import com.ehl.itspark.data.entity.AccountEntity;
import com.ehl.itspark.data.entity.CarEntity;
import com.ehl.itspark.data.entity.ClockEntity;
import com.ehl.itspark.data.entity.CouponEntity;
import com.ehl.itspark.data.entity.FeeRateEntity;
import com.ehl.itspark.data.entity.MessageEntity;
import com.ehl.itspark.data.entity.ParkEnterEntity;
import com.ehl.itspark.data.entity.ParkEntity;
import com.ehl.itspark.data.entity.ParkLotEntity;
import com.ehl.itspark.data.entity.ParkRecordEntity;
import com.ehl.itspark.data.entity.PersonAccessEntity;
import com.ehl.itspark.data.entity.PersonEntity;
import com.ehl.itspark.data.entity.ShiftworkEntity;
import com.ehl.itspark.data.entity.TradeRecordEntity;
import com.ehl.itspark.info.ClockInfo;
import com.ehl.itspark.info.EntranceInfo;
import com.ehl.itspark.info.HistoryRecordSearchInfo;
import com.ehl.itspark.info.LicenseInfo;
import com.ehl.itspark.info.LocationInfo;
import com.ehl.itspark.info.LoginInfo;
import com.ehl.itspark.info.LogoutInfo;
import com.ehl.itspark.info.LotDetailQueryInfo;
import com.ehl.itspark.info.ParkLotQueryInfo;
import com.ehl.itspark.info.PaymentInfo;
import com.ehl.itspark.info.QueryResultInfo;
import com.ehl.itspark.info.ResetPasswdInfo;
import com.ehl.itspark.info.SettleAccountInfo;
import com.ehl.itspark.info.TodayRecordSearchInfo;
import com.ehl.itspark.info.TokenInfo;
import com.ehl.itspark.service.intf.AccountApiService;
import com.ehl.itspark.service.intf.CarApiService;
import com.ehl.itspark.service.intf.ClockApiService;
import com.ehl.itspark.service.intf.CouponApiService;
import com.ehl.itspark.service.intf.FeeApiService;
import com.ehl.itspark.service.intf.MessageApiService;
import com.ehl.itspark.service.intf.OwnerApiService;
import com.ehl.itspark.service.intf.ParkApiService;
import com.ehl.itspark.service.intf.ParkEnterApiService;
import com.ehl.itspark.service.intf.ParkLotApiService;
import com.ehl.itspark.service.intf.ParkRecordApiService;
import com.ehl.itspark.service.intf.PersonAccessApiService;
import com.ehl.itspark.service.intf.PersonApiService;
import com.ehl.itspark.service.intf.TradeRecordApiService;
import com.ehl.itspark.work.SchedulingCalendarBusiness;

@Service
public class CollectorServer {

	private static final int LICENSE_ARRIVING_TYPE=101;
	private static final int LICENSE_LEAVING_TYPE=102;
	private static final int ATTENDANCE_TYPE_START=301;
	private static final int ATTENDANCE_TYPE_END=302;
	
	@Autowired
	private PersonAccessApiService personAccessApiService;
	@Autowired
	private PersonApiService personApiService;
	@Autowired
	private ParkApiService parkApiService;
	@Autowired
	private ParkRecordApiService parkRecordApiService;
	@Autowired
	private ParkEnterApiService parkEnterApiService;
	@Autowired
	private ParkLotApiService parkLotApiService;
	@Autowired
	private CarApiService carApiService;
	@Autowired
	private AccountApiService accountApiService;
	@Autowired
	private OwnerApiService ownerApiService;
	@Autowired
	private TradeRecordApiService tradeRecordApiService;
	@Autowired
	private CouponApiService couponApiService;
	@Autowired
	private ClockApiService clockApiService;
	@Autowired
	private FeeApiService feeApiService;
	@Autowired
	private MessageApiService messageApiService;
	@Autowired
	private DbUtils dbUtils;
	@Autowired
	private SchedulingCalendarBusiness schedulingCalendarBusiness;
	
	private Logger logger=LoggerFactory.getLogger(CollectorServer.class);
	
	/*
	 * 收费员登录
	 */
	public CommonResponse login(LoginInfo info){
		CommonResponse response=new CommonResponse();
		if(info==null||info.getheader().getAccount()==null||"".equals(info.getheader().getAccount()) || info.getPassword()==null || "".equals(info.getPassword())
				|| info.getAndroidID()==null || "".equals(info.getAndroidID()) || info.getVersion()==null || "".equals(info.getVersion())){
			response.setResCode("201");
			response.setResMsg("参数为空");
			return response;
		}
		PersonAccessEntity accessEntity= personAccessApiService.findPersionAccess(info.getheader().getAccount());
		if(accessEntity==null){
			response.setResCode("202");
			response.setResMsg("用户名不存在");
			return response;
		}
		if(!accessEntity.getPword().equals(info.getPassword())){
			response.setResCode("201");
			response.setResMsg("密码错误");
			return response;
		}
		String token=EhcacheUtil.getKey(info.getheader().getAccount());
		if(token==null){
			response.setResult("100", "登录成功,首次登录");
		}else{
			response.setResult("101", "登录成功");
		}
		token=info.getAndroidID()+DateUtils.DateToString(new Date(), "yyyyMMddHH");
		EhcacheUtil.putKeyValue(token,info.getheader().getAccount());
		response.getProperty().put("token", token);
		return response;
	}
	
	
	/*
	 * 请求工作任务信息
	 */
	public CommonResponse requestInfo(TokenInfo info){
		CommonResponse res = new CommonResponse();
		if(info==null||info.getheader().getToken()==null||"".equals(info.getheader().getToken())){
			res.setResult("201", "参数为空");
			return res;
		}
		if(!EhcacheUtil.isValidKey(info.getheader().getToken())){
			res.setResult("202", "登陆超时，请重新登陆");
			return res;
		}
		String personNo= EhcacheUtil.getValue(info.getheader().getToken());
		PersonEntity personEntity=personApiService.findPersionsByNo(personNo);
		if(personEntity==null){
			res.setResult("204", "未找到该用户信息");
			return res;
		}
		if(personEntity.getParkNo()==null||"".equals(personEntity.getParkNo())){
			res.setResult("205", "未找到该用户所在停车场信息");
			return res;
		}
		String workDateFormat = "yyyyMMdd";
		String workTimeFormat = "HH:mm";
		String workDate = DateUtils.DateToString(new Date(),workDateFormat);
		try{
			res.getProperty().put("parkName", personEntity.getParkName());
			res.getProperty().put("parkNumber", personEntity.getParkNo());
			res.getProperty().put("workStartTime", "09:00");
			res.getProperty().put("workEndTime", "17:30");
/*			ShiftworkEntity entity= schedulingCalendarBusiness.findShiftworkByPersionAndDate(personEntity.getName(), workDate);
			res.getProperty().put("parkName", entity.getParkName());
			res.getProperty().put("parkNumber", entity.getParkNo());
			res.getProperty().put("workStartTime", DateUtils.DateToString(entity.getSignIn(), workTimeFormat));
			res.getProperty().put("workEndTime", DateUtils.DateToString(entity.getSignOut(), workTimeFormat));
			res.getProperty().put("feeScale", "5元/时");*/
			res.getProperty().put("chargeStandard", "收费依据:[2017]天津市停车收费1号文件");
			res.getProperty().put("superviseTelephone", "监督电话: 4000 000 000");
			res.setResult("100", "获取工作任务成功");
		}catch(Exception e){
			logger.error("获取工作任务失败，人员:" + personEntity.getParkName() + "，时间:" + workDate + " error:" + e.getMessage(),e);
			res.setResult("206", "获取工作任务失败");
		}
		return res;
	}
	
	
	/*
	 * 上报定位信息
	 */
	public CommonResponse reportLocation(LocationInfo info){
		CommonResponse res = new CommonResponse();
		if(info==null||info.getheader().getToken()==null||"".equals(info.getheader().getToken())){
			res.setResult("201", "参数为空");
			return res;
		}
		if(!EhcacheUtil.isValidKey(info.getheader().getToken())){
			res.setResult("202", "登陆超时，请重新登陆");
			return res;
		}
		String personNo= EhcacheUtil.getValue(info.getheader().getToken());
		PersonEntity personEntity=personApiService.findPersionsByNo(personNo);
		if(personEntity==null){
			res.setResult("203", "未找到该用户信息");
			return res;
		}
		if(personEntity.getParkNo()==null||"".equals(personEntity.getParkNo())){
			res.setResult("204", "未找到该用户所在停车场信息");
			return res;
		}
		
		try {
			String workDateFormat = "yyyyMMdd";
			String workDate = DateUtils.DateToString(new Date(),workDateFormat);
			ShiftworkEntity entity= schedulingCalendarBusiness.findShiftworkByPersionAndDate(personEntity.getName(), workDate);
			boolean result = parkApiService.isInLocation(personEntity.getParkNo(), info.getLongitude(), info.getLatitude(), entity.getAddrRadius());
			if(result){
				res.setResult("100", "考勤范围内");
			}else {
				res.setResult("205", "考勤范围外");
			}
		} catch (Exception e) {
			res.setResult("206", e.getMessage());
		}
		return res;
	}
	
	
	/*
	 * 打卡
	 */
	public CommonResponse clock(ClockInfo info){
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
		String personNo= EhcacheUtil.getValue(info.getheader().getToken());
		PersonEntity personEntity=personApiService.findPersionsByNo(personNo);
		if(personEntity==null){
			res.setResult("203", "未找到该用户信息");
			return res;
		}
		if(personEntity.getParkNo()==null||"".equals(personEntity.getParkNo())){
			res.setResult("204", "未找到该用户所在停车场信息");
			return res;
		}
		try {
			ClockEntity clockEntity = new ClockEntity();
			clockEntity.setNo(DateUtils.DateToString(new Date(), "yyyyMMddhhmmss"));
			clockEntity.setParkNo(info.getParkNo());
			clockEntity.SetEmployeeNo(info.getheader().getAccount());
			clockEntity.setType(info.getClockType());
			clockEntity.setLocationState(info.getLocationState());
			String format = "yyyy-MM-dd HH:mm:ss";
	        Date clockTime = DateUtils.StringToDate(info.getClockTime(),format); 
			clockEntity.setClockTime(clockTime);
			if(clockApiService.saveClock(clockEntity) == 1){
				if(info.getClockType() == ATTENDANCE_TYPE_START){
					res.setResult("100", "上班打卡成功");
				}else if(info.getClockType()  == ATTENDANCE_TYPE_END){
					res.setResult("100", "下班打卡成功");
				}
			}else {
				res.setResult("205", "打卡失败");
			}
		} catch (Exception e) {
			res.setResult("206", e.getMessage());
		}
		return res;
	}
	
	
	/*
	 * 查询车牌是否可入场/出场
	 */
	public CommonResponse sendLicense(LicenseInfo info){
        System.out.println(info.toString());
		CommonResponse res = new CommonResponse();
		if(info==null||info.getheader()==null || info.getheader().getToken()==null||"".equals(info.getheader().getToken()) || info.getLicensePlateNumber()==null || "".equals(info.getLicensePlateNumber())
				|| info.getCarType()==null || "".equals(info.getCarType()) || info.getType()==null){
			res.setResult("201", "参数为空");
			return res;
		}
		if(!EhcacheUtil.isValidKey(info.getheader().getToken())){
			res.setResult("202", "登陆超时，请重新登陆");
			return res;
		}
		List<ParkEnterEntity> parkEnterEntities = parkEnterApiService.findByParkNoAndPlate(info.getParkNumber(), info.getLicensePlateNumber(),info.getCarType());
		List<ParkRecordEntity> parkRecordEntities = parkRecordApiService.findByParkNoAndPlate(/*info.getParkNumber()*/null, info.getLicensePlateNumber(),info.getCarType(), null, null, null);
		List<ParkRecordEntity> unFinishedParkRecordEntities = parkRecordApiService.findByPlateAndPaymentPattern(info.getLicensePlateNumber(), info.getCarType(), 0/*未付车辆*/, null, null);
		List<ParkRecordEntity> escapeParkRecordEntities = parkRecordApiService.findByPlateAndPaymentPattern(info.getLicensePlateNumber(), info.getCarType(), 9/*未付车辆*/, null, null);	
		System.out.println("sendLicense->parkEnterEntities is " + parkEnterEntities.size());
		System.out.println("sendLicense->parkRecordEntities is " + parkRecordEntities.size());
		System.out.println("sendLicense->unFinishedParkRecordEntities is " + unFinishedParkRecordEntities.size());
		System.out.println("sendLicense->escapeParkRecordEntities is " + escapeParkRecordEntities.size());
		if( info.getType()==LICENSE_ARRIVING_TYPE){
			if(parkEnterEntities!=null && parkEnterEntities.size()>0 && parkEnterEntities.get(0)!=null){
			    res.setResult("203", "该车辆已在场内");
			}else if(unFinishedParkRecordEntities.size()>0 || escapeParkRecordEntities.size()>0){
			    res.setResult("204", "欠费车辆");
			    int num = 0;
				HashMap<String,Object> mapTitle = new HashMap<String,Object> ();
				mapTitle.put("licensePlateNumber", "车辆牌照");
				mapTitle.put("startTime","入场时间");
				mapTitle.put("leaveTime", "离场时间");
				mapTitle.put("parkNumber", "车场编号");
				mapTitle.put("expense", "费用");//暂时用应付金额替代实付金额
		    	res.getList().add(num++, mapTitle);
			    String format = "yyyy-MM-dd HH:mm:ss";
				if(unFinishedParkRecordEntities!=null){
					for(int i=0;i<unFinishedParkRecordEntities.size();i++){
					    HashMap<String,Object> map = new HashMap<String,Object>();		
					    map.put("licensePlateNumber", unFinishedParkRecordEntities.get(i).getPlate());
					    map.put("startTime", DateUtils.DateToString(unFinishedParkRecordEntities.get(i).getEnterTime(), format));
					    map.put("leaveTime", DateUtils.DateToString(unFinishedParkRecordEntities.get(i).getLeaveTime(), format));
					    map.put("parkNumber", unFinishedParkRecordEntities.get(i).getParkNo());
					    map.put("expense", String.valueOf(unFinishedParkRecordEntities.get(i).getPayMoney()) + "元");
					    map.put("carType", unFinishedParkRecordEntities.get(i).getCarType());
					    map.put("parkingRecordID", unFinishedParkRecordEntities.get(i).getFlowNo());
				    	res.getList().add(num++, map);
					}
				}
				for(int i=0;i<escapeParkRecordEntities.size();i++){
				    HashMap<String,Object> map = new HashMap<String,Object>();		
				    map.put("licensePlateNumber", escapeParkRecordEntities.get(i).getPlate());
				    map.put("startTime", DateUtils.DateToString(escapeParkRecordEntities.get(i).getEnterTime(), format));
				    map.put("leaveTime", DateUtils.DateToString(escapeParkRecordEntities.get(i).getLeaveTime(), format));
				    map.put("parkNumber", escapeParkRecordEntities.get(i).getParkNo());
				    map.put("expense", String.valueOf(escapeParkRecordEntities.get(i).getPayMoney()) + "元");
				    map.put("carType", escapeParkRecordEntities.get(i).getCarType());
				    map.put("parkingRecordID", escapeParkRecordEntities.get(i).getFlowNo());
			    	res.getList().add(num++, map);
				}
			}else{
				res.setResult("100", "牌照确认成功");	
			}
		}else if( info.getType()==LICENSE_LEAVING_TYPE){
			if(parkEnterEntities.isEmpty()){
				res.setResult("205", "场内无此车辆");
			}else{
				res.getProperty().put("parkingEnterID", parkEnterEntities.get(0).getFlowNo());
				res.setResult("100", "牌照确认成功");	
			}
		}
		return res;
	}
	
	/*else if(parkRecordEntities!=null && parkRecordEntities.size()>0 && parkRecordEntities.get(0).getPaymentFlag()==0){
	    res.setResult("204", "欠费车辆");
		HashMap<String,Object> mapTitle = new HashMap<String,Object> ();
		mapTitle.put("licensePlateNumber", "车辆牌照");
		mapTitle.put("startTime","入场时间");
		mapTitle.put("leaveTime", "离场时间");
		mapTitle.put("parkNumber", "车场编号");
		mapTitle.put("expense", "费用");//暂时用应付金额替代实付金额
    	res.addListItem(mapTitle);
	    String format = "yyyy-MM-dd HH:mm:ss";
	    HashMap<String,Object> map = new HashMap<String,Object>();		
	    map.put("licensePlateNumber", parkRecordEntities.get(0).getPlate());
	    map.put("startTime", DateUtils.DateToString(parkRecordEntities.get(0).getEnterTime(), format));
	    map.put("leaveTime", DateUtils.DateToString(parkRecordEntities.get(0).getLeaveTime(), format));
	    map.put("parkNumber", parkRecordEntities.get(0).getParkNo());
	    map.put("expense", String.valueOf(parkRecordEntities.get(0).getPayMoney()) + "元");
	    map.put("carType", parkRecordEntities.get(0).getCarType());
	    map.put("parkingRecordID", parkRecordEntities.get(0).getFlowNo());
	    res.addListItem(map);
	}else if(parkRecordEntities!=null && parkRecordEntities.size()>0 && parkRecordEntities.get(0).getPaymentFlag()==9){
	    res.setResult("204", "逃费车辆");
		HashMap<String,Object> mapTitle = new HashMap<String,Object> ();
		mapTitle.put("licensePlateNumber", "车辆牌照");
		mapTitle.put("startTime","入场时间");
		mapTitle.put("leaveTime", "离场时间");
		mapTitle.put("parkNumber", "车场编号");
		mapTitle.put("expense", "费用");//暂时用应付金额替代实付金额
    	res.addListItem(mapTitle);
	    String format = "yyyy-MM-dd HH:mm:ss";
	    HashMap<String,Object> map = new HashMap<String,Object>();		
	    map.put("licensePlateNumber", parkRecordEntities.get(0).getPlate());
	    map.put("startTime", DateUtils.DateToString(parkRecordEntities.get(0).getEnterTime(), format));
	    map.put("leaveTime", DateUtils.DateToString(parkRecordEntities.get(0).getLeaveTime(), format));
	    map.put("parkNumber", parkRecordEntities.get(0).getParkNo());
	    map.put("expense", String.valueOf(parkRecordEntities.get(0).getPayMoney()) + "元");
	    map.put("carType", parkRecordEntities.get(0).getCarType());
	    map.put("parkingRecordID", parkRecordEntities.get(0).getFlowNo());
	    res.addListItem(map);
	}*/
	
	
	
	/*
	 * 将入场信息写入入场表
	 */
	public CommonResponse insertEntrance(EntranceInfo info){
        System.out.println(info.toString());
		CommonResponse res = new CommonResponse();
		if(info==null||info.getheader()==null ||info.getheader().getToken()==null||"".equals(info.getheader().getToken()) || info.getCarType()==null || "".equals(info.getCarType())
				|| info.getParkType()==null || "".equals(info.getParkType()) || info.getLicensePlateNumber()==null || "".equals(info.getLicensePlateNumber()) 
				||info.getParkingLocation()==null || "".equals(info.getParkingLocation()) || info.getParkNumber()==null || "".equals(info.getParkNumber()) || info.getEnterTime()==null || "".equals(info.getEnterTime())){
			res.setResult("201", "参数为空");
			return res;
		}
		if(!EhcacheUtil.isValidKey(info.getheader().getToken())){
			res.setResult("202", "登陆超时，请重新登陆");
			return res;
		}
		//生成新的入场信息
		Date date = new Date();
	    ParkEnterEntity parkEnterEntity = new ParkEnterEntity();
	    parkEnterEntity.setFlowNo(DateUtils.DateToString(date, "yyyyMMddHHmmss"));
	    parkEnterEntity.setParkNo(info.getParkNumber());
	    parkEnterEntity.setPlate(info.getLicensePlateNumber());
	    parkEnterEntity.setParkLot(info.getParkingLocation());
	    parkEnterEntity.setCarType(info.getCarType());
	    parkEnterEntity.setParkType(info.getParkType());
	    parkEnterEntity.setEnterTime(DateUtils.StringToDate(info.getEnterTime(), "yyyy-MM-dd HH:mm:ss"));
	    parkEnterEntity.setImg(info.getEnterImage());

	    ParkLotEntity parkLotQueryEntity = new ParkLotEntity();
	    parkLotQueryEntity.setNo(info.getParkingLocation());
	    parkLotQueryEntity.setParkNo(info.getParkNumber());
	    int status = 0;
	    if(parkLotApiService.findParkLotByPage(parkLotQueryEntity, null, null, null, null).getData().size()!=0){
		    status = parkLotApiService.findParkLotByPage(parkLotQueryEntity, null, null, null, null).getData().get(0).getStatus();
	    }else{
	    	res.setResult("203", "请确认泊位是否存在");
	    	return res;
	    }
	    System.out.println("insertEntrance->status is " + status);
	    if(status == 0){//“0”为“车位无车”，“1”为“车位有车”
		    int resultFlag = 0;
	    	 try{
				    if(parkEnterApiService.saveParkEnter(parkEnterEntity)==1){//如果插入成功
					    //刷新车位状态
					    ParkLotEntity parkLotInsertEntity = new ParkLotEntity();
					    parkLotInsertEntity.setParkNo(info.getParkNumber());
					    parkLotInsertEntity.setNo(info.getParkingLocation());
					    parkLotInsertEntity.setPlate(info.getLicensePlateNumber());
					    parkLotInsertEntity.setStatus(1);//“0”为“车位无车”，“1”为“车位有车”
					    parkLotInsertEntity.setTranMode(0);//暂不清楚明确含义，先存入“0”
					    parkLotInsertEntity.setUpdateTime(date);
				    	parkLotApiService.updateParkLot(parkLotInsertEntity);
				    	//刷新停车场剩余车位数
				    	ParkEntity parkEntity = parkApiService.findParkByNo(info.getParkNumber());
				    	if(parkEntity!=null){
					    	parkEntity.setRemainSpace(parkEntity.getRemainSpace()-1);
					    	parkApiService.updatePark(parkEntity);
				    	}
				    	resultFlag=1;
				    }
			  }catch(Exception e){
			    	e.printStackTrace();
			  }
	    	 if(resultFlag==1){
					res.setResult("100", "入场成功");
	    	 }else{
					res.setResult("204", "入场信息保存失败");
	    	 }
		    return res;
	    }else{
			res.setResult("205", "该泊位已被占用");
			return res;
	    }
	}
	
	
	/*
	 * 查询应付金额、车辆类别、停车类别、开始时间
	 */
	public CommonResponse settleAccount(SettleAccountInfo info){
        System.out.println(info.toString());
		CommonResponse res = new CommonResponse();
		if(info==null||info.getheader()==null ||info.getheader().getToken()==null||"".equals(info.getheader().getToken()) || info.getParkNumber()==null || "".equals(info.getParkNumber()) 
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
			    res.getProperty().put("parkingRecordID", parkRecordEntity.getFlowNo());
			    res.getProperty().put("tradeRecordID", parkRecordEntity.getTradeFlowNo());
			    res.getProperty().put("expensePrimary", String.valueOf(parkRecordEntity.getPayMoney()) + "元");
			    Double discount = 0.0;
			    if(!"".equals(info.getCouponID()) && info.getCouponID()!=null){
			    	CouponEntity couponEntity = new CouponEntity();
			    	couponEntity.setCouponID(info.getCouponID());
			    	List<CouponEntity> couponEntities = couponApiService.findCoupons(couponEntity,null,null);
			    	if(couponEntities!=null && couponEntities.size()>0){
			    		discount = couponEntities.get(0).getDenomination();
			    	}
			    }
			    res.getProperty().put("discount", String.valueOf(discount));
			    res.getProperty().put("expenseFinal", String.valueOf(parkRecordEntity.getPayMoney()-discount) );
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
			if(parkEnterEntities!=null && parkEnterEntities.size()>0){
				ParkEnterEntity parkEnterEntity = parkEnterEntities.get(0);
				parkRecordEntity.setFlag(0);//“0”表示正常，”1“表示无入场记录
				parkRecordEntity.setFlowNo("1" + DateUtils.DateToString(new Date(), "yyyyMMddHHmmss"));//开头"1"为路内停车类型，“2”为路外停车类型
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
				
				if("免费停车".equals(parkRecordEntity.getParkType())){
					parkRecordEntity.setPayMoney(0);
					parkRecordEntity.setPaymentFlag(10);
				}else{
					//int duringHours = (int)( (leaveTime.getTime()-enterTime.getTime())/(1000*60*60)) + 1;
					FeeRateEntity feeRateEntity=feeApiService.findByParkNo(parkRecordEntity.getParkNo());
	        		if(feeRateEntity!=null){
		        		if(feeRateEntity.getType()==1){//按次收费
		    				parkRecordEntity.setPayMoney(feeRateEntity.getFeeByCount().doubleValue());
		        		}else if(feeRateEntity.getType()==2){//按小时收费
		        			int FeeHours = (int)( ( (leaveTime.getTime()-enterTime.getTime())/(1000*60*60))-feeRateEntity.getFreeTimeLen()/60 ) + 1;
		        			if(FeeHours>=0){
			    				parkRecordEntity.setPayMoney(feeRateEntity.getFeeByTime().doubleValue()*FeeHours);
		        			}else{
		        				parkRecordEntity.setPayMoney(0);
		        			}
		        		}else{
		    				parkRecordEntity.setPayMoney(0);//未知费率暂时按0元计算
		        		}
	        		}else{
	    				parkRecordEntity.setPayMoney(0);//未知费率暂时按0元计算
	        		}		
					parkRecordEntity.setPaymentFlag(0);
				}
				//"0"表示未支付，”1“表示pos机支付，"2"表示微信支付，”3“表示支付支付，"4"表示微信扫码支付，”5“表示支付宝扫码支付，"6"表示微信刷卡支付，”７“表示支付宝刷卡支付，”8“表示余额支付，“9”表示逃费，“10”其他支付
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
						if(parkLotEntities!=null && parkLotEntities.size()>0){
							parkLotEntities.get(0).setTranMode(null);
							parkLotEntities.get(0).setStatus(0);
							parkLotEntities.get(0).setPlate("");
							parkLotEntities.get(0).setUpdateTime(DateUtils.StringToDate(info.getLeaveTime(), "yyyy-MM-dd HH:mm:ss"));
					    	parkLotApiService.updateParkLot(parkLotEntities.get(0));  
						}
				    	//刷新停车场剩余车位数
				    	ParkEntity parkEntity = parkApiService.findParkByNo(info.getParkNumber());
				    	if(parkEntity!=null){
					    	parkEntity.setRemainSpace(parkEntity.getRemainSpace()+1);
					    	parkApiService.updatePark(parkEntity);
				    	}
						inner.setResponse(info, res, parkRecordEntity);
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
			    if(parkRecordEntities!=null && parkRecordEntities.size()>0){
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
	 * 生成支付订单
	 */
	public CommonResponse pay(PaymentInfo info){
        System.out.println(info.toString());
		CommonResponse res = new CommonResponse();
		if(info==null||info.getheader()==null ||info.getheader().getToken()==null||"".equals(info.getheader().getToken()) || info.getParkNumber()==null || "".equals(info.getParkNumber()) || info.getLicensePlateNumber()==null || "".equals(info.getLicensePlateNumber())
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
					sleep(2*60*60*1000);//将2小时改为2分钟用于调试
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
					   						        //"0"表示未支付，”1“表示pos机支付，"2"表示微信支付，”3“表示支付支付，"4"表示微信扫码支付，”5“表示支付宝扫码支付，"6"表示微信刷卡支付，”７“表示支付宝刷卡支付，”8“表示余额支付，“9”表示逃费
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
	   					    	    updateParkRecord.setPaymentFlag(9);
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
		if(parkRecordEntities!=null){
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
		}
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
		}else{
			//生成新的交易记录
			tradeRecordEntity.setFlowNo(DateUtils.DateToString(new Date(), "yyyyMMddHHmmss"));
			tradeRecordEntity.setIndustryFlag("1");//暂时将行业标识记录为“１”
			tradeRecordEntity.setTradeType(1);//"支付"类别为"1"，“充值”类别为“２”
			tradeRecordEntity.setTradeFlag(info.getLicensePlateNumber());
			tradeRecordEntity.setServiceFlow(info.getParkingRecordID());
			tradeRecordEntity.setServiceEntityFlow(info.getParkNumber());
			tradeRecordEntity.setPayMode(0);//生成订单时置为“未付”
			tradeRecordEntity.setResult(0);//"0"表示待收，”1“表示已收
		    tradeRecordEntity.setOrderTime(new Date());
		    tradeRecordEntity.setOrderTimeStr(new Date().toString());
		    isFirstOrdered = true;
		}
		System.out.println("pay->tradeRecordID is " + tradeRecordEntity.getFlowNo());
		res.getProperty().put("tradeRecordID", tradeRecordEntity.getFlowNo());//返回交易记录ID用于解决首次支付宝扫码/条码支付时缺少参量问题
		if(parkRecordEntities==null || parkRecordEntities.size()==0){
			  res.setResult("203", "不存在对应停车记录");
		}else{
			int resultFlag = 0;
		    ParkRecordEntity parkRecordEntity = parkRecordEntities.get(0);
		    try{
				if(isFirstOrdered){
					tradeRecordApiService.save(tradeRecordEntity);
					ParkRecordEntity parkRecord = new  ParkRecordEntity();
					parkRecord.setParkNo(info.getParkNumber());
					parkRecord.setFlowNo(info.getParkingRecordID());
					parkRecord.setTradeFlowNo(tradeRecordEntity.getFlowNo());
					parkRecordApiService.updateParkRecord(parkRecord);
				}
			    Double paidMoney = Double.parseDouble(info.getPaidMoney());
			    Double discount = 0.0;
			    if(!"".equals(info.getCouponID()) && info.getCouponID()!=null){
			    	CouponEntity couponEntity = new CouponEntity();
			    	couponEntity.setCouponID(info.getCouponID());
			    	List<CouponEntity> couponEntities = couponApiService.findCoupons(couponEntity,null,null);
			    	if(couponEntities!=null && couponEntities.size()>0){
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
	                if(info.getPaymentPattern().equals(1)){//pos机支付{
				        //TODO	
				    }else if(info.getPaymentPattern().equals(4)){//微信扫码支付{
	                	WechatPayment wechatPayment = new WechatPayment();
	                	HashMap<String,Object> map = wechatPayment.pay(4,null, (int)(Double.valueOf(info.getPaidMoney()).doubleValue()*100), "停车费", info.getTradeRecordID());
						if(!"".equals(map.get("code_url")) &&map.get("code_url")!=null){
							res.getProperty().put("code_url", map.get("code_url"));
							resultFlag = 1;
						}
				    }else if(info.getPaymentPattern().equals(5)){//支付宝扫码支付{
				    	AlipayPayment alipayPayment  = new AlipayPayment();
				    	String codeUrl = alipayPayment.scanPay(info.getPaidMoney(), "停车费", info.getTradeRecordID());
				    	if(!"".equals(codeUrl) && codeUrl !=null){
				    		res.getProperty().put("code_url", codeUrl);
							resultFlag = 1;
				    	}
				    }else if(info.getPaymentPattern().equals(6)){//微信刷卡支付{
	                	WechatPayment wechatPayment = new WechatPayment();
	                	wechatPayment.pay(6,null, (int)(Double.valueOf(info.getPaidMoney()).doubleValue()*100), "停车费", info.getTradeRecordID(), info.getAuthCode());
	                	resultFlag = 1;
				    }else if(info.getPaymentPattern().equals(7)){//支付宝条码支付{
				    	AlipayPayment alipayPayment  = new AlipayPayment();
				    	alipayPayment.codePay(info.getPaidMoney(), "停车费", info.getTradeRecordID(), info.getAuthCode());
				    	resultFlag = 1;
				    }else if(info.getPaymentPattern().equals(0)){//未付
				    	resultFlag = 1;
				    }
			     }
		    }catch(Exception e){
		    	e.printStackTrace();
		    }
		    if(resultFlag==1){
		 	    res.setResult("100", "生成支付订单成功");
		    }else{
		         res.setResult("204", "生成支付订单失败");
		    }
		}
		return res;
	}
	
	
	/*
	 * 查询支付结果
	 */
	public  CommonResponse queryResult(QueryResultInfo info){
		System.out.println(info.toString());
		CommonResponse res = new CommonResponse();
		if(info==null||info.getheader()==null || info.getheader()==null ||info.getheader().getToken()==null||"".equals(info.getheader().getToken()) || info.getOrderID()==null || "".equals(info.getOrderID())){
			res.setResult("201", "参数为空");
			return res;
		}
		if(!EhcacheUtil.isValidKey(info.getheader().getToken())){
			res.setResult("202", "登陆超时，请重新登陆");
			return res;
		}
		int resultFlag = 0 ;
		TradeRecordEntity entity = new TradeRecordEntity();
		entity.setFlowNo(info.getOrderID());
		List<TradeRecordEntity> tradeRecordEntities = tradeRecordApiService.findTradeRecordsByPage(entity, null, null, null, null).getData();
		System.out.println("queryResult->tradeRecordEntities is " + tradeRecordEntities );
		System.out.println("queryResult->tradeRecordEntities.get(0).getResult() is " + tradeRecordEntities.get(0).getResult() );
		if(tradeRecordEntities!=null && tradeRecordEntities.size()>0){
			if(tradeRecordEntities.get(0).getResult()==1){
				resultFlag=1;
			}else if(tradeRecordEntities.get(0).getResult()==0){
		    	AlipayPayment alipayPayment  = new AlipayPayment();
		    	alipayPayment.query(info.getType(), tradeRecordEntities.get(0).getPayMode(),info.getOrderID(), null);
			}
		}
   	   if(resultFlag==1){
	      res.setResult("100", "三方平台支付成功");
   	   }else{
          res.setResult("203", "三方平台支付失败");
   	   }
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
		PersonAccessEntity collectorEntity= personAccessApiService.findPersionAccess(info.getheader().getAccount());
		if(collectorEntity==null){
			res.setResCode("203");
			res.setResMsg("用户名不存在");
			return res;
		}
		if(!collectorEntity.getPword().equals(info.getPassword())){
			res.setResCode("204");
			res.setResMsg("原密码错误");
			return res;
		}
		collectorEntity.setPword(info.getNewPassword());
		if(personAccessApiService.updatePersionAccessPword(collectorEntity) == -1){
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
	 * 历史记录查询
	 */
	public CommonResponse searchHistoryRecord(HistoryRecordSearchInfo info){
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
		String format = "yyyy-MM-dd HH:mm:ss";
		String startStr = info.getDate() + " " + "00:00:00";
		String endStr = info.getDate() + " " + "23:59:59";
        Date startTime = DateUtils.StringToDate(startStr,format); 
        Date endTime = DateUtils.StringToDate(endStr, format); 
        List<ParkRecordEntity> parkRecordEntities;
        if(info.getPaymentPattern()==12/*微信*/){
        	List<ParkRecordEntity> wechatAppEntities = parkRecordApiService.findByParkNoAndPaymentPattern(info.getParkNumber(), 2/*微信APP支付*/ ,startTime, endTime);
        	List<ParkRecordEntity> wechatScanEntities = parkRecordApiService.findByParkNoAndPaymentPattern(info.getParkNumber(), 4/*微信扫码支付*/ ,startTime, endTime);
        	List<ParkRecordEntity> wechatCardEntities = parkRecordApiService.findByParkNoAndPaymentPattern(info.getParkNumber(), 6/*微信刷卡支付*/ ,startTime, endTime);
        	parkRecordEntities=wechatAppEntities;
        	parkRecordEntities.addAll(wechatScanEntities);
        	parkRecordEntities.addAll(wechatCardEntities);
        }else if(info.getPaymentPattern()==15/*支付宝*/){
        	List<ParkRecordEntity> alipayAppEntities = parkRecordApiService.findByParkNoAndPaymentPattern(info.getParkNumber(), 3/*支付宝APP支付*/ ,startTime, endTime);
        	List<ParkRecordEntity> alipayScanEntities = parkRecordApiService.findByParkNoAndPaymentPattern(info.getParkNumber(), 5/*支付宝扫码支付*/ ,startTime, endTime);
        	List<ParkRecordEntity> alipayCodeEntities = parkRecordApiService.findByParkNoAndPaymentPattern(info.getParkNumber(), 7/*支付宝条码支付*/ ,startTime, endTime);
        	parkRecordEntities=alipayAppEntities;
        	parkRecordEntities.addAll(alipayScanEntities);
        	parkRecordEntities.addAll(alipayCodeEntities);
        }else{
        	parkRecordEntities = parkRecordApiService.findByParkNoAndPaymentPattern(info.getParkNumber(), info.getPaymentPattern(), startTime, endTime);
        }
    	ParkRecordEntity parkRecordEntity = new ParkRecordEntity();
    	if(parkRecordEntities.size()!=0){
    		HashMap<String,Object> mapTitle = new HashMap<String,Object> ();
    		mapTitle.put("licensePlateNumber", "车辆牌照");
    		mapTitle.put("startTime","入场时间");
    		mapTitle.put("leaveTime", "离场时间");
    		mapTitle.put("parkingLocation", "泊位");
    		mapTitle.put("expense", "费用");//暂时用应付金额替代实付金额
        	res.getList().add(0, mapTitle);
        	for(int i = 0 ; i < parkRecordEntities.size() ; i++) {
            	HashMap<String,Object> mapItem = new HashMap<String,Object> ();
        		parkRecordEntity = parkRecordEntities.get(i);
        		mapItem.put("licensePlateNumber", parkRecordEntity.getPlate());
        		mapItem.put("startTime", DateUtils.DateToString(parkRecordEntity.getEnterTime(), format));
        		mapItem.put("leaveTime", DateUtils.DateToString(parkRecordEntity.getLeaveTime(), format));
        		mapItem.put("parkingLocation", parkRecordEntity.getParkLot());
        		mapItem.put("expense", String.valueOf(parkRecordEntity.getPayMoney() + "元"));//应付金额
            	res.getList().add(i+1, mapItem);
            }
    	}
		res.setResult("100", "查询成功");
        return res;
	}
	
	
	/*
	 * 今日记录查询
	 */
	public CommonResponse searchTodayRecord(TodayRecordSearchInfo info){
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
        Date startTime = DateUtils.StringToDate(startStr,formatTime); 
        Date endTime = DateUtils.StringToDate(endStr, formatTime); 
        
    	List<ParkRecordEntity> parkRecordEntities = parkRecordApiService.findByParkNoAndParkLot(info.getParkNumber(), info.getParkingLocation(), startTime, endTime);
    	ParkRecordEntity parkRecordEntity = new ParkRecordEntity();
    	if(parkRecordEntities!=null && parkRecordEntities.size()>0){
    		HashMap<String,Object> mapTitle = new HashMap<String,Object> ();
    		mapTitle.put("licensePlateNumber", "车辆牌照");
    		mapTitle.put("startTime","入场时间");
    		mapTitle.put("leaveTime", "离场时间");
    		mapTitle.put("paymentPattern", "支付类型");
    		mapTitle.put("expense", "费用");//暂时用应付金额替代实付金额
        	res.getList().add(0, mapTitle);
        	for(int i = 0 ; i < parkRecordEntities.size() ; i++) {
        		parkRecordEntity = parkRecordEntities.get(i);
        		HashMap<String,Object> mapItem = new HashMap<String,Object> ();
        		mapItem.put("licensePlateNumber", parkRecordEntity.getPlate());
        		mapItem.put("startTime", DateUtils.DateToString(parkRecordEntity.getEnterTime(), formatTime));
        		mapItem.put("leaveTime", DateUtils.DateToString(parkRecordEntity.getLeaveTime(), formatTime));
        		mapItem.put("paymentPattern", PaymentConvertUtils.covertToString(parkRecordEntity.getPaymentFlag()));
        		mapItem.put("expense", String.valueOf(parkRecordEntity.getPayMoney()) + "元");//应付金额
            	res.getList().add(i+1, mapItem);
            }
    	}
    	res.setResult("100", "查询成功");     
        return res;
	}
	
	
	/*
	 * 停车场泊位状态查询
	 */
	public CommonResponse queryParkLot(ParkLotQueryInfo info){
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
		ParkEntity parkEntity = new ParkEntity();
		parkEntity.setNo(info.getParkNumber());
		List<ParkEntity> parkEntities = parkApiService.findParks(parkEntity, null, null, null, null);
		if(parkEntities !=null && parkEntities.size()>0){
			res.getProperty().put("totalLocationNumber", parkEntities.get(0).getTotalSpace());
			res.getProperty().put("idleLocationNumber", parkEntities.get(0).getRemainSpace());
			ParkLotEntity parkLotEntity = new ParkLotEntity();
			parkLotEntity.setParkNo(info.getParkNumber());
			List<ParkLotEntity> parkLotEntities = parkLotApiService.findParkLotByPage(parkLotEntity, null, null, null, null).getData();
	    	for(int i = parkLotEntities.size()-1 ; i >=0 ; i--) {
		    	HashMap<String,Object> map = new HashMap<String,Object> ();
	    		map.put("parkingLocation", parkLotEntities.get(i).getNo());
	    		map.put("licensePlateNumber", parkLotEntities.get(i).getPlate());
	        	res.addListItem(map);
	        }
			res.setResult("100", "查询成功");
		}else{
			res.setResult("203", "查询失败");
		}
		return res;
	}
	
	
	/*
	 * 指定泊位停车信息查询
	 */
	public CommonResponse queryParkLotDetail(LotDetailQueryInfo info){
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
		List<ParkEnterEntity> parkEnterEntities = parkEnterApiService.findByParkNoAndParkLot(info.getParkNumber(), info.getParkingLocation());
		if(parkEnterEntities!=null && parkEnterEntities.size()>0){
			ParkEnterEntity parkEnterEntity = parkEnterEntities.get(0);
	        System.out.println("queryParkLotDetail->parkEnterEntity:" + parkEnterEntity.toString());
			res.getProperty().put("parkingEnterID", parkEnterEntity.getFlowNo());
			res.getProperty().put("carType", parkEnterEntity.getCarType());
			res.getProperty().put("parkType", parkEnterEntity.getParkType());
			res.getProperty().put("startTime", DateUtils.DateToString(parkEnterEntity.getEnterTime(), "yyyy-MM-dd HH:mm:ss"));
			res.setResult("100", "查询成功");
		}else{
			res.setResult("203", "查询失败");
		}
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
        int i = 2;
        while(i>0){
            HashMap<String, Object> map=new HashMap<String, Object>();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");       
            Date curDate = new Date(System.currentTimeMillis());//获取当前时间       
            String date = formatter.format(curDate); 
            if(i==1){
                map.put("messageTitle", "考勤通知");
                map.put("messageAbstract", "您9月30日出现一次考勤异常");
                map.put("messageTime", date);
                map.put("messageDetail", "您9月30日上班打卡时间08:40:36(上班时间9:00)，" +
                		"下班打卡时间15:30:23(下班时间17:30)，存在异常，请联系考勤员确认。");
                res.addListItem(map);
            }else if(i==2){
                map.put("messageTitle", "停车通知");
                map.put("messageAbstract", "9月30日出现一次停车逃费现象");
                map.put("messageTime", date);
                map.put("messageDetail", "9月30日出现一次停车逃费现象，入场时间11:15:36，" +
                		"牌照号津A00001，泊位号6，请联系稽查员确认。");
                res.addListItem(map);
            }
            i--;
        }*/
        res.setResult("100", "获取信息成功");
		return res;
	}
}
