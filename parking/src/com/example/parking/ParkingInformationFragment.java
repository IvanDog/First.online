package com.example.parking;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.util.ArrayList;
import java.util.HashMap;

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

import com.example.parking.ParkingSpaceActivity.UserQueryTask;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.ParseException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class ParkingInformationFragment extends Fragment {
	private final int EVENT_DISPLAY_TIME = 101;
	public static final int EVENT_DISPLAY_QUERY_RESULT = 201;
	public static final int EVENT_DISPLAY_REQUEST_TIMEOUT = 202;
	public static final int EVENT_DISPLAY_CONNECT_TIMEOUT = 203;
	public static final int EVENT_DISPLAY_INFORMATION = 204;
	private View mView;
	private TextView mParkNameTV;
	private TextView mParkNumberTV;
	private TextView mLocationNumberTV;
	private TextView mLicenseNumberTV;
	private TextView mCarTypeTV;
	private TextView mParkTypeTV;
	private TextView mStartTimeTV;
	private TextView mLeaveTimeTV;
	private Button mConfirmLeavingBT;
	private Button mCancelLeavingBT;
	private int mLocationNumber;
	private String mCarType;
	private String mParkType;
	private String mStartTime;
	private String mLeaveTime;
	private DBAdapter mDBAdapter;
	private String mLicensePlateNumber;
    private UserQueryTask mQueryTask = null;
	 @Override
	    public void onAttach(Activity activity) {
	        super.onAttach(activity);
	    }

	    @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	    }

	    @Override
	    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	    	mView = inflater.inflate(R.layout.fragment_parking_space_detail, container, false);
	        return mView;
	    }

	    @Override
	    public void onActivityCreated(Bundle savedInstanceState) {
	        super.onActivityCreated(savedInstanceState);
	        mLicensePlateNumber = getActivity().getIntent().getStringExtra("licensePlateNumber");
	        mLocationNumber = getActivity().getIntent().getExtras().getInt("locationNumber");
	        mDBAdapter = new DBAdapter(getActivity());
			mParkNameTV = (TextView)mView.findViewById(R.id.tv_parking_name_parking_detail);
			mParkNumberTV = (TextView) mView.findViewById(R.id.tv_parking_number_parking_detail);
	        mLocationNumberTV=(TextView)mView.findViewById(R.id.tv_location_number_parking_detail);
			mLicenseNumberTV=(TextView)mView.findViewById(R.id.tv_license_plate_number_parking_detail);
			mCarTypeTV=(TextView)mView.findViewById(R.id.tv_car_type_parking_detail);
			mParkTypeTV=(TextView)mView.findViewById(R.id.tv_parking_type_parking_detail);
	        mStartTimeTV=(TextView) mView.findViewById(R.id.tv_start_time_parking_detail);
	        mLeaveTimeTV=(TextView) mView.findViewById(R.id.tv_leave_time_parking_detail);
	        mConfirmLeavingBT=(Button)mView.findViewById(R.id.bt_confirm_leaving);
	        mConfirmLeavingBT.setOnClickListener(new OnClickListener(){
	        	@Override
	        	public void onClick(View v){
					Intent intent = new Intent(getActivity(),LeavingActivity.class);
					Bundle bundle = new Bundle();
					bundle.putString("licensePlate",mLicensePlateNumber );
					intent.putExtras(bundle);
					startActivity(intent);
	        	}
	        });
	        mCancelLeavingBT=(Button)mView.findViewById(R.id.bt_cancel_leaving);
	        mCancelLeavingBT.setOnClickListener(new OnClickListener(){
	        	@Override
	        	public void onClick(View v){
					Intent intent = new Intent(getActivity(),ParkingSpaceActivity.class);
					startActivity(intent);
	        	}
	        });
			mQueryTask = new UserQueryTask();
			mQueryTask.execute((Void) null);
	        //new TimeThread().start();
	        //setParkingInformation();
	    }

	    @Override
	    public void onStart() {
	        super.onStart();
	    }

	    @Override
	    public void onResume() {
	        super.onResume();
	    }

	    @Override
	    public void onPause() {
	        super.onPause();
	    }

	    @Override
	    public void onStop() {
	        super.onStop();
	    }

	    @Override
	    public void onDestroyView() {
	        super.onDestroyView();
	    }

	    @Override
	    public void onDestroy() {
	        super.onDestroy();
	    }

	    @Override
	    public void onDetach() {
	        super.onDetach();
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
	    
	    /**
		 * Add for request parking information
		 * */
		public boolean clientUpdateDisplay() throws ParseException, IOException, JSONException{
			Log.e("clientQuery","enter clientQuery");  
			HttpClient httpClient = new DefaultHttpClient();
			  httpClient.getParams().setIntParameter(  
	                  HttpConnectionParams.SO_TIMEOUT, 5000); // 请求超时设置,"0"代表永不超时  
			  httpClient.getParams().setIntParameter(  
	                  HttpConnectionParams.CONNECTION_TIMEOUT, 5000);// 连接超时设置 
			  //String strurl = "http://" + mIP + ":8080/ServletTest/QueryParkingSpaceServlet";
			  String strurl = "http://" + this.getString(R.string.ip) + ":8080/park/collector/queryCurrentParking/query";
			  HttpPost request = new HttpPost(strurl);
			  request.addHeader("Accept","application/json");
			  request.setHeader("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
			  JSONObject param = new JSONObject();//定义json对象
			  param.put("token", ((ParkingSpaceDetailActivity)getActivity()).readToken());
			  param.put("parkingnumber",((ParkingSpaceDetailActivity)getActivity()).readCollector("parkNumber"));
			  param.put("parkinglocation",mLocationNumber);
			  StringEntity se = new StringEntity(param.toString(), "UTF-8");
			  request.setEntity(se);//发送数据
			  try{
				  HttpResponse httpResponse = httpClient.execute(request);//获得响应
				  int code = httpResponse.getStatusLine().getStatusCode();
				  if(code==HttpStatus.SC_OK){
					  String strResult = EntityUtils.toString(httpResponse.getEntity());
					  Log.e("clientUpdateDisplay","strResult is " + strResult);
					  CommonResponse res = new CommonResponse(strResult);
					  Log.e("clientUpdateDisplay","resCode is  " + res.getResCode());
					  Log.e("clientUpdateDisplay","List is  " + res.getDataList());
					  Log.e("clientUpdateDisplay","Map is  " + res.getPropertyMap());
					  if(res.getResCode().equals("100")){
						  mCarType = (String)res.getPropertyMap().get("carType");
						  mParkType = (String)res.getPropertyMap().get("parkType");
						  mStartTime = (String)res.getPropertyMap().get("startTime");
						  return true;
					  }else if(res.getResCode().equals("201")){
				          return false;
					  } 
				}else{
						  Log.e("yifan", "error code is " + Integer.toString(code));
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
		 * 查询当前泊车信息Task
		 * 
		 */
		public class UserQueryTask extends AsyncTask<Void, Void, Boolean> {
			@Override
			protected Boolean doInBackground(Void... params) {
				try{
					Log.e("clientQuery","UserQueryTask doInBackground");  
					clientUpdateDisplay();
				}catch(Exception e){
					Log.e("clientQuery","Query exists exception ");  
					e.printStackTrace();
				}
				return false;
			}

			@Override
			protected void onPostExecute(final Boolean success) {
				mQueryTask = null;
			    Log.e("clientQuery","EVENT_DISPLAY_INFORMATION ");  
			    Message msg = new Message();
			    msg.what=EVENT_DISPLAY_INFORMATION;
			    mHandler.sendMessage(msg);
			}

			@Override
			protected void onCancelled() {
				mQueryTask = null;
			}
			
		}
		
		private Handler mHandler = new Handler() {
	        @Override
	        public void handleMessage (Message msg) {
	            super.handleMessage(msg);
	            switch (msg.what) {
	                case EVENT_DISPLAY_TIME:
	                    CharSequence sysTimeStr = DateFormat.format("yyyy-MM-dd HH:mm:ss", System.currentTimeMillis());
	                    mLeaveTime = sysTimeStr + "";
	                    mLeaveTimeTV.setText("离场时间：" + sysTimeStr);
	                    break;
	                case EVENT_DISPLAY_INFORMATION:
	        			 mParkNameTV.setText(((ParkingSpaceDetailActivity)getActivity()).readCollector("parkName"));
	        			 mParkNumberTV.setText("车场编号: " + ((ParkingSpaceDetailActivity)getActivity()).readCollector("parkNumber"));
	      				 mLicenseNumberTV.setText("车牌号: " + mLicensePlateNumber);
	      			     mLocationNumberTV.setText("泊位号: " + mLocationNumber);
	     		    	 mCarTypeTV.setText("车辆类型: " + mCarType);
	      		         mParkTypeTV.setText("泊车类型: " + mParkType);
	      		         mStartTimeTV.setText("入场时间: " + mStartTime);
	      			    new TimeThread().start();
	                    break;
	                case EVENT_DISPLAY_QUERY_RESULT:
	                	Toast.makeText(getActivity(), (String)msg.obj, Toast.LENGTH_SHORT).show();
	                	break;
		            case EVENT_DISPLAY_REQUEST_TIMEOUT:
		            	Toast.makeText(getActivity(), (String)msg.obj, Toast.LENGTH_SHORT).show();
		            	break;
		            case EVENT_DISPLAY_CONNECT_TIMEOUT:
		            	Toast.makeText(getActivity(), (String)msg.obj, Toast.LENGTH_SHORT).show();
		            	break;
	                default:
	                    break;
	            }
	        }
	    };
	    
		/*public void setParkingInformation(){
    	mDBAdapter.open();
    	Cursor cursor = mDBAdapter.getParkingByLicensePlate(mLicensePlateNumber);
        try {
        	      cursor.moveToFirst();
  		          mLocationNumber =  cursor.getInt(cursor.getColumnIndex("locationnumber"));
  		          mCarType = cursor.getString(cursor.getColumnIndex("cartype"));
  		          mParkType = cursor.getString(cursor.getColumnIndex("parkingtype"));
  		          mStartTime = cursor.getString(cursor.getColumnIndex("starttime"));
  				  mLicenseNumberTV.setText("车牌号: " + mLicensePlateNumber);
  				  mLocationNumberTV.setText("泊位号: " + mLocationNumber);
  		    	  mCarTypeTV.setText("车辆类型: " + mCarType);
  		          mParkTypeTV.setText("泊车类型: " + mParkType);
  		          mStartTimeTV.setText("入场时间: " + mStartTime);
        }
        catch (Exception e) {
                e.printStackTrace();
        } finally{
            	if(cursor!=null){
            		cursor.close();
                }
        }
	}*/
}
