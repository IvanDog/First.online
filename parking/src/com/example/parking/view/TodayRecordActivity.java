package com.example.parking.view;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.example.parking.info.TodayRecordSearchInfo;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ParseException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class TodayRecordActivity extends Activity {
	public static final int NO_TODAY_PARKING_RECORD =101;
	public static final int EVENT_DISPLAY_QUERY_RESULT = 201;
	public static final int EVENT_DISPLAY_REQUEST_TIMEOUT = 202;
	public static final int EVENT_DISPLAY_CONNECT_TIMEOUT = 203;
	public static final int EVENT_SET_ADAPTER = 204;
	public static String LOG_TAG = "TodayRecordActivity";
    private static final String FILE_NAME_COLLECTOR = "save_pref_collector";
    private static final String FILE_NAME_TOKEN = "save_pref_token";
	private ListView mListView;
	private TextView mEmptyNotifyTV;
	private int mLocationNumber;
    private UserQueryTask mQueryTask = null;
    private ArrayList<HashMap<String, Object>> mList = new ArrayList<HashMap<String, Object>>();
    private TodayRecordListAdapter mTodayRecordListAdapter;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_today_record);
        mListView=(ListView)findViewById(R.id.list_record_today);  
        mEmptyNotifyTV=(TextView)findViewById(R.id.tv_notify_today_record_activity_list_empty);  
        Intent intent = getIntent();
        mLocationNumber=intent.getExtras().getInt("locationNumber");
		mQueryTask = new UserQueryTask();
		mQueryTask.execute((Void) null);
		getActionBar().setDisplayHomeAsUpEnabled(true); 
        IntentFilter filter = new IntentFilter();  
        filter.addAction("ExitApp");  
        filter.addAction("BackMain"); 
        registerReceiver(mReceiver, filter); 
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
	
	private Handler mHandler = new Handler() {
	    @Override
	    public void handleMessage (Message msg) {
	        super.handleMessage(msg);
	        switch (msg.what) {
	            case NO_TODAY_PARKING_RECORD:
	            	mEmptyNotifyTV.setVisibility(View.VISIBLE);
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
	            case EVENT_SET_ADAPTER:
	            	mTodayRecordListAdapter= new TodayRecordListAdapter(getApplicationContext(), mList);
	    	        mListView.setAdapter(mTodayRecordListAdapter); 
	    	        mTodayRecordListAdapter.notifyDataSetChanged();
	            	break;
	            default:
	                break;
	        }
	    }
	};
	
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
    
    public String getDate(){
		SimpleDateFormat formatter = new SimpleDateFormat ("yyyy-MM-dd"); 
		Date curDate = new Date(System.currentTimeMillis());
		String date = formatter.format(curDate);
		return date;
    }
    /**
	 * Add for request today record and receive result
	 * */
	public boolean clientQuery() throws ParseException, IOException, JSONException{
		Log.e(LOG_TAG,"clientTodayQuery-> enter clientTodayQuery");  
		HttpClient httpClient = new DefaultHttpClient();
		  httpClient.getParams().setIntParameter(  
                  HttpConnectionParams.SO_TIMEOUT, 5000); // 请求超时设置,"0"代表永不超时  
		  httpClient.getParams().setIntParameter(  
                  HttpConnectionParams.CONNECTION_TIMEOUT, 5000);// 连接超时设置 
		  String strurl = "http://" + 	this.getString(R.string.ip) + ":8080/itspark/collector/queryToday/query";
		  HttpPost request = new HttpPost(strurl);
		  request.addHeader("Accept","application/json");
		//request.setHeader("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
		  request.setHeader("Content-Type", "application/json; charset=utf-8");
		  JSONObject param = new JSONObject();
		  TodayRecordSearchInfo info = new TodayRecordSearchInfo();
		  CommonRequestHeader header = new CommonRequestHeader();
		  header.addRequestHeader(CommonRequestHeader.REQUEST_COLLECTOR_QUERY_TODAY_PARKING_RECORD_CODE,
				  readAccount(), readToken());
		  info.setHeader(header);
		  info.setParkNumber(readCollector("parkNumber"));
		  info.setParkingLocation(String.valueOf(mLocationNumber));
		  StringEntity se = new StringEntity(JacksonJsonUtil.beanToJson(info), "UTF-8");
		  Log.e(LOG_TAG,"clientTodayQuery-> param is " + JacksonJsonUtil.beanToJson(info));
		  request.setEntity(se);//发送数据
		  try{
			  HttpResponse httpResponse = httpClient.execute(request);//获得响应
			  int code = httpResponse.getStatusLine().getStatusCode();
			  if(code==HttpStatus.SC_OK){
				  String strResult = EntityUtils.toString(httpResponse.getEntity());
				  Log.e(LOG_TAG,"clientTodayQuery->strResult is " + strResult);
				  CommonResponse res = new CommonResponse(strResult);
				  Message msg = new Message();
		          msg.what=EVENT_DISPLAY_QUERY_RESULT;
		          msg.obj= res.getResMsg();
		          mHandler.sendMessage(msg);
				  if(res.getResCode().equals("100")){
					  mList = res.getDataList();
					  return true;
				  }else{
			          return false;
				  } 
			}else{
					  Log.e(LOG_TAG, "clientTodayQuery->error code is " + Integer.toString(code));
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
	 * 当日记录Task
	 * 
	 */
	public class UserQueryTask extends AsyncTask<Void, Void, Boolean> {
		@Override
		protected Boolean doInBackground(Void... params) {
			try{
				Log.e(LOG_TAG,"UserQueryTask->doInBackground");  
				return clientQuery();
			}catch(Exception e){
				Log.e(LOG_TAG,"UserQueryTask-> exists exception ");  
				e.printStackTrace();
			}
			return false;
		}

		@Override
		protected void onPostExecute(final Boolean success) {
			mQueryTask = null;
			if(success){
			    if(mList.size()!=0){
				    Message msg = new Message();
				    msg.what=EVENT_SET_ADAPTER;
				    mHandler.sendMessage(msg);
			    }else{
				    Message msg = new Message();
				    msg.what=NO_TODAY_PARKING_RECORD;
				    mHandler.sendMessage(msg);
			    }				
			}
		}

		@Override
		protected void onCancelled() {
			mQueryTask = null;
		}
		
	}
	
    private String readCollector(String data) {
        SharedPreferences pref = getSharedPreferences(FILE_NAME_COLLECTOR, MODE_MULTI_PROCESS);
        String str = pref.getString(data, "");
        return str;
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

}

