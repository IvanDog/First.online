package com.example.parking;

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

/**
 * Activity which displays a login screen to the user, offering registration as
 * well.
 */
public class LoginActivity extends Activity {
	/**
	 * A dummy authentication store containing known user names and passwords.
	 * TODO: remove after connecting to a real authentication system.
	 */
	private static final String[] DUMMY_CREDENTIALS = new String[] {
			"gouyf@ehualu.com:123456"};

	/**
	 * The default email to populate the email field with.
	 */
	public static final String EXTRA_EMAIL = "com.example.android.authenticatordemo.extra.EMAIL";

	private UserLoginTask mLoginTask = null;
    private UserRegisterTask mRegisterTask = null;
	// Values for email and password at the time of the login attempt.
	private String mEmail;
	private String mPassword;

	// UI references.
	private EditText mEmailView;
	private EditText mPasswordView;
	private View mLoginFormView;
	private View mLoginStatusView;
	private TextView mLoginStatusMessageView;
    private CheckBox mKeepUserinfo;
    private CheckBox mKeepPassword;
    private AlertDialog mDialog;
	private EditText mUserNumberDialogET;
	private EditText mPasswdDialogET;
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
	private int mErrorType = ERROR_TYPE_NO_ERROR;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_login);

		// Set up the login form.
		mEmail = getIntent().getStringExtra(EXTRA_EMAIL);
		mEmailView = (EditText) findViewById(R.id.email);
		mEmailView.setText(mEmail);

		mPasswordView = (EditText) findViewById(R.id.password);
		mPasswordView
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
		findViewById(R.id.register_button).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						showRegisterDialog();
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
        	mEmailView.setText(readData("userinfo").toString());
        }
        if (readBoolean("ispassword")) {
        	mKeepPassword.setChecked(true);
            mPasswordView.setText(readData("password").toString());
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
	 * Attempts to sign in or register the account specified by the login form.
	 * If there are form errors (invalid email, missing fields, etc.), the
	 * errors are presented and no actual login attempt is made.
	 */
	public void attemptLogin() {
		if (mLoginTask != null) {
			return;
		}

		// Reset errors.
		mEmailView.setError(null);
		mPasswordView.setError(null);

		// Store values at the time of the login attempt.
		mEmail = mEmailView.getText().toString();
		mPassword = mPasswordView.getText().toString();

		boolean cancel = false;
		View focusView = null;

		// Check for a valid password.
		if (TextUtils.isEmpty(mPassword)) {
			mPasswordView.setError(getString(R.string.error_passwd_required));
			focusView = mPasswordView;
			cancel = true;
		} else if (mPassword.length() < 4) {
			mPasswordView.setError(getString(R.string.error_invalid_password));
			focusView = mPasswordView;
			cancel = true;
		}

		// Check for a valid email address.
		if (TextUtils.isEmpty(mEmail)) {
			mEmailView.setError(getString(R.string.error_email_required));
			focusView = mEmailView;
			cancel = true;
		} /*else if (!mEmail.contains("@")) {
			mEmailView.setError(getString(R.string.error_invalid_email));
			focusView = mEmailView;
			cancel = true;
		}*/

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
			// The ViewPropertyAnimator APIs are not available, so simply show
			// and hide the relevant UI components.
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
				String email = mEmailView.getText().toString();
				String passwd = mPasswordView.getText().toString();
				return clientLogin(email,passwd);
			}catch(Exception e){
				e.printStackTrace();
			}
			return false;
			/*try {
				// Simulate network access.
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				return false;
			}			
			for (String credential : DUMMY_CREDENTIALS) {
				String[] pieces = credential.split(":");
				if (pieces[0].equals(mEmail)) {
					// Account exists, return true if the password matches.
					if(pieces[1].equals(mPassword)){
						return true;
					}else{
						mErrorType = ERROR_TYPE_PASSWD;
					}
				}else{
					mErrorType = ERROR_TYPE_EMAIL;
				}
			}*/
		}

		@Override
		protected void onPostExecute(final Boolean success) {
			mLoginTask = null;
			showProgress(false);

			if (success) {
				if(mFirstLogin){
					mFirstLogin = false;
					String userinfo = mEmailView.getText().toString();
			        String password = mPasswordView.getText().toString();
			        writeData(userinfo, password, mKeepUserinfo.isChecked(), mKeepPassword.isChecked());
					Intent intent = new Intent(LoginActivity.this,WorkAttendanceActivity.class);
					Bundle bundle = new Bundle();
					bundle.putInt("attendancetype", ATTENDANCE_TYPE_START);
					intent.putExtras(bundle);
					startActivity(intent);
					finish();
				}else{
					String userinfo = mEmailView.getText().toString();
			        String password = mPasswordView.getText().toString();
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
					mEmailView.setError(getString(R.string.error_incorrect_email));
					mEmailView.requestFocus();
				}else if(mErrorType == ERROR_TYPE_PASSWD){
					mPasswordView
					.setError(getString(R.string.error_incorrect_password));
					mPasswordView.requestFocus();
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
	public boolean clientLogin(String name, String pwd)throws ParseException, IOException, JSONException{
		  HttpClient httpClient = new DefaultHttpClient();
		  httpClient.getParams().setIntParameter(  
                  HttpConnectionParams.SO_TIMEOUT, 5000); // 请求超时设置,"0"代表永不超时  
		  httpClient.getParams().setIntParameter(  
                  HttpConnectionParams.CONNECTION_TIMEOUT, 5000);// 连接超时设置,"0"代表永不超时
		  //String strurl = "http://" + mIP + ":8080/ServletTest/LoginServlet";
		  String strurl = "http://" + 	this.getString(R.string.ip) + ":8080/park/collector/login/login";
		  HttpPost request = new HttpPost(strurl);
		  request.addHeader("Accept","application/json");
		  request.setHeader("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
		  JSONObject param = new JSONObject();
		  param.put("name", name);
		  param.put("password", getMD5Code(pwd));
		  String androidID = Secure.getString(this.getContentResolver(), Secure.ANDROID_ID);
		  param.put("androidID", androidID);
		  mPublicKey = generateRSAKeyPair(1024).getPublic().toString();
		  mPrivateKey = generateRSAKeyPair(1024).getPrivate().toString();
		  param.put("publicKey", mPublicKey);
		  StringEntity se = new StringEntity(param.toString(), "UTF-8");
		  request.setEntity(se);//发送数据
		  try{
			  HttpResponse httpResponse = httpClient.execute(request);//获得响应
			  int code = httpResponse.getStatusLine().getStatusCode();
			  if(code==HttpStatus.SC_OK){
				  String strResult = EntityUtils.toString(httpResponse.getEntity());
				  Log.e("clientLogin","strResult is " + strResult);
				  CommonResponse res = new CommonResponse(strResult);
				  String resCode = res.getResCode();
				  Log.e("clientLogin","resCode is  " + res.getResCode());
				  Log.e("clientLogin","resMsg is  " + res.getResMsg());
				  if(resCode.equals("100") || resCode.equals("101")){
					  if(resCode.equals("100")){
						  mFirstLogin = true;
					  }
					  writeToken((String)res.getPropertyMap().get("token"));
					  mErrorType = ERROR_TYPE_NO_ERROR;
					  return true;
				  }else if(resCode.equals("201")){
					  mErrorType = ERROR_TYPE_PASSWD;
					  return false;
				  }else if(resCode.equals("202")){
					  mErrorType = ERROR_TYPE_EMAIL;
					  return false;
				  }else{
					  return false;
				  }
			  }else{
				  Log.e("clientLogin", "error code is " + Integer.toString(code));
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
	  /*if(loginResult.equals("ok")){
	  String parkName = (String) result.get("parkname");
	  String parkNumber = (String) result.get("parknumber");
	  String userNumber = (String) result.get("usernumber");
	  String workStartTIme = (String) result.get("workstarttime");
	  String workEndTIme = (String) result.get("workendtime");
      }*/
   
	/**
	 * Add for request register's state
	 * */
	public boolean clientRegister(String teleNumber, String passwd) throws ParseException, IOException, JSONException{
		  HttpClient httpClient = new DefaultHttpClient();
		  httpClient.getParams().setIntParameter(  
                  HttpConnectionParams.SO_TIMEOUT, 5000); // 请求超时设置,"0"代表永不超时  
		  httpClient.getParams().setIntParameter(  
                  HttpConnectionParams.CONNECTION_TIMEOUT, 5000);// 连接超时设置 
		  //String strurl = "http://" + mIP + ":8080/ServletTest/RegisterServlet";
		  String strurl = "http://" + 	this.getString(R.string.ip) + ":8080/park/collector/login/register";
		  HttpPost request = new HttpPost(strurl);
		  request.addHeader("Accept","application/json");
		  request.setHeader("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
		  JSONObject param = new JSONObject();
		  param.put("account", teleNumber);
		  param.put("password", getMD5Code(passwd));
		  StringEntity se = new StringEntity(param.toString(), "UTF-8");
		  request.setEntity(se);//发送数据
		  try{
			  HttpResponse httpResponse = httpClient.execute(request);//获得响应
			  int code = httpResponse.getStatusLine().getStatusCode();
			  if(code==HttpStatus.SC_OK){
				  String strResult = EntityUtils.toString(httpResponse.getEntity());
				  Log.e("clienRegister","strResult is " + strResult);
				  CommonResponse res = new CommonResponse(strResult);
				  Log.e("clienRegister","resCode is  " + res.getResCode());
				  Log.e("clienRegister","resMsg is  " + res.getResMsg());
				  String resCode = res.getResCode();
				  String resMsg = res.getResMsg();
				  toastWrapper(resMsg);
				  if(resCode == "100"){
					  return true;
				  }
			  }else{
				  Log.e("clienRegister", "error code is " + Integer.toString(code));
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
	 
	    public void showRegisterDialog(){
	    	LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
			View view = inflater.inflate(R.layout.dialog_passwd_set, null);
			mUserNumberDialogET = (EditText)view.findViewById(R.id.et_input_tele_number);
			mPasswdDialogET = (EditText)view.findViewById(R.id.et_input_passwd);
			final Button finishRegisterButton=(Button)view.findViewById(R.id.bt_finish_register);
			final AlertDialog.Builder VCdialogBuilder = new AlertDialog.Builder(LoginActivity.this);
			VCdialogBuilder.setView(view); // 自定义dialog
			finishRegisterButton.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View v){
					showProgress(true);
					mRegisterTask = new UserRegisterTask();
					mRegisterTask.execute((Void) null);
				}
			});
			mDialog = VCdialogBuilder.create();
			mDialog.show();
	    }
		
		/**
		 * 用户注册Task
		 * 
		 */
		public class UserRegisterTask extends AsyncTask<Void, Void, Boolean> {
			@Override
			protected Boolean doInBackground(Void... params) {
				try{
					if("".equals(mUserNumberDialogET.getText().toString()) || "".equals(mPasswdDialogET.getText().toString())){
					  toastWrapper("请完整输入");
					}else{
						 clientRegister(mUserNumberDialogET.getText().toString(),mPasswdDialogET.getText().toString());
						 mDialog.dismiss();
					}
				}catch(Exception e){
					e.printStackTrace();
				}
				return false;
			}

			@Override
			protected void onPostExecute(final Boolean success) {
				mRegisterTask = null;
				showProgress(false);
			}

			@Override
			protected void onCancelled() {
				mRegisterTask = null;
				showProgress(false);
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
}
