package com.example.parking.view;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

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
import com.example.parking.info.LicenseInfo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.ParseException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class InputLicenseActivity extends FragmentActivity {
	private static final int LICENSE_PLATE_NUMBER_SIZE=7;
	private static final int LICENSE_ARRIVING_TYPE=101;
	private static final int LICENSE_LEAVING_TYPE=102;
	private static final int EVENT_INVALID_LICENSE_PLATE=201;
	private static final int EVENT_NOTIFY_CHOOSE_CAR_TYPE =  202;
	private static final int EVENT_SCAN_STATE_NOTIFY=301;
	public static final int EVENT_DISPLAY_QUERY_RESULT = 401;
	public static final int EVENT_DISPLAY_REQUEST_TIMEOUT = 402;
	public static final int EVENT_DISPLAY_CONNECT_TIMEOUT = 403;
	public static final int EVENT_ENTER_ARRIVING = 501;
	public static final int EVENT_ENTER_LEAVING = 502;
	public static final int EVENT_HANDLE_ORDER = 503;
    private static final String FILE_NAME_TOKEN = "save_pref_token";
    private static final String FILE_NAME_COLLECTOR = "save_pref_collector";
	public static String LOG_TAG = "InputLicenseActivity";
	private Fragment mNumberFragment;
	private Fragment mLetterFragment;
	private Fragment mLocationFragment;
	private Fragment mCarTypeFragment;
	private EditText mLicensePlateET;
	private TextView mNumberTV;
	private TextView mLetterTV;
	private TextView mLocationTV;
	private TextView mCarTypeTV;
	private Button mScanBT;
	private Button mNextBT;
	private int mCurrentId;
	private int mType;
	private String mCarType = null;
	private String mParkingEnterID;
	private ArrayList<HashMap<String,Object>> mUnFinishedRecordList;
	private int mUnfinishedFlag = 0;
    private LicenseTask mLicenseTask = null;
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
		Bundle bundle = intent.getExtras();
		if(bundle.getInt("type")==LICENSE_ARRIVING_TYPE){
			mType=LICENSE_ARRIVING_TYPE;
		}else if(bundle.getInt("type")==LICENSE_LEAVING_TYPE){
			mType=LICENSE_LEAVING_TYPE;
		}
        setContentView(R.layout.activity_input_license);
        mLicensePlateET = (EditText) findViewById(R.id.et_license_plate);
        mLetterTV = (TextView) findViewById(R.id.tv_letter);
        mNumberTV = (TextView) findViewById(R.id.tv_number);
        mLocationTV = (TextView) findViewById(R.id.tv_location);
        mCarTypeTV= (TextView) findViewById(R.id.tv_car_type_input);
    	mScanBT = (Button) findViewById(R.id.bt_scan);
    	mNextBT = (Button) findViewById(R.id.bt_next);
        mLetterTV.setOnClickListener(mTabClickListener); 
    	mNumberTV.setOnClickListener(mTabClickListener);
    	mLocationTV.setOnClickListener(mTabClickListener);
    	mCarTypeTV.setOnClickListener(mTabClickListener);
    	changeSelect(R.id.tv_location);
    	changeFragment(R.id.tv_location);
    	mLicensePlateET.setOnTouchListener(new OnTouchListener() {	 
    	      @Override
    	      public boolean onTouch(View v, MotionEvent event) {
    	        // et.getCompoundDrawables()得到一个长度为4的数组，分别表示左右上下四张图片
    	        Drawable drawable = mLicensePlateET.getCompoundDrawables()[2];
    	        //如果右边没有图片，不再处理
    	        if (drawable == null){
    	            return false;
    	        }
    	        //如果不是按下事件，不再处理
    	        if (event.getAction() != MotionEvent.ACTION_UP){
    	            return false;
    	        }
    	        if (event.getX() > mLicensePlateET.getWidth()
                   -mLicensePlateET.getPaddingRight()
    	           - drawable.getIntrinsicWidth()){
    	        	mLicensePlateET.setText("");
    	        }
    	         return false;
    	      }
    	    });
    	mScanBT.setOnClickListener(new Button.OnClickListener(){
			public void onClick(View v){
				Message msg = new Message();
				msg.what= EVENT_SCAN_STATE_NOTIFY;
				mHandler.sendMessage(msg);
			}
		});
    	mLicensePlateET.setInputType(InputType.TYPE_NULL);
    	mLicensePlateET.addTextChangedListener(new TextWatcher(){
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            	if(mLicensePlateET.getText()!=null && mLicensePlateET.getText().length()>LICENSE_PLATE_NUMBER_SIZE){
            		Message msg = new Message();
            		msg.what=EVENT_INVALID_LICENSE_PLATE;
            		mHandler.sendMessage(msg);
            	}
            }
            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }
    	});
    	mNextBT.setOnClickListener(new Button.OnClickListener(){
			public void onClick(View v){
				if(mLicensePlateET.getText().length() !=LICENSE_PLATE_NUMBER_SIZE){
            		Message msg = new Message();
            		msg.what=EVENT_INVALID_LICENSE_PLATE;
            		mHandler.sendMessage(msg);
            		return;
				}
				if(mCarType == null || "".equals(mCarType)){
            		Message msg = new Message();
            		msg.what=EVENT_NOTIFY_CHOOSE_CAR_TYPE;
            		mHandler.sendMessage(msg);
            		return;
				}
				mLicenseTask = new LicenseTask();
				mLicenseTask.execute((Void) null);
				//new SQLThread().start();
			}
		});
    	getActionBar().setDisplayHomeAsUpEnabled(true); 
        IntentFilter filter = new IntentFilter();  
        filter.addAction("ExitApp");  
        filter.addAction("BackMain");  
        registerReceiver(mReceiver, filter); 
	}

	private void changeFragment(int resId) {  
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();//开启一个Fragment事务  
        hideFragments(transaction);//隐藏所有fragment  
        if(resId==R.id.tv_letter){
            if(mLetterFragment==null){//如果为空先添加进来.不为空直接显示  
            	mLetterFragment = new LetterFragment();  
                transaction.add(R.id.main_container,mLetterFragment);  
            }else {  
                transaction.show(mLetterFragment);  
            }
        }else if(resId==R.id.tv_number){
            if(mNumberFragment==null){//如果为空先添加进来.不为空直接显示  
            	mNumberFragment = new NumberFragment();  
                transaction.add(R.id.main_container,mNumberFragment);  
            }else {  
                transaction.show(mNumberFragment);  
            }
        }else if(resId==R.id.tv_location){
            if(mLocationFragment==null){//如果为空先添加进来.不为空直接显示  
            	mLocationFragment = new LocationFragment();  
                transaction.add(R.id.main_container,mLocationFragment);  
            }else {  
                transaction.show(mLocationFragment);  
            }
        }
        else if(resId==R.id.tv_car_type_input){
            if(mCarTypeFragment==null){//如果为空先添加进来.不为空直接显示  
            	mCarTypeFragment = new CarTypeFragment();  
                transaction.add(R.id.main_container,mCarTypeFragment);  
            }else {  
                transaction.show(mCarTypeFragment);  
            }
        }
        transaction.commit();//提交事务  
    }

	private void hideFragments(FragmentTransaction transaction){  
        if (mLetterFragment != null)  
            transaction.hide(mLetterFragment);
        if (mNumberFragment != null)
            transaction.hide(mNumberFragment);
        if (mLocationFragment != null)
            transaction.hide(mLocationFragment);
        if(mCarTypeFragment != null)
        	transaction.hide(mCarTypeFragment);
    }

	private void changeSelect(int resId) {  
		mLetterTV.setSelected(false);
		mLetterTV.setBackgroundResource(R.color.gray);
		mNumberTV.setSelected(false);
		mNumberTV.setBackgroundResource(R.color.gray);
		mLocationTV.setSelected(false);
		mLocationTV.setBackgroundResource(R.color.gray);
		mCarTypeTV.setSelected(false);
		mCarTypeTV.setBackgroundResource(R.color.gray);
        switch (resId) {  
        case R.id.tv_letter:  
        	mLetterTV.setSelected(true);  
        	mLetterTV.setBackgroundResource(R.color.orange);
            break;  
        case R.id.tv_number:  
        	mNumberTV.setSelected(true);  
        	mNumberTV.setBackgroundResource(R.color.orange);
            break;
        case R.id.tv_location:  
        	mLocationTV.setSelected(true);  
        	mLocationTV.setBackgroundResource(R.color.orange);
            break;
        case R.id.tv_car_type_input:  
        	mCarTypeTV.setSelected(true);  
        	mCarTypeTV.setBackgroundResource(R.color.orange);
            break;  
        }  
    }

	private Handler mHandler = new Handler() {
        @Override
        public void handleMessage (Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case EVENT_INVALID_LICENSE_PLATE:
                    Toast.makeText(getApplicationContext(), "请输入正确牌照", Toast.LENGTH_SHORT).show();
        	        break;
                case EVENT_NOTIFY_CHOOSE_CAR_TYPE:
                    Toast.makeText(getApplicationContext(), "请选择车辆类型", Toast.LENGTH_SHORT).show();
        	        break;
                case EVENT_DISPLAY_QUERY_RESULT:
                	Toast.makeText(getApplicationContext(), (String)msg.obj, Toast.LENGTH_SHORT).show();
                	break;
	            case EVENT_DISPLAY_REQUEST_TIMEOUT:
	            	Toast.makeText(getApplicationContext(), (String)msg.obj, Toast.LENGTH_SHORT).show();
	            	break;
	            case EVENT_DISPLAY_CONNECT_TIMEOUT:
	            	Toast.makeText(getApplicationContext(), (String)msg.obj, Toast.LENGTH_SHORT).show();
	            	break;
	            case EVENT_ENTER_ARRIVING:
					Intent arrivingIntent = new Intent(InputLicenseActivity.this,ParkingInformationActivity.class);
					Bundle arrivingBundle = new Bundle();
					arrivingBundle.putString("licensePlateNumber",mLicensePlateET.getText().toString());
					arrivingBundle.putString("carType", mCarType);
					arrivingIntent.putExtras(arrivingBundle);
					startActivity(arrivingIntent);
	            	break;
	            case EVENT_ENTER_LEAVING:
					Intent leavingIntent = new Intent(InputLicenseActivity.this,LeavingActivity.class);
					Bundle leavingBundle = new Bundle();
					leavingBundle.putString("parkNumber",readCollector("parkNumber"));
					leavingBundle.putString("parkingEnterID",mParkingEnterID);
					leavingBundle.putString("licensePlateNumber",mLicensePlateET.getText().toString());
					leavingBundle.putString("carType", mCarType);
					leavingIntent.putExtras(leavingBundle);
					startActivity(leavingIntent);
	            	break;
	            case EVENT_HANDLE_ORDER:
					Intent handleintent = new Intent(InputLicenseActivity.this,UnfinishedParkingRecordActivity.class);
					Bundle handleBundle = new Bundle();
					handleBundle.putSerializable("list",(Serializable)mUnFinishedRecordList);  
					handleintent.putExtras(handleBundle); 
					startActivity(handleintent);
	            	break;
                case EVENT_SCAN_STATE_NOTIFY:
                    Toast.makeText(getApplicationContext(), "扫码功能开发中", Toast.LENGTH_SHORT).show();
            	    break;
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
    
    /**
	 * Add for send license and receive result  
	 * */
	public boolean clientSendLicense()throws ParseException, IOException, JSONException{
		  HttpClient httpClient = new DefaultHttpClient();
		  httpClient.getParams().setIntParameter(  
                  HttpConnectionParams.SO_TIMEOUT,5000); // 请求超时设置,"0"代表永不超时  
		  httpClient.getParams().setIntParameter(  
                  HttpConnectionParams.CONNECTION_TIMEOUT, 5000);// 连接超时设置,"0"代表永不超时
		  String strurl = "http://" + this.getString(R.string.ip) + "/itspark/collector/license/analysis";
		  HttpPost request = new HttpPost(strurl);
		  request.addHeader("Accept","application/json");
		  request.setHeader("Content-Type", "application/json; charset=utf-8");
		  LicenseInfo info = new LicenseInfo();
		  CommonRequestHeader header = new CommonRequestHeader();
		  header.addRequestHeader(CommonRequestHeader.REQUEST_COLLECTOR_GET_LOCATION_STATE_CODE, readAccount(), readToken());
		  info.setHeader(header);
		  info.setCarType(mCarType);
		  info.setType(mType);
		  info.setLicensePlateNumber(mLicensePlateET.getText().toString());
		  info.setParkNumber(readCollector("parkNumber"));
		  StringEntity se = new StringEntity(JacksonJsonUtil.beanToJson(info), "UTF-8");
		  Log.e(LOG_TAG,"clientSendLicense-> param is " + JacksonJsonUtil.beanToJson(info));
		  request.setEntity(se);//发送数据
		  try{
			  HttpResponse httpResponse = httpClient.execute(request);//获得响应
			  int code = httpResponse.getStatusLine().getStatusCode();
			  if(code==HttpStatus.SC_OK){
				  String strResult = EntityUtils.toString(httpResponse.getEntity());
   				  Log.e(LOG_TAG,"clientSendLicense->strResult is " + strResult);
				  CommonResponse res = new CommonResponse(strResult);
				  String resCode = res.getResCode();
				  Message msg = new Message();
		          msg.what=EVENT_DISPLAY_QUERY_RESULT;
		          msg.obj= res.getResMsg();
		          mHandler.sendMessage(msg);
				  if(resCode.equals("100")){
					  if(res.getPropertyMap().get("parkingEnterID")!=null){
						  mParkingEnterID = (String)res.getPropertyMap().get("parkingEnterID");
					  }
                      mUnfinishedFlag = 0;
					  return true;
				  }else if(resCode.equals("204")){
					  mUnFinishedRecordList = res.getDataList();
					  mUnfinishedFlag = 1;
					  return true;
				  }
				  else{
					  return false;
				  }
			  }else{
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
	 * 牌照Task
	 * 
	 */
	public class LicenseTask extends AsyncTask<Void, Void, Boolean> {
		@Override
		protected Boolean doInBackground(Void... params) {
			try{
				Log.e(LOG_TAG,"LicenseTask->doInBackground");  
				return clientSendLicense();
			}catch(Exception e){
				e.printStackTrace();
			}
			return false;
		}

		@Override
		protected void onPostExecute(final Boolean success) {
			 mLicenseTask = null;
			 Log.e(LOG_TAG,"LicenseTask->onPostExecute " + success.toString());  
			if(success){
				if(mType == LICENSE_ARRIVING_TYPE){
					if(mUnfinishedFlag==1){
						  Message msg = new Message();
				          msg.what=EVENT_HANDLE_ORDER;
				          mHandler.sendMessage(msg);
					}else{
						  Message msg = new Message();
				          msg.what=EVENT_ENTER_ARRIVING;
				          mHandler.sendMessage(msg);
					}
				}else if(mType == LICENSE_LEAVING_TYPE){
					  Message msg = new Message();
			          msg.what=EVENT_ENTER_LEAVING;
			          mHandler.sendMessage(msg);
				}
			}
		}

		@Override
		protected void onCancelled() {
			mLicenseTask = null;
		}
		
	}
	
    public String readToken() {
        SharedPreferences pref = getSharedPreferences(FILE_NAME_TOKEN, MODE_MULTI_PROCESS);
        String str = pref.getString("token", "");
        return str;
    }
    
    private String readAccount() {
        SharedPreferences pref = getSharedPreferences(FILE_NAME_COLLECTOR, MODE_MULTI_PROCESS);
        String str = pref.getString("collectorNumber", "");
        return str;
    }
    
    private String readCollector(String data) {
        SharedPreferences pref = getSharedPreferences(FILE_NAME_COLLECTOR, MODE_MULTI_PROCESS);
        String str = pref.getString(data, "");
        return str;
    }
    
    public void setCarType(String type){
    	mCarType = type;
    }
}
