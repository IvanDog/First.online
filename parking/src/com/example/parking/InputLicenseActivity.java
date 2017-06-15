package com.example.parking;

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

import com.example.parking.ParkingInformationFragment.UserQueryTask;
import com.example.parking.R.color;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.ParseException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings.Secure;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.format.DateFormat;
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
	private static final int ARRIVING_TYPE=101;
	private static final int LEAVING_TYPE=102;
	private static final int EVENT_UNFINISHED_LICENSE_PLATE=201;
	private static final int EVENT_ESCAPE_LICENSE_PLATE=202;
	private static final int EVENT_ENTER_PARK_INFORMATION=203;
	private static final int EVENT_NOT_EXISTS_LICENSE_PLATE=204;
	private static final int EVENT_INVALID_LICENSE_PLATE=205;
	private static final int EVENT_SCAN_STATE_NOTIFY=301;
	public static final int EVENT_DISPLAY_QUERY_RESULT = 401;
	public static final int EVENT_DISPLAY_REQUEST_TIMEOUT = 402;
	public static final int EVENT_DISPLAY_CONNECT_TIMEOUT = 403;
	public static final int EVENT_ENTER_ARRIVING = 501;
	public static final int EVENT_ENTER_LEAVING = 502;
    private static final String FILE_NAME_TOKEN = "save_pref_token";
	private Fragment mNumberFragment;
	private Fragment mLetterFragment;
	private Fragment mLocationFragment;
	private EditText mLicensePlateET;
	private TextView mNumberTV;
	private TextView mLetterTV;
	private TextView mLocationTV;
	private Button mScanBT;
	private Button mNextBT;
	private int mCurrentId;
	private int mType;
	private String mParkingNumber = "P1234";
	private DBAdapter mDBAdapter;
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
    	mDBAdapter = new DBAdapter(this);
        Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		if(bundle.getInt("type")==ARRIVING_TYPE){
			mType=ARRIVING_TYPE;
		}else if(bundle.getInt("type")==LEAVING_TYPE){
			mType=LEAVING_TYPE;
		}
        setContentView(R.layout.activity_input_license);
        mLicensePlateET = (EditText) findViewById(R.id.et_license_plate);
        mLetterTV = (TextView) findViewById(R.id.tv_letter);
        mNumberTV = (TextView) findViewById(R.id.tv_number);
        mLocationTV = (TextView) findViewById(R.id.tv_location);
    	mScanBT = (Button) findViewById(R.id.bt_scan);
    	mNextBT = (Button) findViewById(R.id.bt_next);
        mLetterTV.setOnClickListener(mTabClickListener); 
    	mNumberTV.setOnClickListener(mTabClickListener);
    	mLocationTV.setOnClickListener(mTabClickListener);
    	changeSelect(R.id.tv_location);
    	changeFragment(R.id.tv_location);
    	mLicensePlateET.setOnTouchListener(new OnTouchListener() {	 
    	      @Override
    	      public boolean onTouch(View v, MotionEvent event) {
    	        // et.getCompoundDrawables()得到一个长度为4的数组，分别表示左右上下四张图片
    	        Drawable drawable = mLicensePlateET.getCompoundDrawables()[2];
    	        //如果右边没有图片，不再处理
    	        if (drawable == null)
    	            return false;
    	        //如果不是按下事件，不再处理
    	        if (event.getAction() != MotionEvent.ACTION_UP)
    	            return false;
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
        transaction.commit();//提交事务  
    }

	private void hideFragments(FragmentTransaction transaction){  
        if (mLetterFragment != null)  
            transaction.hide(mLetterFragment);
        if (mNumberFragment != null)
            transaction.hide(mNumberFragment);
        if (mLocationFragment != null)
            transaction.hide(mLocationFragment);
    }

	private void changeSelect(int resId) {  
		mLetterTV.setSelected(false);
		mLetterTV.setBackgroundResource(R.color.gray);
		mNumberTV.setSelected(false);
		mNumberTV.setBackgroundResource(R.color.gray);
		mLocationTV.setSelected(false);
		mLocationTV.setBackgroundResource(R.color.gray);
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
					arrivingBundle.putString("licensePlate",mLicensePlateET.getText().toString());
					arrivingIntent.putExtras(arrivingBundle);
					startActivity(arrivingIntent);
	            	break;
	            case EVENT_ENTER_LEAVING:
					Intent leavingIntent = new Intent(InputLicenseActivity.this,LeavingActivity.class);
					Bundle leavingBundle = new Bundle();
					leavingBundle.putString("licensePlate",mLicensePlateET.getText().toString() );
					leavingIntent.putExtras(leavingBundle);
					startActivity(leavingIntent);
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
		  String strurl = "http://" + this.getString(R.string.ip) + ":8080/park/collector/license/analysis";
		  HttpPost request = new HttpPost(strurl);
		  request.addHeader("Accept","application/json");
		  request.setHeader("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
		  JSONObject param = new JSONObject();
		  param.put("token", readToken());
		  param.put("type", mType);
		  param.put("parkingNumber", mParkingNumber);
		  param.put("licensePlateNumber", mLicensePlateET.getText().toString());
		  StringEntity se = new StringEntity(param.toString(), "UTF-8");
		  request.setEntity(se);//发送数据
		  try{
			  HttpResponse httpResponse = httpClient.execute(request);//获得响应
			  int code = httpResponse.getStatusLine().getStatusCode();
			  if(code==HttpStatus.SC_OK){
				  String strResult = EntityUtils.toString(httpResponse.getEntity());
				  CommonResponse res = new CommonResponse(strResult);
				  Log.e("clientInsert","resCode is  " + res.getResCode());
				  Log.e("clientInsert","resMsg is  " + res.getResMsg());
				  Log.e("clientInsert","List is  " + res.getDataList());
				  Log.e("clientInsert","Map is  " + res.getPropertyMap());
				  String resCode = res.getResCode();
				  Message msg = new Message();
		          msg.what=EVENT_DISPLAY_QUERY_RESULT;
		          msg.obj= res.getResMsg();
		          mHandler.sendMessage(msg);
				  if(resCode.equals("100")){
					  return true;
				  }else if(resCode.equals("201")){
					  return false;
				  }else if(resCode.equals("202")){
					  return false;
				  }else if(resCode.equals("203")){
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
				Log.e("clientSendLicense","LicenseTask doInBackground");  
				return clientSendLicense();
			}catch(Exception e){
				e.printStackTrace();
			}
			return false;
		}

		@Override
		protected void onPostExecute(final Boolean success) {
			 mLicenseTask = null;
			 Log.e("clientSendLicense","LicenseTask onPostExecute " + success.toString());  
			if(success){
				if(mType == ARRIVING_TYPE){
					  Message msg = new Message();
			          msg.what=EVENT_ENTER_ARRIVING;
			          mHandler.sendMessage(msg);
				}else if(mType == LEAVING_TYPE){
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
    /*public class SQLThread extends Thread {
    @Override
    public void run () {
    	mDBAdapter.open();
    	Cursor cursor = mDBAdapter.getParkingByLicensePlate(mLicensePlateET.getText().toString());
    	if(cursor.getCount() == 0){
    		if(mType==ARRIVING_TYPE){
        		Message msg = new Message();
        		msg.what=	EVENT_ENTER_PARK_INFORMATION;
        		mHandler.sendMessage(msg);
    		}else if(mType==LEAVING_TYPE){
        		Message msg = new Message();
        		msg.what=	EVENT_NOT_EXISTS_LICENSE_PLATE;
        		mHandler.sendMessage(msg);
    		}
    	}
        try {
        	cursor.moveToFirst();
        	if(mType==ARRIVING_TYPE){
            	if(cursor.getString(cursor.getColumnIndex("paymentpattern")).equals("未付")){
            		Message msg = new Message();
            		msg.what=EVENT_UNFINISHED_LICENSE_PLATE;
            		mHandler.sendMessage(msg);
            	}else if(cursor.getString(cursor.getColumnIndex("paymentpattern")).equals("逃费")){
            		Message msg = new Message();
            		msg.what=EVENT_ESCAPE_LICENSE_PLATE;
            		mHandler.sendMessage(msg);
            	}else{
            		Message msg = new Message();
            		msg.what=	EVENT_ENTER_PARK_INFORMATION;
            		mHandler.sendMessage(msg);
            	}	
        	}else if(mType==LEAVING_TYPE){
            	if(cursor.getString(cursor.getColumnIndex("paymentpattern")).equals("未付")){
					Intent intent = new Intent(InputLicenseActivity.this,LeavingActivity.class);
					Bundle bundle = new Bundle();
					bundle.putString("licensePlate",mLicensePlateET.getText().toString() );
					intent.putExtras(bundle);
					startActivity(intent);
            	}else if(cursor.getString(cursor.getColumnIndex("paymentpattern")).equals("现金支付") ||
            			cursor.getString(cursor.getColumnIndex("paymentpattern")).equals("移动支付") ||
            			cursor.getString(cursor.getColumnIndex("paymentpattern")).equals("逃费")) {
            		Message msg = new Message();
            		msg.what=	EVENT_NOT_EXISTS_LICENSE_PLATE;
            		mHandler.sendMessage(msg);
            	}
        	}
        }
        catch (Exception e) {
                e.printStackTrace();
        } finally{
            	if(cursor!=null){
            		cursor.close();
                }
        }
    }
}*/
}
