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
import com.example.parking.TestLeavingActivity.TimeThread;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ParseException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class LeavingActivity extends Activity {
	private static final int EVENT_ESCAPE_RECORD_SUCCESS = 101;
	private static final int EVENT_RECORD_FAIL = 102;
	private static final int EVENT_CASH_RECORD_SUCCESS = 103;
	private static final int PAYMENT_TYPE_CASH=201;
	private static final int PAYMENT_TYPE_ALIPAY=202;
	private static final int PAYMENT_TYPE_WECHATPAY=203;
	private static final int PAYMENT_TYPE_MOBILE=204;
	private static final int EVENT_PAYMENT_FINISHED=205;
	public static final int EVENT_DISPLAY_QUERY_RESULT = 301;
	public static final int EVENT_DISPLAY_REQUEST_TIMEOUT = 302;
	public static final int EVENT_DISPLAY_CONNECT_TIMEOUT = 303;
	public static final int EVENT_DISPLAY_INFORMATION = 401;
    private static final String FILE_NAME_COLLECTOR = "save_pref_collector";
    private static final String FILE_NAME_TOKEN = "save_pref_token";
	//private Button mPrintBT;
	private TextView mUserNumberTV;;
	private TextView mLicensePlateNumberTV;
	private TextView mStartTimeTV;
	private TextView mLeaveTimeTV;
	private TextView mFeeScaleTV;
	private TextView mExpenseTV;
	private long mCurrentRowID;
	private String mLicensePlateNumber;
	private Button mConfirmPaymentBT;
	private Button mEscapeBT;
	private RadioGroup mPaymentTypeRG;
	private RadioButton  mCashPaymentTypeRB;
	private RadioButton mAlipayPaymentTypeRB;
	private RadioButton mWechatpayPaymentRB;;
	private int mPaymentType;
	private DBAdapter mDBAdapter;
	private int mLocationNumber;
	private String mCarType;
	private String mParkType;
	private String mStartTime;
	private String mLeaveTime;
	private String mExpense;
	private String mPaymentPattern;
	private Context mContext;
	private UserQueryTask mQueryTask = null;
	private UserUpdateTask mUpdateTask = null;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_leaving);
		mContext=this;
		mDBAdapter = new DBAdapter(this);
		mUserNumberTV=(TextView)findViewById(R.id.tv_user_number_leaving);
		//mUserNumberTV.setText("工号:" + readCollector("parkNumber"));
		mLicensePlateNumberTV=(TextView)findViewById(R.id.tv_license_number_leaving);
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		mLicensePlateNumber = bundle.getString("licensePlate");
		//mLicensePlateNumberTV.setText("牌照:" + mLicensePlateNumber);
		mStartTimeTV=(TextView)findViewById(R.id.tv_start_time_leaving);
		mLeaveTimeTV=(TextView)findViewById(R.id.tv_leave_time_leaving);
		//mLeaveTimeTV.setText("离场：" + mLeaveTime);
		mFeeScaleTV=(TextView)findViewById(R.id.tv_fee_Scale_leaving);
		mExpenseTV=(TextView)findViewById(R.id.tv_expense_leaving);
		mPaymentTypeRG=(RadioGroup)findViewById(R.id.rg_payment_type_leaving);
		mCashPaymentTypeRB=(RadioButton)findViewById(R.id.rb_cash_payment_leaving);
		mAlipayPaymentTypeRB=(RadioButton)findViewById(R.id.rb_alipay_payment_leaving);
		mAlipayPaymentTypeRB.setEnabled(false);
		mWechatpayPaymentRB=(RadioButton)findViewById(R.id.rb_wechatpay_payment_leaving);
		mWechatpayPaymentRB.setEnabled(false);
		mPaymentTypeRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() { 
			@Override 
			public void onCheckedChanged(RadioGroup group, int checkedId){
			    if (mCashPaymentTypeRB.getId() == checkedId) {
			    	mPaymentType = PAYMENT_TYPE_CASH; 
		        }else if (mAlipayPaymentTypeRB.getId() == checkedId){
		        	mPaymentType = PAYMENT_TYPE_ALIPAY; 
			    }else if (mWechatpayPaymentRB.getId() == checkedId){
		        	mPaymentType = PAYMENT_TYPE_WECHATPAY; 
			    }
			    mConfirmPaymentBT.setEnabled(true);
			  } 
			});
		mConfirmPaymentBT=(Button)findViewById(R.id.bt_confirm_payment_leaving);
		mConfirmPaymentBT.setEnabled(false);
		mConfirmPaymentBT.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				if(mPaymentType==PAYMENT_TYPE_CASH){
					showCashPaymentDialog();
				}else if(mPaymentType==PAYMENT_TYPE_ALIPAY){
					Intent intent = new Intent(LeavingActivity.this,MobilePaymentActivity.class);
					Bundle bundle = new Bundle();
					bundle.putInt("paytype", PAYMENT_TYPE_ALIPAY);
            		bundle.putString("licenseplate", mLicensePlateNumber);
            		bundle.putInt("locationnumber", mLocationNumber);
            		bundle.putString("cartype", mCarType);
            		bundle.putString("parktype", mParkType);
            		bundle.putString("starttime", mStartTime);
            		bundle.putString("leavetime", mLeaveTime);
            		bundle.putString("expense", mExpense);
            		intent.putExtras(bundle);
					intent.putExtras(bundle);
					startActivity(intent);
				}else if(mPaymentType==PAYMENT_TYPE_WECHATPAY){
					Intent intent = new Intent(LeavingActivity.this,MobilePaymentActivity.class);
					Bundle bundle = new Bundle();
					bundle.putInt("paytype", PAYMENT_TYPE_WECHATPAY);
            		bundle.putString("licenseplate", mLicensePlateNumber);
            		bundle.putInt("locationnumber", mLocationNumber);
            		bundle.putString("cartype", mCarType);
            		bundle.putString("parktype", mParkType);
            		bundle.putString("starttime", mStartTime);
            		bundle.putString("leavetime", mLeaveTime);
            		bundle.putString("expense", mExpense);
            		intent.putExtras(bundle);
					intent.putExtras(bundle);
					startActivity(intent);
				}
			}
		});
		mEscapeBT=(Button)findViewById(R.id.bt_cancel_payment);
		mEscapeBT.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				showEscapeDialog();
			}
		});
		mQueryTask = new UserQueryTask();
		mQueryTask.execute((Void) null);
        getActionBar().setDisplayHomeAsUpEnabled(true);
		//new SQLThread().start();
        IntentFilter filter = new IntentFilter();  
        filter.addAction("ExitApp");  
        filter.addAction("BackMain");  
        registerReceiver(mReceiver, filter); 
	}

	private Handler mHandler = new Handler() {
        @Override
        public void handleMessage (Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case EVENT_ESCAPE_RECORD_SUCCESS:
                	Toast.makeText(getApplicationContext(), "逃费行为已记录", Toast.LENGTH_SHORT).show();
                	break;
                case EVENT_CASH_RECORD_SUCCESS:
                	mConfirmPaymentBT.setEnabled(false);
                	mConfirmPaymentBT.setText("已支付");
                	mEscapeBT.setEnabled(false);
                	Toast.makeText(getApplicationContext(), "现金支付成功", Toast.LENGTH_SHORT).show();
                	break;
                case EVENT_RECORD_FAIL:
                	Toast.makeText(getApplicationContext(), "记录失败", Toast.LENGTH_SHORT).show();
                	break;
                case EVENT_PAYMENT_FINISHED:
                	Toast.makeText(getApplicationContext(), "该订单已支付", Toast.LENGTH_SHORT).show();
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
                case EVENT_DISPLAY_INFORMATION:
                	mUserNumberTV.setText("工号:" + readCollector("parkNumber"));
                	mLicensePlateNumberTV.setText("牌照:" + mLicensePlateNumber);
                	mStartTimeTV.setText("入场：" + mStartTime);
                	mLeaveTimeTV.setText("离场：" + mLeaveTime);
            		mFeeScaleTV.setText("收费标准:" + readCollector("feeScale"));
                	mExpenseTV.setText("费用总计:" + mExpense);
                	break;
                default:
                    break;
            }
        }
    };

    private void showCashPaymentDialog(){
        final AlertDialog.Builder cashPaymentDialog = new AlertDialog.Builder(LeavingActivity.this);
        cashPaymentDialog.setIcon(R.drawable.ic_car_leaving);
        cashPaymentDialog.setTitle("现金支付");
        cashPaymentDialog.setMessage("现金支付成功？");
        cashPaymentDialog.setPositiveButton("确定",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            	String paymentPattern = "现金支付";
        		mUpdateTask = new UserUpdateTask(paymentPattern);
        		mUpdateTask.execute((Void) null);
            }
        });
        cashPaymentDialog.setNegativeButton("关闭",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //TODO
            }
        });
        cashPaymentDialog.show();
    }

    private void showEscapeDialog(){
        final AlertDialog.Builder escapeDialog = new AlertDialog.Builder(LeavingActivity.this);
        escapeDialog.setIcon(R.drawable.ic_car_leaving);
        escapeDialog.setTitle("逃费");
        escapeDialog.setMessage("该用户已逃费？");
        escapeDialog.setPositiveButton("确定",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            	String paymentPattern = "逃费";
        		mUpdateTask = new UserUpdateTask(paymentPattern);
        		mUpdateTask.execute((Void) null);
            }
        });
        escapeDialog.setNegativeButton("关闭",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //TODO
            }
        });
        escapeDialog.show();
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
    
    /**
	 * Add for request detail for charge
	 * */
	public boolean clientQueryExpense() throws ParseException, IOException, JSONException{
		Log.e("clientQueryExpense","enter clientQueryExpense");  
		HttpClient httpClient = new DefaultHttpClient();
		  httpClient.getParams().setIntParameter(  
                  HttpConnectionParams.SO_TIMEOUT, 5000); // 请求超时设置,"0"代表永不超时  
		  httpClient.getParams().setIntParameter(  
                  HttpConnectionParams.CONNECTION_TIMEOUT, 5000);// 连接超时设置 
		  String strurl = "http://" + this.getString(R.string.ip) + ":8080/park/collector/leavingInformation/queryExpense";
		  HttpPost request = new HttpPost(strurl);
		  request.addHeader("Accept","application/json");
		  request.setHeader("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
		  JSONObject param = new JSONObject();
		  param.put("token", readToken());
		  param.put("parkNumber",readCollector("parkNumber"));
		  param.put("licensePlateNumber",mLicensePlateNumber);
          CharSequence sysTimeStr = DateFormat.format("yyyy-MM-dd HH:mm:ss", System.currentTimeMillis());
          mLeaveTime = sysTimeStr + "";
          param.put("leaveTime",mLeaveTime);
		  StringEntity se = new StringEntity(param.toString(), "UTF-8");
		  request.setEntity(se);//发送数据
		  try{
			  HttpResponse httpResponse = httpClient.execute(request);//获得响应
			  int code = httpResponse.getStatusLine().getStatusCode();
			  if(code==HttpStatus.SC_OK){
				  String strResult = EntityUtils.toString(httpResponse.getEntity());
				  Log.e("clientQueryExpense","strResult is " + strResult);
				  CommonResponse res = new CommonResponse(strResult);
				  Log.e("clientQueryExpense","resCode is  " + res.getResCode());
				  Log.e("clientQueryExpense","List is  " + res.getDataList());
				  Log.e("clientQueryExpense","Map is  " + res.getPropertyMap());
				  if(res.getResCode().equals("100")){
					  mCarType = (String)res.getPropertyMap().get("carType");
					  mParkType = (String)res.getPropertyMap().get("parkType");
					  mStartTime = (String)res.getPropertyMap().get("startTime");
					  mExpense = (String)res.getPropertyMap().get("expense");
					  return true;
				  }else if(res.getResCode().equals("201")){
			          return false;
				  } 
			}else{
					  Log.e("clientQueryExpense", "error code is " + Integer.toString(code));
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
	 * 查询j结算信息Task
	 * 
	 */
	public class UserQueryTask extends AsyncTask<Void, Void, Boolean> {
		@Override
		protected Boolean doInBackground(Void... params) {
			try{
				Log.e("clientQueryExpense","UserQueryTask doInBackground");  
				return clientQueryExpense();
			}catch(Exception e){
				Log.e("clientQueryExpense","Query exists exception ");  
				e.printStackTrace();
			}
			return false;
		}

		@Override
		protected void onPostExecute(final Boolean success) {
			mQueryTask = null;
		    Log.e("clientQueryExpense","EVENT_DISPLAY_INFORMATION ");  
		    if(success){
			    Message msg = new Message();
			    msg.what=EVENT_DISPLAY_INFORMATION;
			    mHandler.sendMessage(msg);
		    }
		}

		@Override
		protected void onCancelled() {
			mQueryTask = null;
		}
		
	}
	
	 /**
		 * Add for request detail for charge
		 * */
		public boolean clientUpdatePaymentPattern(String paymentPattern) throws ParseException, IOException, JSONException{
			Log.e("clientUpdatePaymentPattern","enter clientUpdatePaymentPattern");  
			HttpClient httpClient = new DefaultHttpClient();
			  httpClient.getParams().setIntParameter(  
	                  HttpConnectionParams.SO_TIMEOUT, 5000); // 请求超时设置,"0"代表永不超时  
			  httpClient.getParams().setIntParameter(  
	                  HttpConnectionParams.CONNECTION_TIMEOUT, 5000);// 连接超时设置 
			  String strurl = "http://" + this.getString(R.string.ip) + ":8080/park/collector/leavingInformation/updatePaymentPattern";
			  HttpPost request = new HttpPost(strurl);
			  request.addHeader("Accept","application/json");
			  request.setHeader("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
			  JSONObject param = new JSONObject();//定义json对象
			  param.put("token", readToken());
			  param.put("parkNumber",readCollector("parkNumber"));
			  param.put("licensePlateNumber",mLicensePlateNumber);
			  param.put("paymentPattern",paymentPattern);
			  StringEntity se = new StringEntity(param.toString(), "UTF-8");
			  request.setEntity(se);
			  try{
				  HttpResponse httpResponse = httpClient.execute(request);//获得响应
				  int code = httpResponse.getStatusLine().getStatusCode();
				  if(code==HttpStatus.SC_OK){
					  String strResult = EntityUtils.toString(httpResponse.getEntity());
					  Log.e("clientUpdatePaymentPattern","strResult is " + strResult);
					  CommonResponse res = new CommonResponse(strResult);
					  Log.e("clientUpdatePaymentPattern","resCode is  " + res.getResCode());
					  Log.e("clientUpdatePaymentPattern","List is  " + res.getDataList());
					  Log.e("clientUpdatePaymentPattern","Map is  " + res.getPropertyMap());
					  if(res.getResCode().equals("100")){
						  mPaymentPattern = paymentPattern;
						  return true;
					  }else if(res.getResCode().equals("201")){
				          return false;
					  } 
				}else{
						  Log.e("clientQueryExpense", "error code is " + Integer.toString(code));
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
		 * 查询j结算信息Task
		 * 
		 */
		public class UserUpdateTask extends AsyncTask<Void, Void, Boolean> {
			private String paymentPattern = null;
			public UserUpdateTask(String paymentPattern){
				this.paymentPattern = paymentPattern;
			}
			@Override
			protected Boolean doInBackground(Void... params) {
				try{
					Log.e("clientUpdatePaymentPattern","UserQueryTask doInBackground");  
					return clientUpdatePaymentPattern(paymentPattern);
				}catch(Exception e){
					Log.e("clientUpdatePaymentPattern","Query exists exception ");  
					e.printStackTrace();
				}
				return false;
			}

			@Override
			protected void onPostExecute(final Boolean success) {
				mUpdateTask = null;
			    Log.e("clientUpdatePaymentPattern","EVENT_DISPLAY_INFORMATION ");  
			    if(mPaymentPattern.equals("现金支付")){
			    	if(success){
	    				Message msg = new Message();
	                    msg.what = EVENT_CASH_RECORD_SUCCESS;
	                    mHandler.sendMessage(msg);
	    				Intent intent = new Intent(LeavingActivity.this,PrintPreviewActivity.class);
	            		Bundle bundle = new Bundle();
	            		bundle.putInt("paytype", PAYMENT_TYPE_CASH);
	            		bundle.putString("licenseplate", mLicensePlateNumber);
	            		bundle.putInt("locationnumber", mLocationNumber);
	            		bundle.putString("cartype", mCarType);
	            		bundle.putString("parktype", mParkType);
	            		bundle.putString("starttime", mStartTime);
	            		bundle.putString("leavetime", mLeaveTime);
	            		bundle.putString("expense", mExpense);
	            		intent.putExtras(bundle);
	    				startActivity(intent);
	            	}else{
	    				Message msg = new Message();
	                    msg.what = EVENT_RECORD_FAIL;
	                    mHandler.sendMessage(msg);
	            	}
			    }else if(mPaymentPattern.equals("逃费")){
	            	if(success){
	    				Message msg = new Message();
	                    msg.what = EVENT_ESCAPE_RECORD_SUCCESS;
	                    mHandler.sendMessage(msg);
	                    Intent intentBack = new Intent();
	                    intentBack.setAction("BackMain");
	                    sendBroadcast(intentBack);
	    				Intent intent = new Intent(LeavingActivity.this,MainActivity.class);
	    				startActivity(intent);
	            	}else{
	    				Message msg = new Message();
	                    msg.what = EVENT_RECORD_FAIL;
	                    mHandler.sendMessage(msg);
	            	}
			    }
			}

			@Override
			protected void onCancelled() {
				mUpdateTask = null;
			}
			
		}
    /*public class SQLThread extends Thread {
    @Override
    public void run () {
    	mDBAdapter.open();
    	Cursor cursor = mDBAdapter.getParkingByLicensePlate(mLicensePlateNumber);
        try {
        	cursor.moveToFirst();
        	//if(cursor.getString(cursor.getColumnIndex("paymentpattern")).equals("未付")){
   	             mCurrentRowID = cursor.getLong(cursor.getColumnIndex("_id"));
  		         String startTime=cursor.getString(cursor.getColumnIndex("starttime"));
  		         mStartTimeTV.setText("入场：" + startTime);
  		         if(cursor.getString(cursor.getColumnIndex("leavetime"))!=null){
      		         String leaveTime=cursor.getString(cursor.getColumnIndex("leavetime"));
      		       mLeaveTimeTV.setText("离场：" + leaveTime);
      		       mLeaveTime = mLeaveTimeTV.getText().toString();
  		         }else{
      		         CharSequence sysTimeStr = DateFormat.format("yyyy-MM-dd HH:mm:ss", System.currentTimeMillis());
      		         mLeaveTimeTV.setText("离场：" + sysTimeStr);
      		         mLeaveTime = sysTimeStr.toString();
  		         }
  		          mLocationNumber =  cursor.getInt(cursor.getColumnIndex("locationnumber"));
  		          mCarType = cursor.getString(cursor.getColumnIndex("cartype"));
  		          mParkType = cursor.getString(cursor.getColumnIndex("parkingtype"));
  		          if(mParkType.equals("免费停车")){
  		        	mExpense=mContext.getString(R.string.free_expense_fixed);
  		    		mExpenseTV.setText("费用总计:" +  mContext.getString(R.string.free_expense_fixed));
  		          }else if(mParkType.equals("普通停车")){
    		        mExpense=mContext.getString(R.string.expense_fixed);
  		        	mExpenseTV.setText("费用总计:" +  mContext.getString(R.string.expense_fixed));
  		          }
  		          mStartTime = cursor.getString(cursor.getColumnIndex("starttime"));
        	//}
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
