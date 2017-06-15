package com.example.parking;

import java.io.ByteArrayOutputStream;
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

import com.example.parking.HistoryRecordFragment.UserQueryTask;
import com.example.parking.InputLicenseActivity.LicenseTask;
import com.example.parking.R.drawable;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ParseException;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class ParkingInformationActivity extends Activity {
	private final static int EVENT_DISPLAY_TIME = 101;
	private final static int EVENT_INSERT_SUCCESS = 102;
	private final static int TAKE_PHOTO =201;
	public static final int EVENT_DISPLAY_QUERY_RESULT = 301;
	public static final int EVENT_DISPLAY_REQUEST_TIMEOUT = 302;
	public static final int EVENT_DISPLAY_CONNECT_TIMEOUT = 303;
	private String mLicensePlateNumber;
	private TextView mParkNameTV;
	private TextView mParkNumberTV;
	private Spinner mCarTypeSP;
	private Spinner mParkingTypeSP;
	private Spinner mLocationNumberSP;
	private TextView mLicensePlateNumberTV;
	private TextView mStartTimeTV;
	private Button mOkBT;
	private Button mPhotoBT;
	private TextView mPhotoTitleTV;;
	private ImageView mPhotoFirstIV;
	private ImageView mPhotoSecondIV;
	private Bitmap mPhotoFirst = null;
	private Bitmap mPhotoSecond = null;
	private DBAdapter mDBAdapter;
	private boolean mPermissionState=true;
    private InsertTask  mInsertTask = null;
    private static final String FILE_NAME_COLLECTOR = "save_pref_collector";
    private static final String FILE_NAME_TOKEN = "save_pref_token";
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mDBAdapter = new DBAdapter(this);
		setContentView(R.layout.activity_parking_information);
		mParkNameTV = (TextView) findViewById(R.id.tv_parking_name);
		mParkNumberTV = (TextView) findViewById(R.id.tv_parking_number);
		mParkNumberTV.setText("车场编号:" + readCollector("parkNumber"));
		mParkNameTV.setText(readCollector("parkName"));
		mCarTypeSP = (Spinner) findViewById(R.id.sp_car_type);
		mParkingTypeSP = (Spinner) findViewById(R.id.sp_parking_type);
		mLocationNumberSP = (Spinner) findViewById(R.id.sp_parking_location);
		mLicensePlateNumberTV = (TextView) findViewById(R.id.tv_license_plate_number);
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		mLicensePlateNumber = bundle.getString("licensePlate");
		mLicensePlateNumberTV.setText(mLicensePlateNumber);
		mStartTimeTV=(TextView) findViewById(R.id.tv_start_time_arriving);
		new TimeThread().start();
		mOkBT=(Button) findViewById(R.id.bt_confirm_arriving);
		mOkBT.setOnClickListener(new Button.OnClickListener(){
			@Override
			public void onClick(View v){
	        	 mInsertTask = new InsertTask();
	        	 mInsertTask.execute((Void) null);
			}
		});
		/*mOkBT.setOnClickListener(new InsertOnclickListener(mLicensePlateNumberTV.getText().toString(),
				mCarTypeSP.getSelectedItem().toString(), mParkingTypeSP.getSelectedItem().toString(), Integer.parseInt(mLocationNumberSP.getSelectedItem().toString()),
				DateFormat.format("yyyy-MM-dd HH:mm:ss", System.currentTimeMillis()).toString(), null, null, "未付"));*/
		mPhotoBT=(Button) findViewById(R.id.bt_camera_arriving);
		mPhotoBT.setOnClickListener(new Button.OnClickListener(){
			@Override
			public void onClick(View v){
				if(mPhotoFirst!=null && mPhotoSecond!=null){
					Toast.makeText(getApplicationContext(), "最多可添加两张图片",Toast.LENGTH_SHORT).show();
				}else{
					openTakePhoto();
				}
			}
		});
		mPhotoTitleTV = (TextView)findViewById(R.id.tv_photo_title_arriving);
		mPhotoFirstIV = (ImageView)findViewById(R.id.iv_photo_first_arriving);
		mPhotoFirstIV.setOnClickListener(new Button.OnClickListener(){
			@Override
			public void onClick(View v){
				LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
				View imgEntryView = inflater.inflate(R.layout.dialog_photo_entry, null); // 加载自定义的布局文件
				final AlertDialog dialog = new AlertDialog.Builder(ParkingInformationActivity.this).create();
				ImageView img = (ImageView)imgEntryView.findViewById(R.id.iv_large_image);
				Button deleteBT = (Button)imgEntryView.findViewById(R.id.bt_delete_image);
				img.setImageBitmap(mPhotoFirst);
				dialog.setView(imgEntryView); // 自定义dialog
				dialog.show();
				imgEntryView.setOnClickListener(new OnClickListener() {
				    public void onClick(View paramView) {
				        dialog.cancel();
				    }
			    });
				deleteBT.setOnClickListener(new OnClickListener() {
				    public void onClick(View paramView) {
				    	mPhotoFirst = null;
				    	mPhotoFirstIV.setImageResource(drawable.ic_photo_background_64px);
				        dialog.cancel();
				    }
			    });
			}
        });
		mPhotoSecondIV = (ImageView)findViewById(R.id.iv_photo_second_arriving);
		mPhotoSecondIV.setOnClickListener(new Button.OnClickListener(){
			@Override
			public void onClick(View v){
				LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
				View imgEntryView = inflater.inflate(R.layout.dialog_photo_entry, null); // 加载自定义的布局文件
				final AlertDialog dialog = new AlertDialog.Builder(ParkingInformationActivity.this).create();
				ImageView img = (ImageView)imgEntryView.findViewById(R.id.iv_large_image);
				Button deleteBT = (Button)imgEntryView.findViewById(R.id.bt_delete_image);
				img.setImageBitmap(mPhotoSecond);
				dialog.setView(imgEntryView); // 自定义dialog
				dialog.show();
				imgEntryView.setOnClickListener(new OnClickListener() {
				    public void onClick(View paramView) {
				        dialog.cancel();
				    }
			    });
				deleteBT.setOnClickListener(new OnClickListener() {
				    public void onClick(View paramView) {
				    	mPhotoSecond = null;
				    	mPhotoSecondIV.setImageResource(drawable.ic_photo_background_64px);
				        dialog.cancel();
				    }
			    });
			}
        });
		getActionBar().setDisplayHomeAsUpEnabled(true); 
        IntentFilter filter = new IntentFilter();  
        filter.addAction("ExitApp");  
        registerReceiver(mReceiver, filter); 
	}

	public class TimeThread extends Thread {
        @Override
        public void run () {
            do {
                try {
                    Message msg = new Message();
                    msg.what = EVENT_DISPLAY_TIME;
                    mHandler.sendMessage(msg);
                    Thread.sleep(1000);
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } while(true);
        }
    }

	private Handler mHandler = new Handler() {
        @Override
        public void handleMessage (Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case EVENT_DISPLAY_QUERY_RESULT:
                	Toast.makeText(getApplicationContext(), (String)msg.obj, Toast.LENGTH_SHORT).show();
                	break;
                case EVENT_DISPLAY_TIME:
                    CharSequence sysTimeStr = DateFormat.format("yyyy-MM-dd HH:mm:ss", System.currentTimeMillis());
                    mStartTimeTV.setText("入场时间：" + sysTimeStr);
                    break;
                case EVENT_INSERT_SUCCESS:
                    Intent intentBack = new Intent();
                    intentBack.setAction("BackMain");
                    sendBroadcast(intentBack);
           	        Intent intent = new Intent(ParkingInformationActivity.this,MainActivity.class);
				    startActivity(intent);
				    finish();
                	break;
	            case EVENT_DISPLAY_REQUEST_TIMEOUT:
	            	Toast.makeText(getApplicationContext(), (String)msg.obj, Toast.LENGTH_SHORT).show();
	            	break;
	            case EVENT_DISPLAY_CONNECT_TIMEOUT:
	            	Toast.makeText(getApplicationContext(), (String)msg.obj, Toast.LENGTH_SHORT).show();
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
	
	private void openTakePhoto(){
		 /**
		 * 启动拍照之前先判断sdcard是否可用
		 */
		     String state = Environment.getExternalStorageState(); //拿到sdcard是否可用的状态码
		     if (state.equals(Environment.MEDIA_MOUNTED)){   //如果可用
		          Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
		          startActivityForResult(intent,TAKE_PHOTO);
		     }else {
		          Toast.makeText(ParkingInformationActivity.this,"sdcard不可用",Toast.LENGTH_SHORT).show();
		     }
		}
	
	@Override
	 protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	       super.onActivityResult(requestCode, resultCode, data);
	       if (data!= null) {
	           switch (requestCode) {
               case TAKE_PHOTO: //拍摄图片并选择
               if (data.getData() != null|| data.getExtras() != null){ //防止没有返回结果
                   Uri uri =data.getData();
                   if (uri != null) {
                	   if(mPhotoFirst==null){
	                	   mPhotoFirst =BitmapFactory.decodeFile(uri.getPath()); //拿到图片
                	   }else if(mPhotoSecond==null){
                		   mPhotoSecond =BitmapFactory.decodeFile(uri.getPath()); //拿到图片
                	   }
                   }
                   Bundle bundle =data.getExtras();
                   if (bundle != null){
    	               if (mPhotoFirst == null) {
                    	   mPhotoFirst =(Bitmap) bundle.get("data");
   	                    }else 	if (mPhotoSecond == null) {
   	                    	mPhotoSecond =(Bitmap) bundle.get("data");
	   	                }
                   } 
               }
               if(mPhotoFirst!=null){
	               mPhotoFirstIV.setImageBitmap(mPhotoFirst);
               }
               if(mPhotoSecond!=null){
	               mPhotoSecondIV.setImageBitmap(mPhotoSecond);
               }
               break;
          
	          }
	      }
	   }
	


    public byte[] converImageToByte(Bitmap bitmap) {
	    if(bitmap!=null){
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            return baos.toByteArray();
	    }else{
	    	return null;
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
	 * Add for request parking arriving 
	 * */
	public boolean clientInsert()throws ParseException, IOException, JSONException{
		  HttpClient httpClient = new DefaultHttpClient();
		  httpClient.getParams().setIntParameter(  
                  HttpConnectionParams.SO_TIMEOUT,5000); // 请求超时设置,"0"代表永不超时  
		  httpClient.getParams().setIntParameter(  
                  HttpConnectionParams.CONNECTION_TIMEOUT, 5000);// 连接超时设置,"0"代表永不超时
		  String strurl = "http://" + this.getString(R.string.ip) + ":8080/park/collector/insertArriving/insert";
		  HttpPost request = new HttpPost(strurl);
		  request.addHeader("Accept","application/json");
		  request.setHeader("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
		  JSONObject param = new JSONObject();//定义json对象
		  param.put("token", readToken());
		  param.put("parkingNumber", readCollector("parkNumber"));
		  param.put("licensePlateNumber", mLicensePlateNumber);
		  param.put("parkingLocation", Integer.parseInt(String.valueOf(mLocationNumberSP.getSelectedItem().toString())));
		  param.put("carType", mCarTypeSP.getSelectedItem().toString());
		  param.put("parkType", mParkingTypeSP.getSelectedItem().toString());
		  if(mPhotoFirst!=null){
			  param.put("firstPhoto", converImageToByte(mPhotoFirst));
		  }
		  if(mPhotoSecond!=null){
			  param.put("secondPhoto", converImageToByte(mPhotoSecond));
		  }
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
	 * 用户入场Task
	 * 
	 */
	public class InsertTask extends AsyncTask<Void, Void, Boolean> {
		@Override
		protected Boolean doInBackground(Void... params) {
			try{
				Log.e("clientInsert","InsertTask doInBackground");  
				return clientInsert();
			}catch(Exception e){
				Log.e("clientInsert","InsertTask doInBackground EXCEPTION");  
				e.printStackTrace();
			}
			return false;
		}

		@Override
		protected void onPostExecute(final Boolean success) {
			mInsertTask = null;
			 Log.e("clientInsert","InsertTask onPostExecute " + success.toString());  
			if(success){
   		        Message msg = new Message();
                msg.what = EVENT_INSERT_SUCCESS;
                mHandler.sendMessage(msg);
			}
		}

		@Override
		protected void onCancelled() {
			mInsertTask = null;
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
    /*private class InsertOnclickListener implements Button.OnClickListener{
    private String licensePlate;
    private String carType;
    private String parkingType;mPhotoFirst
    private int locationNumber;
    private String startTime;
    private String leaveTime;
    private String expense;
    private String paymentPattern;
    public InsertOnclickListener(String licensePlate, String carType, String parkingType, int locationNumber, 
		      String startTime, String leaveTime, String expense, String paymentPattern){
       this.licensePlate = licensePlate;
       this.carType = carType;
       this.parkingType = parkingType;
       this.locationNumber = locationNumber;
       this.startTime = startTime;
       this.leaveTime = leaveTime;
       this.expense = expense;
       this.paymentPattern = paymentPattern;
    }
    @Override
    public void onClick(View v){
	        mDBAdapter.open();
	        Cursor cursor = mDBAdapter.getParkingByLocationNumber( Integer.parseInt(mLocationNumberSP.getSelectedItem().toString()));
	        try {
   	    cursor.moveToFirst();
   	    mPermissionState=true;
   	    if(cursor.getString(cursor.getColumnIndex("paymentpattern")).equals("未付")){
  		        Message msg = new Message();
              msg.what = EVENT_DUPLICATED_LOCATION_NUMBER;
              mHandler.sendMessage(msg);
              mPermissionState=false;
              Log.e("yifan","p1 = " + mPermissionState);
   	   }
        }catch (Exception e) {
               e.printStackTrace();
        } finally{
       	if(cursor!=null){
       		cursor.close();
           }
       }
	        Log.e("yifan","p3 = " + mPermissionState);
	        if(mPermissionState){
   	    long  result = mDBAdapter.insertParking(licensePlate,mCarTypeSP.getSelectedItem().toString(),mParkingTypeSP.getSelectedItem().toString(),
   			 Integer.parseInt(mLocationNumberSP.getSelectedItem().toString()),startTime,leaveTime,expense,paymentPattern,converImageToByte(mPhoto));
  	        if (result != -1){//插入成功
  		        Message msg = new Message();
               msg.what = EVENT_INSERT_SUCCESS;
               mHandler.sendMessage(msg);
               Intent intentBack = new Intent();
               intentBack.setAction("BackMain");
               sendBroadcast(intentBack);
      	        Intent intent = new Intent(ParkingInformationActivity.this,MainActivity.class);
			    startActivity(intent);
			    finish();
           }
  	        mDBAdapter.close(); 
	         }
   }
}*/
}

