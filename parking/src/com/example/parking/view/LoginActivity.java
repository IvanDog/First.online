package com.example.parking.view;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

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
import com.example.parking.R.menu;
import com.example.parking.R.string;
import com.example.parking.common.JacksonJsonUtil;
import com.example.parking.common.RSAUtils;
import com.example.parking.info.CommonRequestHeader;
import com.example.parking.info.CommonResponse;
import com.example.parking.info.LoginInfo;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ParseException;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.service.textservice.SpellCheckerService.Session;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class LoginActivity extends Activity {

	private UserLoginTask mLoginTask = null;

	private String mAccount;
	private String mPassword;

	private EditText mAccountET;
	private EditText mPasswordET;
	private View mLoginFormView;
	private View mLoginStatusView;
	private TextView mLoginStatusMessageView;
    private CheckBox mKeepUserinfo;
    private CheckBox mKeepPassword;
	private String mPublicKey;
	private String mPrivateKey;
	private boolean mFirstLogin;
	private static final int ATTENDANCE_TYPE_START=301;
	private static final int ATTENDANCE_TYPE_END=302;
	private static final int ERROR_TYPE_EMAIL=401;
	private static final int ERROR_TYPE_PASSWD=402;
	private static final int ERROR_TYPE_NO_ERROR=403;
    private static final String FILE_NAME_NAME_PASSWD = "save_spref_name_passwd";
    private static final String FILE_NAME_TOKEN = "save_pref_token";
    private static final String FILE_NAME_COLLECTOR = "save_pref_collector";
	private int mErrorType = ERROR_TYPE_NO_ERROR;
	public static String LOG_TAG = "LoginActivity";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_login);

		mAccountET = (EditText) findViewById(R.id.email);

		mPasswordET = (EditText) findViewById(R.id.password);
		mPasswordET
				.setOnEditorActionListener(new TextView.OnEditorActionListener() {
					@Override
					public boolean onEditorAction(TextView textView, int id,
							KeyEvent keyEvent) {
						if (id == R.id.login || id == EditorInfo.IME_NULL) {
							attemptLogin();
							return true;
						}
						return false;
					}
				});

		mLoginFormView = findViewById(R.id.login_form);
		mLoginStatusView = findViewById(R.id.login_status);
		mLoginStatusMessageView = (TextView) findViewById(R.id.login_status_message);

		findViewById(R.id.sign_in_button).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						attemptLogin();
					}
				});
        mKeepUserinfo = (CheckBox) findViewById(R.id.ck_userinfo);
        mKeepPassword = (CheckBox) findViewById(R.id.ck_password);
        mKeepUserinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                writeBoolean(mKeepUserinfo.isChecked(), mKeepPassword.isChecked());
            }
        });
        mKeepPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                writeBoolean(mKeepUserinfo.isChecked(), mKeepPassword.isChecked());
            }
        });
        initView();
        IntentFilter filter = new IntentFilter();  
        filter.addAction("ExitApp");  
        registerReceiver(mReceiver, filter); 
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.activity_login, menu);
		return true;
	}

    private void initView() {
        if (readBoolean("isuserinfo")) {
        	mKeepUserinfo.setChecked(true);
        	mAccountET.setText(readData("userinfo").toString());
        }
        if (readBoolean("ispassword")) {
        	mKeepPassword.setChecked(true);
            mPasswordET.setText(readData("password").toString());
        }
    }

    private String readData(String data) {
        SharedPreferences pref = getSharedPreferences(FILE_NAME_NAME_PASSWD, MODE_MULTI_PROCESS);
        String str = pref.getString(data, "");
        return str;
    }

    private boolean writeData(String userinfo, String password, boolean isUserinfo, boolean isPassword) {
        SharedPreferences.Editor share_edit = getSharedPreferences(FILE_NAME_NAME_PASSWD,
                MODE_MULTI_PROCESS).edit();
        share_edit.putString("userinfo", userinfo);
        share_edit.putString("password", password);
        share_edit.putBoolean("isuserinfo", isUserinfo);
        share_edit.putBoolean("ispassword", isPassword);
        share_edit.commit();
        return true;
    }
    
    private boolean writeToken(String token) {
        SharedPreferences.Editor share_edit = getSharedPreferences(FILE_NAME_TOKEN,
                MODE_MULTI_PROCESS).edit();
        share_edit.putString("token", token);
        share_edit.commit();
        return true;
    }

    private boolean readBoolean(String data) {
        SharedPreferences pref = getSharedPreferences(FILE_NAME_NAME_PASSWD, MODE_MULTI_PROCESS);
        return pref.getBoolean(data, false);
    }

    private void writeBoolean(boolean isUserinfo, boolean isPassword) {
        SharedPreferences.Editor share_edit = getSharedPreferences(FILE_NAME_NAME_PASSWD,
                MODE_MULTI_PROCESS).edit();
        share_edit.putBoolean("isuserinfo", isUserinfo);
        share_edit.putBoolean("ispassword", isPassword);
        share_edit.commit();
    }

	/**
	 * Attempts to sign
	 */
	public void attemptLogin() {
		if (mLoginTask != null) {
			return;
		}

		// Reset errors.
		mAccountET.setError(null);
		mPasswordET.setError(null);

		// Store values at the time of the login attempt.
		mAccount = mAccountET.getText().toString();
		mPassword = mPasswordET.getText().toString();

		boolean cancel = false;
		View focusView = null;

		// Check for a valid password.
		if (TextUtils.isEmpty(mPassword)) {
			mPasswordET.setError(getString(R.string.error_passwd_required));
			focusView = mPasswordET;
			cancel = true;
		} else if (mPassword.length() < 4) {
			mPasswordET.setError(getString(R.string.error_invalid_password));
			focusView = mPasswordET;
			cancel = true;
		}

		// Check for a valid email address.
		if (TextUtils.isEmpty(mAccount)) {
			mAccountET.setError(getString(R.string.error_email_required));
			focusView = mAccountET;
			cancel = true;
		}

		if (cancel) {
			focusView.requestFocus();
		} else {
			mLoginStatusMessageView.setText(R.string.login_progress_signing_in);
			showProgress(true);
			mLoginTask = new UserLoginTask();
			mLoginTask.execute((Void) null);
		}
	}

	/**
	 * Shows the progress UI and hides the login form.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	private void showProgress(final boolean show) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			int shortAnimTime = getResources().getInteger(
					android.R.integer.config_shortAnimTime);

			mLoginStatusView.setVisibility(View.VISIBLE);
			mLoginStatusView.animate().setDuration(shortAnimTime)
					.alpha(show ? 1 : 0)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mLoginStatusView.setVisibility(show ? View.VISIBLE
									: View.GONE);
						}
					});

			mLoginFormView.setVisibility(View.VISIBLE);
			mLoginFormView.animate().setDuration(shortAnimTime)
					.alpha(show ? 0 : 1)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mLoginFormView.setVisibility(show ? View.GONE
									: View.VISIBLE);
						}
					});
		} else {
			mLoginStatusView.setVisibility(show ? View.VISIBLE : View.GONE);
			mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
		}
	}

	/**
	 * 用户注册Task
	 * 
	 */
	public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {
		@Override
		protected Boolean doInBackground(Void... params) {
			try{
				String email = mAccountET.getText().toString();
				String passwd = mPasswordET.getText().toString();
				return clientLogin(email,passwd);
			}catch(Exception e){
				e.printStackTrace();
			}
			return false;
		}

		@Override
		protected void onPostExecute(final Boolean success) {
			mLoginTask = null;
			showProgress(false);

			if (success) {
				mFirstLogin = true;
				if(mFirstLogin){
					mFirstLogin = false;
					String userinfo = mAccountET.getText().toString();
			        String password = mPasswordET.getText().toString();
			        writeData(userinfo, password, mKeepUserinfo.isChecked(), mKeepPassword.isChecked());
					Intent intent = new Intent(LoginActivity.this,WorkAttendanceActivity.class);
					Bundle bundle = new Bundle();
					bundle.putInt("attendancetype", ATTENDANCE_TYPE_START);
					intent.putExtras(bundle);
					startActivity(intent);
					finish();
				}else{
					String userinfo = mAccountET.getText().toString();
			        String password = mPasswordET.getText().toString();
			        writeData(userinfo, password, mKeepUserinfo.isChecked(), mKeepPassword.isChecked());
					Intent intent = new Intent(LoginActivity.this,MainActivity.class);
					Bundle bundle = new Bundle();
					bundle.putInt("attendancetype", ATTENDANCE_TYPE_START);
					intent.putExtras(bundle);
					startActivity(intent);
					finish();
				}
			} else {
				if(mErrorType == ERROR_TYPE_EMAIL){
					mAccountET.setError(getString(R.string.error_incorrect_email));
					mAccountET.requestFocus();
				}else if(mErrorType == ERROR_TYPE_PASSWD){
					mPasswordET
					.setError(getString(R.string.error_incorrect_password));
					mPasswordET.requestFocus();
				}
			}
			mErrorType=ERROR_TYPE_NO_ERROR;
		}

		@Override
		protected void onCancelled() {
			mLoginTask = null;
			showProgress(false);
		}
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
	 * Add for request login's state
	 * */
	public boolean clientLogin(String account, String pwd)throws ParseException, IOException, JSONException{
		  HttpClient httpClient = new DefaultHttpClient();
		  httpClient.getParams().setIntParameter(  
                  HttpConnectionParams.SO_TIMEOUT, 5000); // 请求超时设置,"0"代表永不超时  
		  httpClient.getParams().setIntParameter(  
                  HttpConnectionParams.CONNECTION_TIMEOUT, 5000);// 连接超时设置,"0"代表永不超时
		  String strurl = "http://" + 	this.getString(R.string.ip) + "/itspark/collector/login/login";
		  Log.e(LOG_TAG,"clientLogin-> url is " + strurl);
		  HttpPost request = new HttpPost(strurl);
		  request.addHeader("Accept","application/json");
		  //request.setHeader("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
		  request.setHeader("Content-Type", "application/json; charset=utf-8");
		  JSONObject param = new JSONObject();
		  LoginInfo info=new LoginInfo();
		  CommonRequestHeader header = new CommonRequestHeader();
		  header.addRequestHeader(CommonRequestHeader.REQUEST_COLLECTOR_LOGIN_CODE, account, readToken());
		  info.setHeader(header);
		  info.setVersion(getVersion());
		  info.setPassword(getMD5Code(pwd));
		  String androidID = Secure.getString(this.getContentResolver(), Secure.ANDROID_ID);
		  info.setAndroidID(androidID);
		  mPublicKey = generateRSAKeyPair(1024).getPublic().toString();
		  mPrivateKey = generateRSAKeyPair(1024).getPrivate().toString();
//		  param.put("publicKey", mPublicKey);
		  StringEntity se = new StringEntity(JacksonJsonUtil.beanToJson(info), "UTF-8");
		  Log.e(LOG_TAG,"clientLogin-> param is " + JacksonJsonUtil.beanToJson(info));
		  request.setEntity( se);//发送数据
		  try{
			  HttpResponse httpResponse = httpClient.execute(request);//获得响应
			  int code = httpResponse.getStatusLine().getStatusCode();
			  if(code==HttpStatus.SC_OK){
				  String strResult = EntityUtils.toString(httpResponse.getEntity());
				  Log.e(LOG_TAG,"clientLogin-> strResult is " + strResult);
				  CommonResponse res = new CommonResponse(strResult);
				  String resCode = res.getResCode();
				  if(resCode.equals("100") || resCode.equals("101")){
					  if(resCode.equals("100")){
						  mFirstLogin = true;
					  }
					  writeCollector(account);
					  writeToken((String)res.getPropertyMap().get("token"));
					  mErrorType = ERROR_TYPE_NO_ERROR;
					  return true;
				  }else{
					  return false;
				  }
			  }else{
				  Log.e(LOG_TAG, "clientLogin->error code is " + Integer.toString(code));
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
	 * 封装Toast
	 * */
	 private void toastWrapper(final String str) {
	      runOnUiThread(new Runnable() {
	          @Override
	           public void run() {
	               Toast.makeText(LoginActivity.this, str, Toast.LENGTH_SHORT).show();
	           }
	      });
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
	     * 随机生成RSA密钥对 
	     *  
	     * @param keyLength 
	     *            密钥长度，范围：512～2048<br> 
	     *            一般1024 
	     * @return 
	     */  
	    public static KeyPair generateRSAKeyPair(int keyLength)  
	    {  
	        KeyPair keyPair = RSAUtils.generateRSAKeyPair(keyLength);
			return keyPair;  
	    }  
	    
	    /**
	     * 获取版本号
	     * @return 当前应用的版本号
	     */
	    public String getVersion() {
	        try {
	            PackageManager manager = this.getPackageManager();
	            PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
	            String version = info.versionName;
	            return this.getString(R.string.app_name) + version;
	        } catch (Exception e) {
	            e.printStackTrace();
	            return this.getString(R.string.can_not_find_version_name);
	        }
	    }
	    
	    /**
	     * 获取token
	     */
	    private String readToken() {
	        SharedPreferences pref = getSharedPreferences(FILE_NAME_TOKEN, MODE_MULTI_PROCESS);
	        String str = pref.getString("token", "");
	        return str;
	    }
	    
	    /**
	     * 记录账户信息
	     */
	    private boolean writeCollector(String account) {
	        SharedPreferences.Editor share_edit = getSharedPreferences(FILE_NAME_COLLECTOR,
	                MODE_MULTI_PROCESS).edit();
	        share_edit.putString("collectorNumber", account);
	        share_edit.commit();
	        return true;
	    }
}
