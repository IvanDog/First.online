package com.example.parking.view;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.security.MessageDigest;

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
import com.example.parking.info.CommonRequestHeader;
import com.example.parking.info.CommonResponse;


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
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ResetPasswdActivity extends Activity {
	private static final int EVENT_INPUT_UNFINISHED = 101;
	private static final int EVENT_INPUT_RENEW_ERROR =102;
	private static final int EVENT_MODIFY_SUCCESS = 103;
    private static final String FILE_NAME_TOKEN = "save_pref_token";
    private static final String FILE_NAME_COLLECTOR = "save_pref_collector";
	public static String LOG_TAG = "ResetPasswdActivity";
	private Button mConfirmBT;
	private Button mCancelBT;
	private EditText mOldPasswdET;
	private EditText mNewPasswdET;
	private EditText mRenewPasswdET;
    private UserResetTask mResetTask = null;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_reset_passwd);
		mOldPasswdET=(EditText)findViewById(R.id.et_old_passwd);
		mNewPasswdET=(EditText)findViewById(R.id.et_new_passwd);
		mRenewPasswdET=(EditText)findViewById(R.id.et_renew_passwd);
		mConfirmBT=(Button)findViewById(R.id.bt_confirm_reset_passwd);
		mConfirmBT.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				if((mOldPasswdET.getText().toString()).equals("") || (mNewPasswdET.getText().toString()).equals("") 
						|| (mRenewPasswdET.getText().toString()).equals("")){
					Message msg = new Message();
	                msg.what = EVENT_INPUT_UNFINISHED;
	                mHandler.sendMessage(msg);
				}else if(!(mNewPasswdET.getText().toString()).equals(mRenewPasswdET.getText().toString())){
					Message msg = new Message();
	                msg.what = EVENT_INPUT_RENEW_ERROR;
	                mHandler.sendMessage(msg);
				}else{
					mResetTask = new UserResetTask(mOldPasswdET.getText().toString(),mNewPasswdET.getText().toString());
					mResetTask.execute((Void) null);
				}
			}
		});
		mCancelBT=(Button)findViewById(R.id.bt_cancel_reset_passwd);
		mCancelBT.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				finish();
		    }
		});
		getActionBar().setDisplayHomeAsUpEnabled(true); 
        IntentFilter filter = new IntentFilter();  
        filter.addAction("ExitApp");  
        registerReceiver(mReceiver, filter); 
	}
	
	private Handler mHandler = new Handler() {
        @Override
        public void handleMessage (Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case EVENT_INPUT_UNFINISHED:
            	    Toast.makeText(getApplicationContext(), "请完成输入", Toast.LENGTH_SHORT).show();
            	    break;
                case EVENT_INPUT_RENEW_ERROR:
                	Toast.makeText(getApplicationContext(), "两次输入不一致", Toast.LENGTH_SHORT).show();
                	break;
                case EVENT_MODIFY_SUCCESS:
                	finish();
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
			}
		}            
    }; 
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }
	
    /**
	 * Add for reset password
	 * */
	public boolean clientReset(String oldPassword, String newPassword)throws ParseException, IOException, JSONException{
		  HttpClient httpClient = new DefaultHttpClient();
		  httpClient.getParams().setIntParameter(  
                  HttpConnectionParams.SO_TIMEOUT, 5000); // 请求超时设置,"0"代表永不超时  
		  httpClient.getParams().setIntParameter(  
                  HttpConnectionParams.CONNECTION_TIMEOUT, 5000);// 连接超时设置,"0"代表永不超时
		  String strurl = "http://" + 	this.getString(R.string.ip) + ":8080/park/collector/reset/reset";
		  HttpPost request = new HttpPost(strurl);
		  request.addHeader("Accept","application/json");
		//request.setHeader("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
		  request.setHeader("Content-Type", "application/json; charset=utf-8");
		  JSONObject param = new JSONObject();
		  CommonRequestHeader header = new CommonRequestHeader();
		  header.addRequestHeader(CommonRequestHeader.REQUEST_COLLECTOR_RESET_PASSWORD_CODE, readAccount(), readToken());
		  param.put("header", header);
		  param.put("password", getMD5Code(oldPassword));
		  param.put("newPassword", getMD5Code(newPassword));
		  StringEntity se = new StringEntity(param.toString(), "UTF-8");
		  request.setEntity(se);
		  try{
			  HttpResponse httpResponse = httpClient.execute(request);//获得响应
			  int code = httpResponse.getStatusLine().getStatusCode();
			  if(code==HttpStatus.SC_OK){
				  String strResult = EntityUtils.toString(httpResponse.getEntity());
				  Log.e(LOG_TAG,"clientReset->strResult is " + strResult);
				  CommonResponse res = new CommonResponse(strResult);
				  String resCode = res.getResCode();
				  toastWrapper(res.getResMsg());
				  if(resCode.equals("100")){
					  return true;
				  }else if(resCode.equals("201")){
					  return false;
				  }else if(resCode.equals("202")){
					  return false;
				  }
			  }else{
				  Log.e(LOG_TAG, "clientReset->error code is " + Integer.toString(code));
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
	 * 重置密码Task
	 * 
	 */
	public class UserResetTask extends AsyncTask<Void, Void, Boolean> {
		private String oldPasswd;
		private String newPasswd;
		public UserResetTask(String oldPasswd,String newPasswd){
			this.oldPasswd = oldPasswd;
			this.newPasswd = newPasswd;
		}
		@Override
		protected Boolean doInBackground(Void... params) {
			try{
				return clientReset(oldPasswd,newPasswd);
			}catch(Exception e){
				e.printStackTrace();
			}
			return false;
		}

		@Override
		protected void onPostExecute(final Boolean success) {
			mResetTask = null;
			if(success){
				Message msg = new Message();
                msg.what = EVENT_MODIFY_SUCCESS;
                mHandler.sendMessage(msg);
			}
		}

		@Override
		protected void onCancelled() {
			mResetTask = null;
		}
		
	}
	
	/**
	 * MD5 算法
	 * */
	public String getMD5Code(String info) {  
	    try {  
	        MessageDigest md5 = MessageDigest.getInstance("MD5");  
	        md5.update(info.getBytes("UTF-8"));  
	        byte[] encryption = md5.digest();  
	  
	        StringBuffer strBuf = new StringBuffer();  
	        for (int i = 0; i < encryption.length; i++) {  
	            if (Integer.toHexString(0xff & encryption[i]).length() == 1) {  
	                strBuf.append("0").append(  
	                        Integer.toHexString(0xff & encryption[i]));  
	            } else {  
	                strBuf.append(Integer.toHexString(0xff & encryption[i]));  
	            }  
	        }  
	        return strBuf.toString();  
	    } catch (Exception e) {  
	        // TODO: handle exception  
	        return "";  
	    }  
	}
	
	/**
	 * 封装Toast
	 * */
	 private void toastWrapper(final String str) {
	      runOnUiThread(new Runnable() {
	          @Override
	           public void run() {
	               Toast.makeText(ResetPasswdActivity.this, str, Toast.LENGTH_SHORT).show();
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
