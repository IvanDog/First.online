package com.example.parking;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.util.ArrayList;
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

import com.example.parking.R.drawable;
import com.example.parking.TodayRecordActivity.UserQueryTask;

import android.app.Activity;
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
import android.text.format.DateFormat;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class MessageCenterActivity extends Activity {
	public static final int NO_MESSAGE =101;
	public static final int EVENT_SET_ADAPTER = 102;
    private static final String FILE_NAME_TOKEN = "save_pref_token";
    private static final String FILE_NAME_COLLECTOR = "save_pref_collector";
    private UserGetTask mGetTask= null;
    private ArrayList<HashMap<String, Object>> mList = new ArrayList<HashMap<String, Object>>();
    private MessageCenterListAdapter mMessageCenterListAdapter;
	private ListView mListView;
	private TextView mEmptyNotifyTV;

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_message_center);
		mListView=(ListView)findViewById(R.id.list_message_center);  
		mEmptyNotifyTV=(TextView)findViewById(R.id.tv_notify_message_list_empty); 
        //List<Map<String, Object>> list=getData();  
        //mListView.setAdapter(new MessageCenterListAdapter(this, list)); 
        mListView.setOnItemClickListener(new OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                    long arg3) {
            	//TODO
            }
        });
        mGetTask = new UserGetTask();
        mGetTask.execute((Void) null);
		getActionBar().setDisplayHomeAsUpEnabled(true); 
        IntentFilter filter = new IntentFilter();  
        filter.addAction("ExitApp");  
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
	
    private BroadcastReceiver mReceiver = new BroadcastReceiver(){  
		@Override
		public void onReceive(Context context, Intent intent) {
			if(intent.getAction()!=null && intent.getAction().equals("ExitApp")){
				finish();
			}
		}            
    }; 
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }

	private Handler mHandler = new Handler() {
        @Override
        public void handleMessage (Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case NO_MESSAGE:
            	    mEmptyNotifyTV.setVisibility(View.VISIBLE);
                    break;
                case EVENT_SET_ADAPTER:
            	    mMessageCenterListAdapter= new MessageCenterListAdapter(getApplicationContext(), mList);
    	            mListView.setAdapter(mMessageCenterListAdapter); 
    	            mMessageCenterListAdapter.notifyDataSetChanged();
            	    break;
                default:
                    break;
            }
        }
    };
    
    /**
	 * Add for get message
	 * */
	public boolean clientGet() throws ParseException, IOException, JSONException{
		Log.e("clientGet","enter clientGet");  
		HttpClient httpClient = new DefaultHttpClient();
		  httpClient.getParams().setIntParameter(  
                  HttpConnectionParams.SO_TIMEOUT, 5000); // 请求超时设置,"0"代表永不超时  
		  httpClient.getParams().setIntParameter(  
                  HttpConnectionParams.CONNECTION_TIMEOUT, 5000);// 连接超时设置 
		  String strurl = "http://" + 	this.getString(R.string.ip) + ":8080/park/collector/messageCenter/getMessage";
		  HttpPost request = new HttpPost(strurl);
		  request.addHeader("Accept","application/json");
		  request.setHeader("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
		  JSONObject param = new JSONObject();
		  param.put("token", readToken());
		  param.put("collectorNumber", readCollector("collectorNumber"));
		  StringEntity se = new StringEntity(param.toString(), "UTF-8");
		  request.setEntity(se);//发送数据
		  try{
			  HttpResponse httpResponse = httpClient.execute(request);//获得响应
			  int code = httpResponse.getStatusLine().getStatusCode();
			  if(code==HttpStatus.SC_OK){
				  String strResult = EntityUtils.toString(httpResponse.getEntity());
				  Log.e("clientGet","strResult is " + strResult);
				  CommonResponse res = new CommonResponse(strResult);
				  Log.e("clientGet","resCode is  " + res.getResCode());
				  Log.e("clientGet","resMsg is  " + res.getResMsg());
				  Log.e("clientGet","List is  " + res.getDataList());
				  Log.e("clientGet","Map is  " + res.getPropertyMap());
				  toastWrapper(res.getResMsg());  
				  if(res.getResCode().equals("100")){
					  mList = res.getDataList();
					  return true;
				  }else if(res.getResCode().equals("201")){
			          return false;
				  } 
			}else{
					  Log.e("clientGet", "error code is " + Integer.toString(code));
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
	 * 消息Task
	 * 
	 */
	public class UserGetTask extends AsyncTask<Void, Void, Boolean> {
		@Override
		protected Boolean doInBackground(Void... params) {
			try{
				Log.e("clientGet","UserGetTask doInBackground");  
				return clientGet();
			}catch(Exception e){
				Log.e("clientGet","Query exists exception ");  
				e.printStackTrace();
			}
			return false;
		}

		@Override
		protected void onPostExecute(final Boolean success) {
			mGetTask = null;
			if(success){
			    if(mList.size()!=0){
				    Message msg = new Message();
				    msg.what=EVENT_SET_ADAPTER;
				    mHandler.sendMessage(msg);
			    }else{
				    Message msg = new Message();
				    msg.what=NO_MESSAGE;
				    mHandler.sendMessage(msg);
			    }				
			}
		}

		@Override
		protected void onCancelled() {
			mGetTask = null;
		}
		
	}
	
	/**
	 * 封装Toast
	 * */
	 private void toastWrapper(final String str) {
	      runOnUiThread(new Runnable() {
	          @Override
	           public void run() {
	               Toast.makeText(MessageCenterActivity.this, str, Toast.LENGTH_SHORT).show();
	           }
	      });
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
    /*public List<Map<String, Object>> getData(){  
    List<Map<String, Object>> list=new ArrayList<Map<String,Object>>();  
    for (int i = 1; i <= 2; i++) {  
        Map<String, Object> map=new HashMap<String, Object>();  
        if(i==1){
            map.put("messageCenterImage",  drawable.ic_add_alert_black_18dp);
            map.put("messageCenterTitle", "考勤通知");
            map.put("messageCenterDetail", "您4月25日出现一次考勤异常");
            map.put("messageCenterTime", "2017.04.25" + " " + "15:15:40");
            map.put("messageCenterDetailHide", "您4月25日上班打卡时间08:40:36(上班时间9:00)，" +
            		"下班打卡时间15:30:23(下班时间17:30)，存在异常，请联系考勤员确认。");
        }else if(i==2){
            map.put("messageCenterImage",  drawable.ic_error_outline_black_18dp);
            map.put("messageCenterTitle", "停车通知");
            map.put("messageCenterDetail", "4月25日出现一次停车逃费现象");
            map.put("messageCenterTime", "2017.04.25" + " " + "16:25:36");
            map.put("messageCenterDetailHide", "4月25日出现一次停车逃费现象，入场时间11:15:36，" +
            		"牌照号津A00001，泊位号6，请联系稽查员确认。");
        }
        list.add(map);  
    }  
    return list;  
  }*/
}
