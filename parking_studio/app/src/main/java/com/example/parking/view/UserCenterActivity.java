package com.example.parking.view;

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

import com.example.parking.R;
import com.example.parking.R.drawable;
import com.example.parking.R.id;
import com.example.parking.R.layout;
import com.example.parking.R.string;
import com.example.parking.common.JacksonJsonUtil;
import com.example.parking.info.CommonRequestHeader;
import com.example.parking.info.CommonResponse;
import com.example.parking.info.LogoutInfo;

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
import android.os.Message;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class UserCenterActivity extends Activity {
	private ListView mListView;
    private UserLogoutTask mLogoutTask = null;
    private static final String FILE_NAME_TOKEN = "save_pref_token";
    private static final String FILE_NAME_COLLECTOR = "save_pref_collector";
	public static String LOG_TAG = "UserCenterActivity";
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_center);
		mListView=(ListView)findViewById(R.id.list_function_user_center);  
        List<Map<String, Object>> list=getData();  
        mListView.setAdapter(new UserCenterListAdapter(this, list));
        mListView.setOnItemClickListener(new OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                    long arg3) {
                // TODO Auto-generated method stub
            	Map<String,Object> map=(Map<String,Object>)mListView.getItemAtPosition(arg2);
                String userCenterFunction=(String)map.get("userCenterFunction");
                if(userCenterFunction.equals("消息中心")){
                	Intent intent = new Intent(UserCenterActivity.this,MessageCenterActivity.class);
                	startActivityForResult(intent,0);
                }else if(userCenterFunction.equals("重置密码")){
                	Intent intent = new Intent(UserCenterActivity.this,ResetPasswdActivity.class);
                	startActivityForResult(intent,1);
                }else if(userCenterFunction.equals("退出账号")){
                	showExitDialog();
                }
            }
        });
		getActionBar().setDisplayHomeAsUpEnabled(true); 
        IntentFilter filter = new IntentFilter();  
        filter.addAction("ExitApp");  
        registerReceiver(mReceiver, filter); 
	}

    @Override  
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {  
        super.onActivityResult(requestCode, resultCode, data);  
        if(requestCode==0){  
            // TODO Auto-generated method stub  
        }else if(requestCode==1){  
            // TODO Auto-generated method stub  
        }
    } 
    
    public List<Map<String, Object>> getData(){  
        List<Map<String, Object>> list=new ArrayList<Map<String,Object>>();  
        for (int i = 1; i <= 3; i++) {  
            Map<String, Object> map=new HashMap<String, Object>();  
            if(i==1){
                map.put("userCenterFunction",  "消息中心");
                map.put("userCenterFunctionSpreadImage",  drawable.ic_chevron_right_black_24dp);
                map.put("userCenterFunctionImage",  drawable.ic_message_black_18dp);
            }else if(i==2){
                map.put("userCenterFunction",  "重置密码");
                map.put("userCenterFunctionSpreadImage",  drawable.ic_chevron_right_black_24dp);
                map.put("userCenterFunctionImage",  drawable.ic_lock_black_18dp);
            }else if(i==3){
            	map.put("userCenterFunction",  "退出账号");
            	map.put("userCenterFunctionSpreadImage",  drawable.ic_chevron_right_black_24dp);
                map.put("userCenterFunctionImage",  drawable.ic_power_settings_new_black_18dp);
            }
            list.add(map);  
        }  
        return list;  
      }

    private void showExitDialog(){
        final AlertDialog.Builder exitDialog = new AlertDialog.Builder(UserCenterActivity.this);
        exitDialog.setIcon(R.drawable.ic_exit_to_app_white_24dp);
        exitDialog.setTitle("退出账号");
        exitDialog.setMessage("确定退出当前账号？");
        exitDialog.setPositiveButton("确定",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
				mLogoutTask = new UserLogoutTask();
				mLogoutTask.execute((Void) null);
            }
        });
        exitDialog.setNegativeButton("关闭",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //TODO
            }
        });
        exitDialog.show();
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
    
    /**
	 * Add for logout
	 * */
	public boolean clientLogout()throws ParseException, IOException, JSONException{
		  HttpClient httpClient = new DefaultHttpClient();
		  httpClient.getParams().setIntParameter(  
                  HttpConnectionParams.SO_TIMEOUT, 5000); // 请求超时设置,"0"代表永不超时  
		  httpClient.getParams().setIntParameter(  
                  HttpConnectionParams.CONNECTION_TIMEOUT, 5000);// 连接超时设置,"0"代表永不超时
		  String strurl = "http://" + 	this.getString(R.string.ip) + "/itspark/collector/userCenter/logout";
		  HttpPost request = new HttpPost(strurl);
		  request.addHeader("Accept","application/json");
		  //request.setHeader("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
		  request.setHeader("Content-Type", "application/json; charset=utf-8");
		  LogoutInfo info = new LogoutInfo();
		  CommonRequestHeader header = new CommonRequestHeader();
		  header.addRequestHeader(CommonRequestHeader.REQUEST_COLLECTOR_LOGOUT_CODE, readAccount(), readToken());
		  info.setHeader(header);
		  StringEntity se = new StringEntity(JacksonJsonUtil.beanToJson(info), "UTF-8");
		  Log.e(LOG_TAG,"clientLogin-> param is " + JacksonJsonUtil.beanToJson(info));
		  request.setEntity( se);//发送数据
		  try{
			  HttpResponse httpResponse = httpClient.execute(request);//获得响应
			  int code = httpResponse.getStatusLine().getStatusCode();
			  if(code==HttpStatus.SC_OK){
				  String strResult = EntityUtils.toString(httpResponse.getEntity());
				  Log.e(LOG_TAG,"clientLogout->strResult is " + strResult);
				  CommonResponse res = new CommonResponse(strResult);
				  String resCode = res.getResCode();
				  toastWrapper(res.getResMsg());
				  if(resCode.equals("100")){
					  return true;
				  }else{
					  return false;
				  }
			  }else{
				  Log.e(LOG_TAG, "clientLogout-。error code is " + Integer.toString(code));
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
	 * 退出账号Task
	 * 
	 */
	public class UserLogoutTask extends AsyncTask<Void, Void, Boolean> {
		@Override
		protected Boolean doInBackground(Void... params) {
			try{
				return clientLogout();
			}catch(Exception e){
				e.printStackTrace();
			}
			return false;
		}

		@Override
		protected void onPostExecute(final Boolean success) {
			mLogoutTask = null;
			if(success){
                Intent intentFinsh = new Intent();  
                intentFinsh.setAction("ExitApp");  
                sendBroadcast(intentFinsh); 
				Intent intent = new Intent(UserCenterActivity.this,LoginActivity.class);
				startActivity(intent);
				finish();
			}
		}
	}
	
	/**
	 * 封装Toast
	 * */
	 private void toastWrapper(final String str) {
	      runOnUiThread(new Runnable() {
	          @Override
	           public void run() {
	               Toast.makeText(UserCenterActivity.this, str, Toast.LENGTH_SHORT).show();
	           }
	      });
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
}
