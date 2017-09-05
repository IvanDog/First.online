package com.example.parking;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class ParkingSpaceDetailActivity extends FragmentActivity {
	private Fragment mParkingInformationFragment;
	private Fragment mTodayRecordFragment;
	private TextView mParkingInformationTV;
	private TextView mRecordOfTodayTV;
	private int mCurrentId;
	private String mLicensePlateNumber;
    private static final String FILE_NAME_COLLECTOR = "save_pref_collector";
    private static final String FILE_NAME_TOKEN = "save_pref_token";
	public static String LOG_TAG = "ParkingSpaceDetailActivity";
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
        setContentView(R.layout.activity_parking_space_detail);
        mParkingInformationTV = (TextView) findViewById(R.id.parkingInformation);
        mRecordOfTodayTV = (TextView) findViewById(R.id.recordOfToday);
        mParkingInformationTV.setOnClickListener(mTabClickListener); 
        mRecordOfTodayTV.setOnClickListener(mTabClickListener);
        changeSelect(R.id.parkingInformation);
        changeFragment(R.id.parkingInformation);
		getActionBar().setDisplayHomeAsUpEnabled(true); 
        IntentFilter filter = new IntentFilter();  
        filter.addAction("ExitApp");  
        filter.addAction("BackMain");  
        registerReceiver(mReceiver, filter); 
	}

	private void changeFragment(int resId) {  
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();//开启一个Fragment事务  
        hideFragments(transaction);//隐藏所有fragment  
        if(resId==R.id.parkingInformation){
        	mParkingInformationFragment = new ParkingInformationFragment();
        	 transaction.replace(R.id.parking_container, mParkingInformationFragment);
/*            if(mParkingInformationFragment==null){//如果为空先添加进来.不为空直接显示  
            	mParkingInformationFragment = new ParkingInformationFragment();  
                transaction.add(R.id.parking_container,mParkingInformationFragment);  
            }else {  
            	transaction.replace(R.id.parking_container, mParkingInformationFragment);
            }*/
        }else if(resId==R.id.recordOfToday){
        	mTodayRecordFragment = new TodayRecordFragment();  
        	transaction.replace(R.id.parking_container, mTodayRecordFragment);
            /*if(mTodayRecordFragment==null){//如果为空先添加进来.不为空直接显示  
            	mTodayRecordFragment = new TodayRecordFragment();  
                transaction.add(R.id.parking_container,mTodayRecordFragment);  
            }else {  
                transaction.show(mTodayRecordFragment);  
            }*/
        }
        transaction.commit();//一定要记得提交事务  
    }

	private void hideFragments(FragmentTransaction transaction){  
        if (mParkingInformationFragment != null) {
        	transaction.hide(mParkingInformationFragment);
        }else if(mTodayRecordFragment!=null){
        	transaction.hide(mTodayRecordFragment);
        }
    }

	private void changeSelect(int resId) {  
		mParkingInformationTV.setSelected(false);
		mParkingInformationTV.setBackgroundResource(R.color.gray);
		mRecordOfTodayTV.setSelected(false);  
		mRecordOfTodayTV.setBackgroundResource(R.color.gray);
        switch (resId) {  
        case R.id.parkingInformation:  
        	mParkingInformationTV.setSelected(true);
        	mParkingInformationTV.setBackgroundResource(R.color.orange);
            break;  
        case R.id.recordOfToday:  
        	mRecordOfTodayTV.setSelected(true);
        	mRecordOfTodayTV.setBackgroundResource(R.color.orange);
            break;
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
    
    public String readCollector(String data) {
        SharedPreferences pref = getSharedPreferences(FILE_NAME_COLLECTOR, MODE_MULTI_PROCESS);
        String str = pref.getString(data, "");
        return str;
    }
    
    public String readToken() {
        SharedPreferences pref = getSharedPreferences(FILE_NAME_TOKEN, MODE_MULTI_PROCESS);
        String str = pref.getString("token", "");
        return str;
    }
    
    public String readAccount() {
        SharedPreferences pref = getSharedPreferences(FILE_NAME_COLLECTOR, MODE_MULTI_PROCESS);
        String str = pref.getString("collectorNumber", "");
        return str;
    }
	/**
	 * Add for request to search detail information for licensenumber
	public void requestSearchDetail()throws ParseException, IOException, JSONException{
		  HttpClient httpClient = new DefaultHttpClient();
		  String strurl = "//此处url待定";
		  HttpPost request = new HttpPost(strurl);
		  request.addHeader("Accept","application/json");
		  request.addHeader("Content-Type","application/json");//还可以自定义增加header
		  JSONObject param = new JSONObject();//定义json对象
		  param.put("type", "detailsearch");
		  param.put("licenseplatenumber", mLicensePlateNumber);
		  Log.e("yifan", param.toString());
		  StringEntity se = new StringEntity(param.toString());
		  request.setEntity(se);//发送数据
		  HttpResponse httpResponse = httpClient.execute(request);//获得相应
		  int code = httpResponse.getStatusLine().getStatusCode();
		  if(code==HttpStatus.SC_OK){
			  String strResult = EntityUtils.toString(httpResponse.getEntity());
			  JSONObject result = new JSONObject(strResult);
			  String carType = (String) result.get("cartype");
			  String parkType = (String) result.get("parktype");
			  int locationNumber = (Integer) result.get("locaionnumber");
			  String startTime = (String) result.get("starttime");
		  }else{
			  Log.e("yifan", Integer.toString(code));
		  }
		 }
	//Client's json:{ "type":"detailsearch", "licenseplatenumber":"津HG9025"}
	//Server's json:{ "cartype":"小客车", "parktype":"普通停车", "locationnumber":1, "starttime":"2017-05-04 15:49:20"}
	*/
}
