package com.example.parking.view;


import java.io.IOException;
import java.io.InterruptedIOException;

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

import com.example.parking.R;
import com.example.parking.R.id;
import com.example.parking.R.layout;
import com.example.parking.R.string;
import com.example.parking.common.JacksonJsonUtil;
import com.example.parking.info.CommonRequestHeader;
import com.example.parking.info.CommonResponse;
import com.example.parking.info.PaymentInfo;
import com.example.parking.info.SettleAccountInfo;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class LeavingActivity extends Activity {
	private static final int PAYMENT_TYPE_POS=201;
	private static final int PAYMENT_TYPE_ALIPAY=202;
	private static final int PAYMENT_TYPE_WECHATPAY=203;
	private static final int PAYMENT_TYPE_UNFINISHED=204;
	public static final int EVENT_DISPLAY_QUERY_RESULT = 301;
	public static final int EVENT_DISPLAY_REQUEST_TIMEOUT = 302;
	public static final int EVENT_DISPLAY_CONNECT_TIMEOUT = 303;
	public static final int EVENT_DISPLAY_INFORMATION = 401;
	public static final int EVENT_ORDER_SUCCESS = 501;
	public static String LOG_TAG = "LeavingActivity";
    private static final String FILE_NAME_COLLECTOR = "save_pref_collector";
    private static final String FILE_NAME_TOKEN = "save_pref_token";
	private TextView mUserNumberTV;;
	private TextView mLicensePlateNumberTV;
	private TextView mStartTimeTV;
	private TextView mLeaveTimeTV;
	private TextView mFeeScaleTV;
	private TextView mExpenseTV;
	private String mParkingEnterID;
	private String mParkingRecordID;
	private String mTradeRecordID;
	private String mParkNumber;
	private String mLicensePlateNumber;
	private Button mConfirmPaymentBT;
	private RadioGroup mPaymentTypeRG;
	private RadioButton  mPosPaymentTypeRB;
	private RadioButton mAlipayPaymentTypeRB;
	private RadioButton mWechatpayPaymentRB;
	private RadioButton mUnfinishedPaymentRB;
	private int mPaymentType;
	private String mCarType;
	private String mParkType;
	private String mStartTime;
	private String mLeaveTime;
	private String mExpense;
	private String mFeeScale;
	private String mPaymentPattern;
	private Context mContext;
	private UserQueryTask mQueryTask = null;
	private UserPayTask mPayTask = null;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_leaving);
		mContext=this;
		mUserNumberTV=(TextView)findViewById(R.id.tv_user_number_leaving);
		//mUserNumberTV.setText("工号:" + readCollector("parkNumber"));
		mLicensePlateNumberTV=(TextView)findViewById(R.id.tv_license_number_leaving);
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		mParkingEnterID = bundle.getString("parkingEnterID");
		mParkNumber=bundle.getString("parkNumber");
		mLicensePlateNumber = bundle.getString("licensePlateNumber");
		mCarType = bundle.getString("carType");
        mParkingRecordID = bundle.getString("parkingRecordID");
        mLeaveTime = bundle.getString("leaveTime");
		//mLicensePlateNumberTV.setText("牌照:" + mLicensePlateNumber);
		mStartTimeTV=(TextView)findViewById(R.id.tv_start_time_leaving);
		mLeaveTimeTV=(TextView)findViewById(R.id.tv_leave_time_leaving);
		//mLeaveTimeTV.setText("离场：" + mLeaveTime);
		mFeeScaleTV=(TextView)findViewById(R.id.tv_fee_Scale_leaving);
		mExpenseTV=(TextView)findViewById(R.id.tv_expense_leaving);
		mPaymentTypeRG=(RadioGroup)findViewById(R.id.rg_payment_type_leaving);
		mPosPaymentTypeRB=(RadioButton)findViewById(R.id.rb_pos_payment_leaving);
		mPosPaymentTypeRB.setEnabled(false);
		mAlipayPaymentTypeRB=(RadioButton)findViewById(R.id.rb_alipay_payment_leaving);
		//mAlipayPaymentTypeRB.setEnabled(false);
		mWechatpayPaymentRB=(RadioButton)findViewById(R.id.rb_wechatpay_payment_leaving);
		mWechatpayPaymentRB.setEnabled(false);
		mUnfinishedPaymentRB=(RadioButton)findViewById(id.rb_unfinished_payment_leaving);
		//mUnfinishedPaymentRB.setEnabled(false);
		mPaymentTypeRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() { 
			@Override 
			public void onCheckedChanged(RadioGroup group, int checkedId){
			    if (mPosPaymentTypeRB.getId() == checkedId) {
			    	mPaymentType = PAYMENT_TYPE_POS; 
		        }else if (mAlipayPaymentTypeRB.getId() == checkedId){
		        	mPaymentType = PAYMENT_TYPE_ALIPAY; 
			    }else if (mWechatpayPaymentRB.getId() == checkedId){
		        	mPaymentType = PAYMENT_TYPE_WECHATPAY; 
			    }else if (mUnfinishedPaymentRB.getId() == checkedId){
					mPaymentType = PAYMENT_TYPE_UNFINISHED;
				}
			    mConfirmPaymentBT.setEnabled(true);
			  } 
			});
		mConfirmPaymentBT=(Button)findViewById(R.id.bt_confirm_payment_leaving);
		mConfirmPaymentBT.setEnabled(false);
		mConfirmPaymentBT.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				mPayTask = new UserPayTask("未付");
				mPayTask.execute((Void) null);
			}
		});
		mQueryTask = new UserQueryTask();
		mQueryTask.execute((Void) null);
        getActionBar().setDisplayHomeAsUpEnabled(true);
		//new SQLThread().start();
        IntentFilter filter = new IntentFilter();  
        filter.addAction("ExitApp");  
        filter.addAction("BackMain");  
        registerReceiver(mReceiver, filter); 
	}

	private Handler mHandler = new Handler() {
        @Override
        public void handleMessage (Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case EVENT_DISPLAY_QUERY_RESULT:
                	Toast.makeText(getApplicationContext(), (String)msg.obj, Toast.LENGTH_SHORT).show();
                	break;
	            case EVENT_DISPLAY_REQUEST_TIMEOUT:
	            	Toast.makeText(getApplicationContext(), (String)msg.obj, Toast.LENGTH_SHORT).show();
	            	break;
	            case EVENT_DISPLAY_CONNECT_TIMEOUT:
	            	Toast.makeText(getApplicationContext(), (String)msg.obj, Toast.LENGTH_SHORT).show();
	            	break;
                case EVENT_DISPLAY_INFORMATION:
                	mUserNumberTV.setText("工号:" + readCollector("collectorNumber"));
                	mLicensePlateNumberTV.setText("牌照:" + mLicensePlateNumber);
                	mStartTimeTV.setText("入场：" + mStartTime);
                	mLeaveTimeTV.setText("离场：" + mLeaveTime);
            		mFeeScaleTV.setText("收费标准:" + mFeeScale);
                	mExpenseTV.setText("费用总计:" + mExpense);
                	break;
				case EVENT_ORDER_SUCCESS:
					if(mPaymentType==PAYMENT_TYPE_POS){
						//TODO
					}else if(mPaymentType==PAYMENT_TYPE_ALIPAY){
						Intent intent = new Intent(LeavingActivity.this,MobilePaymentActivity.class);
						Bundle bundle = new Bundle();
						bundle.putInt("paytype", PAYMENT_TYPE_ALIPAY);
						bundle.putString("parkNumber", mParkNumber);
						bundle.putString("licensePlateNumber", mLicensePlateNumber);
						bundle.putString("parkingRecordID", mParkingRecordID);
						bundle.putString("tradeRecordID", mTradeRecordID);
						bundle.putString("carType", mCarType);
						bundle.putString("parkType", mParkType);
						bundle.putString("startTime", mStartTime);
						bundle.putString("leaveTime", mLeaveTime);
						bundle.putString("paidMoney", mExpense);
						bundle.putString("feeScale",mFeeScale);
						intent.putExtras(bundle);
						startActivity(intent);
					}else if(mPaymentType==PAYMENT_TYPE_WECHATPAY){
						Intent intent = new Intent(LeavingActivity.this,MobilePaymentActivity.class);
						Bundle bundle = new Bundle();
						bundle.putInt("paytype", PAYMENT_TYPE_WECHATPAY);
						bundle.putString("parkNumber", mParkNumber);
						bundle.putString("licensePlateNumber", mLicensePlateNumber);
						bundle.putString("parkingRecordID", mParkingRecordID);
						bundle.putString("tradeRecordID", mTradeRecordID);
						bundle.putString("carType", mCarType);
						bundle.putString("parkType", mParkType);
						bundle.putString("startTime", mStartTime);
						bundle.putString("leaveTime", mLeaveTime);
						bundle.putString("paidMoney", mExpense);
						bundle.putString("feeScale",mFeeScale);
						intent.putExtras(bundle);
						startActivity(intent);
					}else if(mPaymentType==PAYMENT_TYPE_UNFINISHED){
						Intent intentBack = new Intent();
						intentBack.setAction("BackMain");
						sendBroadcast(intentBack);
						Intent intent = new Intent(LeavingActivity.this,MainActivity.class);
						startActivity(intent);
						finish();
						break;
					}
                default:
                    break;
            }
        }
    };  
    
	public boolean onOptionsItemSelected(MenuItem item) {  
	    switch (item.getItemId()) {  
	         case android.R.id.home:  
	        	 finish();
	             break;    
	        default:  
	             break;  
	    }  
	    return super.onOptionsItemSelected(item);  
	  }  
	
    private BroadcastReceiver mReceiver = new BroadcastReceiver(){  
		@Override
		public void onReceive(Context context, Intent intent) {
			if(intent.getAction()!=null && intent.getAction().equals("ExitApp")){
				finish();
			}else if(intent.getAction()!=null && intent.getAction().equals("BackMain")){
				finish();
			}
		}            
    }; 
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }

    private String readToken() {
        SharedPreferences pref = getSharedPreferences(FILE_NAME_TOKEN, MODE_MULTI_PROCESS);
        String str = pref.getString("token", "");
        return str;
    }
    
    private String readCollector(String data) {
        SharedPreferences pref = getSharedPreferences(FILE_NAME_COLLECTOR, MODE_MULTI_PROCESS);
        String str = pref.getString(data, "");
        return str;
    }
    
    private String readAccount() {
        SharedPreferences pref = getSharedPreferences(FILE_NAME_COLLECTOR, MODE_MULTI_PROCESS);
        String str = pref.getString("collectorNumber", "");
        return str;
    }
    /**
	 * 查询应付金额
	 * */
	public boolean clientQueryExpense() throws ParseException, IOException, JSONException{
		Log.e("clientQueryExpense","enter clientQueryExpense");  
		HttpClient httpClient = new DefaultHttpClient();
		  httpClient.getParams().setIntParameter(  
                  HttpConnectionParams.SO_TIMEOUT, 5000); // 请求超时设置,"0"代表永不超时  
		  httpClient.getParams().setIntParameter(  
                  HttpConnectionParams.CONNECTION_TIMEOUT, 5000);// 连接超时设置 
		  String strurl = "http://" + this.getString(R.string.ip) + "/itspark/collector/leavingInformation/queryExpense";
		  HttpPost request = new HttpPost(strurl);
		  request.addHeader("Accept","application/json");
		  request.setHeader("Content-Type", "application/json; charset=utf-8");
		  SettleAccountInfo info = new SettleAccountInfo();
		  CommonRequestHeader header = new CommonRequestHeader();
		  header.addRequestHeader(CommonRequestHeader.REQUEST_COLLECTOR_QUERY_EXPENSE_CODE, readAccount(), readToken());
		  info.setHeader(header);
		  info.setParkNumber(mParkNumber);
		  info.setLicensePlateNumber(mLicensePlateNumber);
		  info.setCarType(mCarType);
		  if(mParkingEnterID!=null && !"".equals(mParkingEnterID)){
			  info.setParkingEnterID(String.valueOf(mParkingEnterID));
		  }else if(mParkingRecordID!=null && !"".equals(mParkingRecordID)){
			  info.setParkingRecordID(mParkingRecordID);
		  }
		  if(mLeaveTime==null){
	          CharSequence sysTimeStr = DateFormat.format("yyyy-MM-dd HH:mm:ss", System.currentTimeMillis());
	          mLeaveTime = sysTimeStr + "";
		  }
          info.setLeaveTime(mLeaveTime);
		  StringEntity se = new StringEntity( JacksonJsonUtil.beanToJson(info), "UTF-8");
		  Log.e(LOG_TAG,"clientQueryExpense-> param is " + JacksonJsonUtil.beanToJson(info));
		  request.setEntity(se);//发送数据
		  try{
			  HttpResponse httpResponse = httpClient.execute(request);//获得响应
			  int code = httpResponse.getStatusLine().getStatusCode();
			  if(code==HttpStatus.SC_OK){
				  String strResult = EntityUtils.toString(httpResponse.getEntity());
				  Log.e(LOG_TAG,"clientQueryExpense->strResult is " + strResult);
				  CommonResponse res = new CommonResponse(strResult);
				  Message msg = new Message();
		          msg.what=EVENT_DISPLAY_QUERY_RESULT;
		          msg.obj= res.getResMsg();
		          mHandler.sendMessage(msg);
				  if(res.getResCode().equals("100")){
					  //mCarType = (String)res.getPropertyMap().get("carType");
					  mParkType = (String)res.getPropertyMap().get("parkType");
					  mStartTime = (String)res.getPropertyMap().get("startTime");
					  //mLeaveTime = (String)res.getPropertyMap().get("leaveTime");
					  mExpense = (String)res.getPropertyMap().get("expensePrimary");
					  mParkingRecordID = (String)res.getPropertyMap().get("parkingRecordID");
					  mTradeRecordID = (String)res.getPropertyMap().get("tradeRecordID");
					  mFeeScale = (String)res.getPropertyMap().get("feeScale");
					  return true;
				  }else{
			          return false;
				  } 
			}else{
					  Log.e(LOG_TAG, "clientQueryExpense->error code is " + Integer.toString(code));
					  return false;
		    }
		  }catch(InterruptedIOException e){
			  if(e instanceof ConnectTimeoutException){
				  Message msg = new Message();
		          msg.what=EVENT_DISPLAY_CONNECT_TIMEOUT;
		          msg.obj= "连接超时";
		          mHandler.sendMessage(msg);
			  }else if(e instanceof InterruptedIOException){
				  Message msg = new Message();
		          msg.what=EVENT_DISPLAY_REQUEST_TIMEOUT;
		          msg.obj="请求超时";
		          mHandler.sendMessage(msg);
			  }
          }finally{  
        	  httpClient.getConnectionManager().shutdown();  
          }  
		  return false;
    }
	
    /**
	 * 查询结算信息Task
	 * 
	 */
	public class UserQueryTask extends AsyncTask<Void, Void, Boolean> {
		@Override
		protected Boolean doInBackground(Void... params) {
			try{
				Log.e(LOG_TAG,"UserQueryTask-> doInBackground");  
				return clientQueryExpense();
			}catch(Exception e){
				Log.e(LOG_TAG,"clientQueryExpense->Query exists exception ");  
				e.printStackTrace();
			}
			return false;
		}

		@Override
		protected void onPostExecute(final Boolean success) {
			mQueryTask = null;
		    if(success){
			    Message msg = new Message();
			    msg.what=EVENT_DISPLAY_INFORMATION;
			    mHandler.sendMessage(msg);
		    }
		}

		@Override
		protected void onCancelled() {
			mQueryTask = null;
		}
		
	}
	
	 /**
		 * Add for finish payment
		 * */
		public boolean clientPay(String paymentPattern) throws ParseException, IOException, JSONException{
			Log.e("clientUpdatePaymentPattern","enter clientUpdatePaymentPattern");  
			HttpClient httpClient = new DefaultHttpClient();
			  httpClient.getParams().setIntParameter(  
	                  HttpConnectionParams.SO_TIMEOUT, 5000); // 请求超时设置,"0"代表永不超时  
			  httpClient.getParams().setIntParameter(  
	                  HttpConnectionParams.CONNECTION_TIMEOUT, 5000);// 连接超时设置 
			  String strurl = "http://" + this.getString(R.string.ip) + "/itspark/collector/leavingInformation/pay";
			  HttpPost request = new HttpPost(strurl);
			  request.addHeader("Accept","application/json");
			  request.setHeader("Content-Type", "application/json; charset=utf-8");
			  PaymentInfo info = new PaymentInfo();
			  CommonRequestHeader header = new CommonRequestHeader();
			  header.addRequestHeader(CommonRequestHeader.REQUEST_COLLECTOR_PAY_CODE, readAccount(), readToken());
			  info.setHeader(header);
			  info.setParkNumber(mParkNumber);
			  info.setLicensePlateNumber(mLicensePlateNumber);
			  info.setParkingRecordID(String.valueOf(mParkingRecordID));
			  info.setTradeRecordID(mTradeRecordID);
			  info.setPaymentPattern(convertPayPattToInteger(paymentPattern));
			  info.setPaidMoney(mExpense.replace("元", ""));
			  StringEntity se = new StringEntity(JacksonJsonUtil.beanToJson(info), "UTF-8");
			  Log.e(LOG_TAG,"clientPay-> param is " + JacksonJsonUtil.beanToJson(info));
			  request.setEntity(se);
			  try{
				  HttpResponse httpResponse = httpClient.execute(request);//获得响应
				  int code = httpResponse.getStatusLine().getStatusCode();
				  if(code==HttpStatus.SC_OK){
					  String strResult = EntityUtils.toString(httpResponse.getEntity());
					  Log.e(LOG_TAG,"clientPay->strResult is " + strResult);
					  CommonResponse res = new CommonResponse(strResult);
					  Message msg = new Message();
			          msg.what=EVENT_DISPLAY_QUERY_RESULT;
			          msg.obj= res.getResMsg();
			          mHandler.sendMessage(msg);
					  if(res.getResCode().equals("100")){
						  mPaymentPattern = paymentPattern;
						  mTradeRecordID = (String)res.getPropertyMap().get("tradeRecordID");
						  return true;
					  }else{
				          return false;
					  } 
				}else{
						  Log.e(LOG_TAG, "clientPay->error code is " + Integer.toString(code));
						  return false;
			    }
			  }catch(InterruptedIOException e){
				  if(e instanceof ConnectTimeoutException){
					  Message msg = new Message();
			          msg.what=EVENT_DISPLAY_CONNECT_TIMEOUT;
			          msg.obj= "连接超时";
			          mHandler.sendMessage(msg);
				  }else if(e instanceof InterruptedIOException){
					  Message msg = new Message();
			          msg.what=EVENT_DISPLAY_REQUEST_TIMEOUT;
			          msg.obj="请求超时";
			          mHandler.sendMessage(msg);
				  }
	          }finally{  
	        	  httpClient.getConnectionManager().shutdown();  
	          }  
			  return false;
	    }
		
	    /**
		 * 查询j结算信息Task
		 * 
		 */
		public class UserPayTask extends AsyncTask<Void, Void, Boolean> {
			private String paymentPattern = null;
			public UserPayTask(String paymentPattern){
				this.paymentPattern = paymentPattern;
			}
			@Override
			protected Boolean doInBackground(Void... params) {
				try{
					Log.e(LOG_TAG,"UserPayTask->doInBackground");  
					return clientPay(paymentPattern);
				}catch(Exception e){
					Log.e(LOG_TAG,"UserPayTask->exists exception ");  
					e.printStackTrace();
				}
				return false;
			}

			@Override
			protected void onPostExecute(final Boolean success) {
				if(success){
					Message msg = new Message();
					msg.what=EVENT_ORDER_SUCCESS;
					mHandler.sendMessage(msg);
				}
				mPayTask = null;
			}

			@Override
			protected void onCancelled() {
				mPayTask = null;
			}
			
		}
		
		public Integer convertPayPattToInteger(String paymentPattern){
           if("pos机支付".equals(paymentPattern)){
				return 1;
			}else if("微信支付".equals(paymentPattern)){
				return 2;
			}else if("支付宝支付".equals(paymentPattern)){
				return 3;
			}else if("微信扫码支付".equals(paymentPattern)){
				return 4;
			}else if("支付宝扫码付".equals(paymentPattern)){
				return 5;
			}else if("微信刷卡支付".equals(paymentPattern)){
				return 6;
			}else if("支付宝条码付".equals(paymentPattern)){
				return 7;
			}else if("余额支付".equals(paymentPattern)){
				return 8;
			}else if("逃费".equals(paymentPattern)){
				return 9;
			}else{
				return 0;
			}
		}

}
