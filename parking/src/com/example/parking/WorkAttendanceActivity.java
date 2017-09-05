package com.example.parking;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.net.ParseException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class WorkAttendanceActivity extends Activity {
    private static final String FILE_NAME_ATTENDANCE = "save_spref_attendance";
    private static final String FILE_NAME_COLLECTOR = "save_pref_collector";
    private static final String FILE_NAME_TOKEN = "save_pref_token";
	private static final int ATTENDANCE_TYPE_START=301;
	private static final int ATTENDANCE_TYPE_END=302;
	private static final int EVENT_UPDATE_INFORMATION = 201; 
	private static final int EVENT_DISPLAY_LOCATION_STATE = 202;
	private static final int EVENT_DISPLAY_TIME_START = 203;
	private static final int EVENT_DISPLAY_TIME_END = 204;
	private static final int EVENT_ATTENDANCE_START_SUCCESS = 205;
	private static final int EVENT_ATTENDANCE_END_SUCCESS = 206;
	private static final int EVENT_ENTER_MAIN = 207;
	private static final int EVENT_EXIT_LOGIN = 208;
	public static String LOG_TAG = "WorkAttendanceActivity";
	private int mType;
	private Button mAttendanceBT;
	private TextView mParkNumberTV;
	private TextView mUserNumberTV;
	private TextView mAttendanceWorkStartTimeTV;
	private TextView mAttendanceWorkEndTimeTV;
	private TextView mAttendanceStartLocationTV;
	private TextView mAttendanceEndLocationTV;
	private TextView mAttendanceDateTV;
	private TextView mLocationStateTV;
	private Context mContext;
    
	private UserUpdateInformationTask mUserUpdateInformationTask = null;
    private UserReprotLocationTask mUserReprotLocationTask = null;
    private UserClockTask mUserClockTask = null;
    
    private Integer mLocationState =0;//“０”为考勤范围外，"１"为考勤范围内
    
    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
  //声明mLocationOption对象
    public AMapLocationClientOption mLocationOption = null;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_attendance);
		mContext = this;
		//初始化定位
		mLocationClient = new AMapLocationClient(getApplicationContext());
		//设置定位回调监听
		mLocationClient.setLocationListener(mLocationListener);
		setUpMap();
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		mType = bundle.getInt("attendancetype");
		mParkNumberTV = (TextView)findViewById(R.id.tv_attendance_park_number);
		mUserNumberTV = (TextView)findViewById(R.id.tv_attendance_user_number);
		mAttendanceDateTV=(TextView)findViewById(R.id.tv_attendance_date);
		SimpleDateFormat formatter = new SimpleDateFormat ("yyyy年MM月dd日"); 
		Date curDate = new Date(System.currentTimeMillis());
		String dateStr = formatter.format(curDate);
		mAttendanceDateTV.setText(dateStr);
		mAttendanceWorkStartTimeTV=(TextView)findViewById(R.id.tv_attendance_work_start_time);
		mAttendanceWorkEndTimeTV=(TextView)findViewById(R.id.tv_attendance_work_end_time);
		mAttendanceStartLocationTV=(TextView)findViewById(R.id.tv_attendance_start_location);
		mAttendanceEndLocationTV=(TextView)findViewById(R.id.tv_attendance_end_location);
		mLocationStateTV=(TextView)findViewById(R.id.tv_location_state);
		mAttendanceBT=(Button)findViewById(R.id.bt_work_attendance);
		mAttendanceBT.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View  v){
			    mUserClockTask = new UserClockTask();
			    mUserClockTask.execute((Void) null);
			}
		});
        if(mType==ATTENDANCE_TYPE_START){
		    mUserUpdateInformationTask = new UserUpdateInformationTask();
		    mUserUpdateInformationTask.execute((Void) null);
	    }else if(mType == ATTENDANCE_TYPE_END){
       	      Message msg = new Message();
             msg.what = EVENT_UPDATE_INFORMATION;
             mHandler.sendMessage(msg);
			 new TimeThread().start();
	    }
		getActionBar().setDisplayHomeAsUpEnabled(true); 
	}

	public class TimeThread extends Thread {
        @Override
        public void run () {
            do {
                try {
                    Thread.sleep(1000);
                    Message msg = new Message();
                    if(mType==ATTENDANCE_TYPE_START){
                    	msg.what = EVENT_DISPLAY_TIME_START;
                    }else if(mType==ATTENDANCE_TYPE_END){
                    	msg.what = EVENT_DISPLAY_TIME_END;
                    }
                    mHandler.sendMessage(msg);
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } while(true);
        }
    }

	private Handler mHandler = new Handler() {
        @Override
        public void handleMessage (Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
               case EVENT_UPDATE_INFORMATION:
            	   Log.e("clientRequest","EVENT_UPDATE_INFORMATION");
           		   mParkNumberTV.setText("车场编号:" + readCollector("parkNumber"));
           		   mUserNumberTV.setText("工号:" + readCollector("collectorNumber"));
           		   mAttendanceWorkStartTimeTV.setText("上班时间:" + readCollector("workStartTime"));
           	       mAttendanceWorkEndTimeTV.setText("下班时间:" + readCollector("workEndTime"));
            	   break;
               case EVENT_DISPLAY_LOCATION_STATE:
            	   mLocationStateTV.setText("当前位置:" + (String)msg.obj);
            	   break;
                case EVENT_DISPLAY_TIME_START:
                    CharSequence sysTimeStrStart = DateFormat.format("HH:mm:ss", System.currentTimeMillis());
                    mAttendanceBT.setText("上班打卡\n" + sysTimeStrStart);
                    break;
                case EVENT_DISPLAY_TIME_END:
                    CharSequence sysTimeStrEnd = DateFormat.format("HH:mm:ss", System.currentTimeMillis());
                    mAttendanceBT.setText("下班打卡\n" + sysTimeStrEnd);
                    break;
                case EVENT_ATTENDANCE_START_SUCCESS:
                	 String startTime = "打卡时间:" + mAttendanceBT.getText().toString().replaceAll("上班打卡\n", "") + "(" 
                                       +	readCollector("workStartTime") + ")";
                	 String startLocation = mAttendanceStartLocationTV.getText().toString();
                	 writeAttendance(startTime,startLocation);
    				 mAttendanceWorkStartTimeTV.setText(startTime);
                     Toast.makeText(getApplicationContext(), "打卡成功，即将进入主页", Toast.LENGTH_SHORT).show();
                	 break;
                case EVENT_ATTENDANCE_END_SUCCESS:
                	mAttendanceWorkEndTimeTV.setText("打卡时间:" + mAttendanceBT.getText().toString().replaceAll("下班打卡\n", "")
    							+ "(" + readCollector("workEndTime") + ")");
                     Toast.makeText(getApplicationContext(), "打卡成功，即将退出登录", Toast.LENGTH_SHORT).show();
                	 break;
                case EVENT_ENTER_MAIN:
                	Intent intentMain = new Intent(WorkAttendanceActivity.this,MainActivity.class);
                	startActivity(intentMain);
                	finish();
                	break;
                case EVENT_EXIT_LOGIN:
                    Intent intentFinsh = new Intent();  
                    intentFinsh.setAction("ExitApp");  
                    sendBroadcast(intentFinsh); 
                	Intent intentLogin = new Intent(WorkAttendanceActivity.this,LoginActivity.class);
                	startActivity(intentLogin);
                	finish();
                	break;
                default:
                    break;
            }
        }
    };

    private String readAttendance(String data) {
        SharedPreferences pref = getSharedPreferences(FILE_NAME_ATTENDANCE, MODE_MULTI_PROCESS);
        String str = pref.getString(data, "");
        return str;
    }

    private boolean writeAttendance(String attendancestarttime,String attendancestartlocation) {
        SharedPreferences.Editor share_edit = getSharedPreferences(FILE_NAME_ATTENDANCE,
                MODE_MULTI_PROCESS).edit();
        share_edit.putString("attendancestarttime", attendancestarttime);
        share_edit.putString("attendancestartlocation", attendancestartlocation);
        share_edit.commit();
        return true;
    }
    
    private String readCollector(String data) {
        SharedPreferences pref = getSharedPreferences(FILE_NAME_COLLECTOR, MODE_MULTI_PROCESS);
        String str = pref.getString(data, "");
        return str;
    }

    private boolean writeCollector(String parkName, String parkNumber, String workStartTime, 
    		String workEndTime, String feeScale, String chargeStandard, String superviseTelephone) {
        SharedPreferences.Editor share_edit = getSharedPreferences(FILE_NAME_COLLECTOR,
                MODE_MULTI_PROCESS).edit();
        share_edit.putString("parkName", parkName);
        share_edit.putString("parkNumber", parkNumber);
        share_edit.putString("workStartTime", workStartTime);
        share_edit.putString("workEndTime", workEndTime);
        share_edit.putString("feeScale", feeScale);
        share_edit.putString("chargeStandard", chargeStandard);
        share_edit.putString("superviseTelephone", superviseTelephone);
        share_edit.commit();
        return true;
    }
    
    private String readToken() {
        SharedPreferences pref = getSharedPreferences(FILE_NAME_TOKEN, MODE_MULTI_PROCESS);
        String str = pref.getString("token", "");
        return str;
    }
    
    private String readAccount() {
        SharedPreferences pref = getSharedPreferences(FILE_NAME_COLLECTOR, MODE_MULTI_PROCESS);
        String str = pref.getString("collectorNumber", "");
        return str;
    }
    
	public boolean onOptionsItemSelected(MenuItem item) {  
	    switch (item.getItemId()) {  
	         case android.R.id.home:  
	     		if(mType==ATTENDANCE_TYPE_START){
                	Intent intent = new Intent(WorkAttendanceActivity.this,LoginActivity.class);
                	startActivity(intent);
	     		}else if(mType==ATTENDANCE_TYPE_END){
                	Intent intent = new Intent(WorkAttendanceActivity.this,MainActivity.class);
                	startActivity(intent);
	    		}
	             finish();  
	             break;    
	        default:  
	             break;  
	    }  
	    return super.onOptionsItemSelected(item);  
	  }  
	
    @Override  
    protected void onDestroy() {  
        super.onDestroy();  
        mLocationClient.unRegisterLocationListener(mLocationListener);
    }

    
    /**
     * 配置定位参数
     */
    private void setUpMap() {
     //初始化定位参数
     mLocationOption = new AMapLocationClientOption();
     //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
     mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
     //设置是否返回地址信息（默认返回地址信息）
     mLocationOption.setNeedAddress(true);
     //设置是否只定位一次,默认为false
     mLocationOption.setOnceLocation(false);
     //设置是否强制刷新WIFI，默认为强制刷新
     mLocationOption.setWifiActiveScan(true);
     //设置是否允许模拟位置,默认为false，不允许模拟位置
     mLocationOption.setMockEnable(false);
     //设置定位间隔,单位毫秒,默认为2000ms
     mLocationOption.setInterval(2000);
     //给定位客户端对象设置定位参数
     mLocationClient.setLocationOption(mLocationOption);
     //启动定位
     mLocationClient.startLocation();
    }
    
    /**
     * 声明定位回调监听器
     */
    public AMapLocationListener mLocationListener = new AMapLocationListener() {
     @Override
     public void onLocationChanged(AMapLocation amapLocation) {
        if (amapLocation != null) {
           if (amapLocation.getErrorCode() == 0) {
            //定位成功回调信息，设置相关消息
                amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
                amapLocation.getLatitude();//获取纬度
                amapLocation.getLongitude();//获取经度
                amapLocation.getAccuracy();//获取精度信息
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = new Date(amapLocation.getTime());
                df.format(date);//定位时间
                amapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
                amapLocation.getCountry();//国家信息
                amapLocation.getProvince();//省信息
                amapLocation.getCity();//城市信息
                amapLocation.getDistrict();//城区信息
                amapLocation.getStreet();//街道信息
                amapLocation.getStreetNum();//街道门牌号信息
                amapLocation.getCityCode();//城市编码
                amapLocation.getAdCode();//地区编码
                amapLocation.getAoiName();//获取当前定位点的AOI信息
                Drawable drawable = getResources().getDrawable(R.drawable.ic_add_location_black_18dp);
        		drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight()); //设置边界
                if(mType==ATTENDANCE_TYPE_START){
                	String location = amapLocation.getProvince() + "" + amapLocation .getDistrict() + "" + amapLocation.getStreet() + "" + amapLocation.getStreetNum();
    			    mAttendanceStartLocationTV.setText(location);
    			    mAttendanceStartLocationTV.setCompoundDrawables(drawable, null, null, null);//画在左边
    		    }else if(mType==ATTENDANCE_TYPE_END){
    			    mAttendanceWorkStartTimeTV.setText(readAttendance("attendancestarttime"));
    			    mAttendanceStartLocationTV.setText(readAttendance("attendancestartlocation"));
    			    mAttendanceStartLocationTV.setCompoundDrawables(drawable, null, null, null);//画在左边
    			    String location = amapLocation.getProvince() + "" + amapLocation.getDistrict() + "" + amapLocation.getStreet() + "" + amapLocation.getStreetNum();
    		        mAttendanceEndLocationTV.setText(location);
    			    mAttendanceEndLocationTV.setCompoundDrawables(drawable, null, null, null);//画在左边		
    		    }
			    mUserReprotLocationTask = new UserReprotLocationTask(amapLocation);
			    mUserReprotLocationTask.execute((Void) null);
           } else {
                //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                Log.e("AmapError", "location Error, ErrCode:" + amapLocation.getErrorCode() + ", errInfo:" + amapLocation.getErrorInfo());
           }
        }
     }
    };
    
    /**
   	 * Add for report request collector's information 
   	 * */
   	public boolean clientRequestInformation() throws ParseException, IOException, JSONException{
   		  HttpClient httpClient = new DefaultHttpClient();
   		  httpClient.getParams().setIntParameter(  
                     HttpConnectionParams.SO_TIMEOUT, 5000); // 请求超时设置,"0"代表永不超时  
   		  httpClient.getParams().setIntParameter(  
                     HttpConnectionParams.CONNECTION_TIMEOUT, 5000);// 连接超时设置 
   		  String strurl = "http://" + this.getString(R.string.ip) + "/itspark/collector/workAttendance/requestInformation";
   		  HttpPost request = new HttpPost(strurl);
   		  request.addHeader("Accept","application/json");
		  //request.setHeader("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
		  request.setHeader("Content-Type", "application/json; charset=utf-8");
   		  JSONObject param = new JSONObject();
   		  TokenInfo info = new TokenInfo();
		  CommonRequestHeader header = new CommonRequestHeader();
		  header.addRequestHeader(CommonRequestHeader.REQUEST_COLLECTOR_GET_WORK_CODE, readAccount(), readToken());
		  info.setHeader(header);
   		  StringEntity se = new StringEntity(JacksonJsonUtil.beanToJson(info), "UTF-8");
		  Log.e(LOG_TAG,"clientRequest-> param is " + JacksonJsonUtil.beanToJson(info));
   		  request.setEntity(se);//发送数据
   		  try{
   			  HttpResponse httpResponse = httpClient.execute(request);//获得响应
   			  int code = httpResponse.getStatusLine().getStatusCode();
   			  if(code==HttpStatus.SC_OK){
   				  String strResult = EntityUtils.toString(httpResponse.getEntity());
   				  Log.e(LOG_TAG,"clientRequest->strResult is " + strResult);
   				  CommonResponse res = new CommonResponse(strResult);
   				  String parkName = (String)res.getPropertyMap().get("parkName");
   				  String parkNumber = (String)res.getPropertyMap().get("parkNumber");
   				  String workStartTime = (String)res.getPropertyMap().get("workStartTime");
   				  String workEndTime = (String)res.getPropertyMap().get("workEndTime");
   				  String feeScale = (String)res.getPropertyMap().get("feeScale");
   				  String chargeStandard = (String)res.getPropertyMap().get("chargeStandard");
   				  String superviseTelephone = (String)res.getPropertyMap().get("superviseTelephone");
   				  if(writeCollector(parkName,parkNumber,workStartTime,workEndTime,feeScale,chargeStandard,superviseTelephone)){
   	   				  Log.e(LOG_TAG,"clientRequest->writeCollector ok"); 
   				  }
   				  toastWrapper(res.getResMsg());
   				  String resCode = res.getResCode();
   				  if(resCode.equals("100")){
   					  return true;
   				  }else {
   					  return false;
   				  }
   			  }else{
   				  Log.e(LOG_TAG, "clientRequest-> error code is " + Integer.toString(code));
   				  return false;
   			  }
   		  }catch(InterruptedIOException e){
   			  if(e instanceof ConnectTimeoutException){
   				  toastWrapper("连接超时");  
   			  }else if(e instanceof InterruptedIOException){
   				  toastWrapper("请求超时");  
   			  }
             }finally{  
           	  httpClient.getConnectionManager().shutdown();  
             }  
   		  return false;
       } 
   	/**
   	 * 用户获取相关信息的Task
   	 * 
   	 */
   	public class UserUpdateInformationTask extends AsyncTask<Void, Void, Boolean> {
   		@Override
   		protected Boolean doInBackground(Void... params) {
   			try{
   			    return clientRequestInformation();
   			}catch(Exception e){
   				e.printStackTrace();
   			}
   			return false;
   		}

   		@Override
   		protected void onPostExecute(final Boolean success) {
   			mUserUpdateInformationTask = null;
   			Log.e(LOG_TAG,"clientRequest-> onPostExecute  " + success.toString());
   			if(success){
 	        	  Message msg = new Message();
   	              msg.what = EVENT_UPDATE_INFORMATION;
   	              mHandler.sendMessage(msg);
   				  new TimeThread().start(); 
   			}
   		}

   		@Override
   		protected void onCancelled() {
   			mUserUpdateInformationTask = null;
   		}
   		
   	}
    
    
    
    /**
	 * Add for report current location 
	 * */
	public boolean clientReportLocation(AMapLocation amapLocation) throws ParseException, IOException, JSONException{
		  HttpClient httpClient = new DefaultHttpClient();
		  httpClient.getParams().setIntParameter(  
                  HttpConnectionParams.SO_TIMEOUT, 5000); // 请求超时设置,"0"代表永不超时  
		  httpClient.getParams().setIntParameter(  
                  HttpConnectionParams.CONNECTION_TIMEOUT, 5000);// 连接超时设置 
		  String strurl = "http://" + this.getString(R.string.ip) + "/itspark/collector/workAttendance/reportLocation";
		  HttpPost request = new HttpPost(strurl);
		  request.addHeader("Accept","application/json");
		  //request.setHeader("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
		  request.setHeader("Content-Type", "application/json; charset=utf-8");
		  JSONObject param = new JSONObject();
		  LocationInfo info = new LocationInfo();
		  CommonRequestHeader header = new CommonRequestHeader();
		  header.addRequestHeader(CommonRequestHeader.REQUEST_COLLECTOR_GET_LOCATION_STATE_CODE, readAccount(), readToken());
		  info.setHeader(header);
		  info.setLatitude(amapLocation.getLatitude());
		  info.setLongitude(amapLocation.getLongitude());
		  StringEntity se = new StringEntity(JacksonJsonUtil.beanToJson(info), "UTF-8");
		  Log.e(LOG_TAG,"clientReport-> param is " + JacksonJsonUtil.beanToJson(info));
		  request.setEntity(se);//发送数据
		  try{
			  HttpResponse httpResponse = httpClient.execute(request);//获得响应
			  int code = httpResponse.getStatusLine().getStatusCode();
			  if(code==HttpStatus.SC_OK){
				  String strResult = EntityUtils.toString(httpResponse.getEntity());
				  Log.e(LOG_TAG,"clientReport-> strResult is " + strResult);
				  CommonResponse res = new CommonResponse(strResult);
	        	  Message msg = new Message();
	              msg.what = EVENT_DISPLAY_LOCATION_STATE;
	              msg.obj = res.getResMsg();
	              mHandler.sendMessage(msg);
				  String resCode = res.getResCode();
   				  if(resCode.equals("100")){
   					mLocationState = 1;
   					  return true;
   				  }else{
   					mLocationState = 0;
   					  return false;
   				  }
			  }else{
				  Log.e(LOG_TAG, "clientReport-> error code is " + Integer.toString(code));
				  return false;
			  }
		  }catch(InterruptedIOException e){
			  if(e instanceof ConnectTimeoutException){
				  toastWrapper("连接超时");  
			  }else if(e instanceof InterruptedIOException){
				  toastWrapper("请求超时");  
			  }
          }finally{  
        	  httpClient.getConnectionManager().shutdown();  
          }  
		  return false;
    } 
	
	/**
	 * 用户上班打卡时上报位置Task
	 * 
	 */
	public class UserReprotLocationTask extends AsyncTask<Void, Void, Boolean> {
		private AMapLocation amapLocation;
		public UserReprotLocationTask(AMapLocation amapLocation){
			this.amapLocation = amapLocation;
		}
		@Override
		protected Boolean doInBackground(Void... params) {
			try{
			    return clientReportLocation(amapLocation);
			}catch(Exception e){
				e.printStackTrace();
			}
			return false;
		}

		@Override
		protected void onPostExecute(final Boolean success) {
			mUserReprotLocationTask = null;
		}

		@Override
		protected void onCancelled() {
			mUserReprotLocationTask = null;
		}
		
	}
	
	
    /**
   	 * Add for clock 
   	 * */
   	public boolean clientClock() throws ParseException, IOException, JSONException{
   		  HttpClient httpClient = new DefaultHttpClient();
   		  httpClient.getParams().setIntParameter(  
                     HttpConnectionParams.SO_TIMEOUT, 5000); // 请求超时设置,"0"代表永不超时  
   		  httpClient.getParams().setIntParameter(  
                     HttpConnectionParams.CONNECTION_TIMEOUT, 5000);// 连接超时设置 
   		  String strurl = "http://" + this.getString(R.string.ip) + "/itspark/collector/workAttendance/clock";
   		  HttpPost request = new HttpPost(strurl);
   		  request.addHeader("Accept","application/json");
		  //request.setHeader("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
		  request.setHeader("Content-Type", "application/json; charset=utf-8");
   		  JSONObject param = new JSONObject();
   		  ClockInfo info = new ClockInfo();
		  CommonRequestHeader header = new CommonRequestHeader();
		  header.addRequestHeader(CommonRequestHeader.REQUEST_COLLECTOR_WORK_CLOCK_CODE, readAccount(), readToken());
   		  info.setHeader(header);
   		  info.setParkNo(readCollector("parkNumber"));
   		  info.setClockType(mType);
   		  info.setLocationState(mLocationState);
   		  CharSequence currentTime = DateFormat.format("yyyy-MM-dd HH:mm:ss", System.currentTimeMillis());
   		  info.setClockTime(currentTime + "");
   		  StringEntity se = new StringEntity(JacksonJsonUtil.beanToJson(info), "UTF-8");
		  Log.e(LOG_TAG,"clientClock-> param is " + JacksonJsonUtil.beanToJson(info));
   		  request.setEntity(se);//发送数据
   		  try{
   			  HttpResponse httpResponse = httpClient.execute(request);//获得响应
   			  int code = httpResponse.getStatusLine().getStatusCode();
   			  if(code==HttpStatus.SC_OK){
   				  String strResult = EntityUtils.toString(httpResponse.getEntity());
   				  Log.e(LOG_TAG,"clientClock-> strResult is " + strResult);
   				  CommonResponse res = new CommonResponse(strResult);
   				  toastWrapper(res.getResMsg());
   				  String resCode = res.getResCode();
   				  if(resCode.equals("100")){
   					  return true;
   				  }else{
   					  return false;
   				  }
   			  }else{
   				  Log.e(LOG_TAG, "clientClock-> error code is " + Integer.toString(code));
   				  return false;
   			  }
   		  }catch(InterruptedIOException e){
   			  if(e instanceof ConnectTimeoutException){
   				  toastWrapper("连接超时");  
   			  }else if(e instanceof InterruptedIOException){
   				  toastWrapper("请求超时");  
   			  }
             }finally{  
           	  httpClient.getConnectionManager().shutdown();  
             }  
   		  return false;
       } 
	/**
	 * 用户上班打卡Task
	 * 
	 */
	public class UserClockTask extends AsyncTask<Void, Void, Boolean> {
		@Override
		protected Boolean doInBackground(Void... params) {
			try{
			    return clientClock();
			}catch(Exception e){
				e.printStackTrace();
			}
			return false;
		}

		@Override
		protected void onPostExecute(final Boolean success) {
			mUserClockTask = null;
			if(success){
				if(mType==ATTENDANCE_TYPE_START){
					mAttendanceBT.setEnabled(false);
	        		Message msg1 = new Message();
	                msg1.what = EVENT_ATTENDANCE_START_SUCCESS;
	                mHandler.sendMessage(msg1);
	        		Message msg2 = new Message();
	                msg2.what = EVENT_ENTER_MAIN;
	                mHandler.sendMessageDelayed(msg2, 1000);
				}else if(mType==ATTENDANCE_TYPE_END){
					mAttendanceBT.setEnabled(false);
	        		Message msg1 = new Message();
	                msg1.what = EVENT_ATTENDANCE_END_SUCCESS;
	                mHandler.sendMessage(msg1);
	        		Message msg2 = new Message();
	                msg2.what = EVENT_EXIT_LOGIN;
	                mHandler.sendMessageDelayed(msg2, 5000);
				}
			}
		}

		@Override
		protected void onCancelled() {
			mUserClockTask = null;
		}
		
	}
	/**
	 * 封装Toast
	 * */
	 private void toastWrapper(final String str) {
	      runOnUiThread(new Runnable() {
	          @Override
	           public void run() {
	               Toast.makeText(WorkAttendanceActivity.this, str, Toast.LENGTH_SHORT).show();
	           }
	      });
	 }
}
