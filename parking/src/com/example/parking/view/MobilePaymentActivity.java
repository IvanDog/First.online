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

import com.example.parking.R;
import com.example.parking.R.color;
import com.example.parking.R.id;
import com.example.parking.R.layout;
import com.example.parking.R.string;
import com.example.parking.common.JacksonJsonUtil;
import com.example.parking.info.CommonRequestHeader;
import com.example.parking.info.CommonResponse;
import com.example.parking.info.PaymentInfo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ParseException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

public class MobilePaymentActivity extends FragmentActivity {
    private final static int SCANNIN_GREQUEST_CODE = 101; 
	private static final int PAYMENT_TYPE_ALIPAY=202;
	private static final int PAYMENT_TYPE_WECHATPAY=203;
	public static final int EVENT_DISPLAY_QUERY_RESULT = 301;
	public static final int EVENT_DISPLAY_REQUEST_TIMEOUT = 302;
	public static final int EVENT_DISPLAY_CONNECT_TIMEOUT = 303;
	public static final int EVENT_SCAN_MODE = 304;
	    
    private static final String FILE_NAME_COLLECTOR = "save_pref_collector";
    private static final String FILE_NAME_TOKEN = "save_pref_token";
	public static String LOG_TAG = "MobilePaymentActivity";
	private Fragment mPaymentFragment;
	private TextView mScanTitleTV;
	private TextView mCardTitleTV;
	private int mCurrentId;
	private int mPayType;//支付宝/微信
	private int mPaymentPattern;//支付宝扫码/微信扫码/支付宝刷卡/微信刷卡
	private String mParkNumber;
	private String mCarType;
	private String mParkType;
	private String mStartTime;
	private String mLeaveTime;
	private String mPaidMoney;
	private String mFeeScale;
	private String mParkingRecordID;
	private String mTradeRecordID;
	private String mLicensePlateNumber;
	private String mCodeUrl;
	private String mAuthCode;
	private UserPayTask mPayTask = null;
	private OnClickListener mTabClickListener = new OnClickListener() {
        @Override  
        public void onClick(View v) {  
            if (v.getId() != mCurrentId) {//如果当前选中跟上次选中的一样,不需要处理  
                changeSelect(v.getId());//改变图标跟文字颜色的选中   
                changeFragment(v.getId());//fragment的切换  
                mCurrentId = v.getId();//设置选中id  
            }  
        }  
    };  
    
    
	@Override  
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);  
        Intent intent = getIntent();
        mPayType = intent.getExtras().getInt("paytype");
		mParkNumber =  intent.getExtras().getString("parkNumber");
		mLicensePlateNumber =  intent.getExtras().getString("licensePlateNumber");
		mParkingRecordID =  intent.getExtras().getString("parkingRecordID");
		mTradeRecordID =   intent.getExtras().getString("tradeRecordID");
		mCarType =   intent.getExtras().getString("carType");
		mParkType =   intent.getExtras().getString("parkType");
		mStartTime =   intent.getExtras().getString("startTime");
		mLeaveTime =   intent.getExtras().getString("leaveTime");
		mPaidMoney =   intent.getExtras().getString("paidMoney");
		mParkType =   intent.getExtras().getString("parkType");
        setContentView(R.layout.activity_mobile_payment);
        mScanTitleTV = (TextView) findViewById(R.id.tv_mobile_payment_scan);
        mCardTitleTV = (TextView) findViewById(R.id.tv_mobile_payment_card);
        mScanTitleTV.setOnClickListener(mTabClickListener);
        mCardTitleTV.setOnClickListener(mTabClickListener); 
        changeSelect(R.id.tv_mobile_payment_scan);
        changeFragment(R.id.tv_mobile_payment_scan);
		getActionBar().setDisplayHomeAsUpEnabled(true);
        IntentFilter filter = new IntentFilter();  
        filter.addAction("ExitApp");  
        filter.addAction("BackMain");  
        registerReceiver(mReceiver, filter); 
	}

	private void changeSelect(int resId) {  
		mScanTitleTV.setSelected(false);
		mScanTitleTV.setBackgroundResource(R.color.gray);
		mCardTitleTV.setSelected(false);  
		mCardTitleTV.setBackgroundResource(R.color.gray);
        switch (resId) {  
        case R.id.tv_mobile_payment_scan:  
        	mScanTitleTV.setSelected(true);  
        	mScanTitleTV.setBackgroundResource(R.color.orange);
            break;  
        case R.id.tv_mobile_payment_card:  
        	mCardTitleTV.setSelected(true);  
        	mCardTitleTV.setBackgroundResource(R.color.orange);
            break;
        }  
    }

	private void changeFragment(int resId) {  
        if(resId==R.id.tv_mobile_payment_scan){
        	if(mPayType==PAYMENT_TYPE_ALIPAY){
            	mPayTask = new UserPayTask("支付宝扫码付");
        	}else if(mPayType==PAYMENT_TYPE_WECHATPAY){
            	mPayTask = new UserPayTask("微信扫码支付");
        	}
        	mPayTask.execute((Void) null);
        }else if(resId==R.id.tv_mobile_payment_card){
            Intent intent = new Intent();  
            intent.setClass(MobilePaymentActivity.this, CaptureActivity.class);  
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);  
            startActivityForResult(intent, SCANNIN_GREQUEST_CODE); 
        }
    }

	private void hideFragments(FragmentTransaction transaction){  
        if (mPaymentFragment != null) {
        	transaction.hide(mPaymentFragment);
        }
    }
	
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
    
    @Override  
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {  
        super.onActivityResult(requestCode, resultCode, data);  
        switch (requestCode) {  
        case SCANNIN_GREQUEST_CODE:  
            if(resultCode == RESULT_OK){  
                Bundle bundle = data.getExtras();  
                mAuthCode = bundle.getString("result");
            	if(mPayType==PAYMENT_TYPE_ALIPAY){
                	mPayTask = new UserPayTask("支付宝条码付");
            	}else if(mPayType==PAYMENT_TYPE_WECHATPAY){
                	mPayTask = new UserPayTask("微信刷卡支付");
            	}
            	mPayTask.execute((Void) null);
            }  
            break;  
        }  
    }
    /**
	 * 发起支付
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
		  info.setParkingRecordID(convertString(mParkingRecordID));
		  info.setTradeRecordID(convertString(mTradeRecordID));
		  info.setPaymentPattern(convertPayPattToInteger(paymentPattern));
		  info.setPaidMoney(mPaidMoney.replace("元", ""));
		  if(mPaymentPattern==6 || mPaymentPattern ==7){//“微信刷卡支付”或“支付宝条码付”
			  info.setAuthCode(mAuthCode);
		  }
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
			          mPaymentPattern=convertPayPattToInteger(paymentPattern);
			          if(mPaymentPattern==4 || mPaymentPattern==5){//扫码支付
						  mCodeUrl=(String)res.getPropertyMap().get("code_url");
			          }
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
	 * 支付Task
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
			    msg.what=EVENT_SCAN_MODE;
			    mHandler.sendMessage(msg);
			}
			mPayTask = null;
		}

		@Override
		protected void onCancelled() {
			mPayTask = null;
		}
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
	            case EVENT_SCAN_MODE:
	        		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();//开启一个Fragment事务  
	                hideFragments(transaction);//隐藏所有fragment  
	            	mPaymentFragment = new MobilePaymentFragment(mPayType,mCodeUrl);
	               	transaction.replace(R.id.mobile_payment_container, mPaymentFragment);
	                transaction.commit();//一定要记得提交事务  
	            	break;
                default:
                    break;
            }
        }
    };  
    
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
			}else if("未付".equals(paymentPattern)){
				return 0;
			}else{
				return -1;
			}
		}
	
	public String convertString(String str){
		if("null".equals(str)){ 
			return "";
		}else{
			return str;
		}
	}
}
