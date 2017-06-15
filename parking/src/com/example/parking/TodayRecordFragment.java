package com.example.parking;

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

import com.example.parking.TodayRecordActivity.UserQueryTask;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.ParseException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class TodayRecordFragment extends Fragment {
	public static final int NO_TODAY_PARKING_RECORD =101;
	public static final int EVENT_DISPLAY_QUERY_RESULT = 201;
	public static final int EVENT_DISPLAY_REQUEST_TIMEOUT = 202;
	public static final int EVENT_DISPLAY_CONNECT_TIMEOUT = 203;
	public static final int EVENT_SET_ADAPTER = 204;
	private View mView;
	private ListView mListView;
	private TextView mEmptyNotifyTV;
	private int mLocationNumber;
	private DBAdapter mDBAdapter;
    private UserQueryTask mQueryTask = null;
    private ArrayList<HashMap<String, Object>> mList = new ArrayList<HashMap<String, Object>>();
    private TodayRecordListAdapter mTodayRecordListAdapter;
	
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
	    	mView = inflater.inflate(R.layout.fragment_record_today, container, false);
	        mListView=(ListView)mView.findViewById(R.id.list_record_today);  
	        mEmptyNotifyTV=(TextView)mView.findViewById(R.id.tv_notify_today_record_fragment_list_empty);  
	        Intent intent = getActivity().getIntent();
	        mLocationNumber=intent.getExtras().getInt("locationNumber");
			mQueryTask = new UserQueryTask();
			mQueryTask.execute((Void) null);
	        mDBAdapter = new DBAdapter(getActivity());
	        //List<Map<String, Object>> list=getData();  
	        //mListView.setAdapter(new TodayRecordListAdapter(getActivity(), list)); 
	        return mView;
	    }

	    @Override
	    public void onActivityCreated(Bundle savedInstanceState) {
	        super.onActivityCreated(savedInstanceState);
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
			Log.e("clientTodayQuery","enter clientQuery");  
			HttpClient httpClient = new DefaultHttpClient();
			  httpClient.getParams().setIntParameter(  
	                  HttpConnectionParams.SO_TIMEOUT, 5000); // 请求超时设置,"0"代表永不超时  
			  httpClient.getParams().setIntParameter(  
	                  HttpConnectionParams.CONNECTION_TIMEOUT, 5000);// 连接超时设置 
			  //String strurl = "http://" + mIP + ":8080/ServletTest/QueryParkingSpaceServlet";
			  String strurl = "http://" + this.getString(R.string.ip) + ":8080/park/collector/queryToday/query";
			  HttpPost request = new HttpPost(strurl);
			  request.addHeader("Accept","application/json");
			  request.setHeader("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
			  JSONObject param = new JSONObject();
			  param.put("token", ((ParkingSpaceDetailActivity)getActivity()).readToken());
			  param.put("parkingnumber",((ParkingSpaceDetailActivity)getActivity()).readCollector("parkNumber"));
			  param.put("parkinglocation", mLocationNumber);
			  StringEntity se = new StringEntity(param.toString(), "UTF-8");
			  request.setEntity(se);//发送数据
			  try{
				  HttpResponse httpResponse = httpClient.execute(request);//获得响应
				  int code = httpResponse.getStatusLine().getStatusCode();
				  if(code==HttpStatus.SC_OK){
					  String strResult = EntityUtils.toString(httpResponse.getEntity());
					  Log.e("clientTodayQuery","strResult is " + strResult);
					  CommonResponse res = new CommonResponse(strResult);
					  Log.e("clientTodayQuery","resCode is  " + res.getResCode());
					  Log.e("clientTodayQuery","resMsg is  " + res.getResMsg());
					  Log.e("clientTodayQuery","List is  " + res.getDataList());
					  Log.e("clientTodayQuery","Map is  " + res.getPropertyMap());
					  Message msg = new Message();
			          msg.what=EVENT_DISPLAY_QUERY_RESULT;
			          msg.obj= res.getResMsg();
			          mHandler.sendMessage(msg);
					  if(res.getResCode().equals("100")){
						  mList = res.getDataList();
						  return true;
					  }else if(res.getResCode().equals("201")){
				          return false;
					  } 
				}else{
						  Log.e("clientTodayQuery", "error code is " + Integer.toString(code));
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
		 * Represents an asynchronous registration task used to authenticate
		 * the user.
		 */
		public class UserQueryTask extends AsyncTask<Void, Void, Boolean> {
			@Override
			protected Boolean doInBackground(Void... params) {
				try{
					Log.e("clientTodayQuery","UserQueryTask doInBackground");  
					return clientQuery();
				}catch(Exception e){
					Log.e("clientTodayQuery","Query exists exception ");  
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
		
		private Handler mHandler = new Handler() {
		    @Override
		    public void handleMessage (Message msg) {
		        super.handleMessage(msg);
		        switch (msg.what) {
		            case NO_TODAY_PARKING_RECORD:
		            	mEmptyNotifyTV.setVisibility(View.VISIBLE);
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
		            case EVENT_SET_ADAPTER:
		            	mTodayRecordListAdapter= new TodayRecordListAdapter(getActivity(), mList);
		    	        mListView.setAdapter(mTodayRecordListAdapter); 
		    	        mTodayRecordListAdapter.notifyDataSetChanged();
		            	break;
		            default:
		                break;
		        }
		    }
		};
	    /*public List<Map<String, Object>> getData(){  
			SimpleDateFormat formatter = new SimpleDateFormat ("yyyy-MM-dd%"); 
			Date curDate = new Date(System.currentTimeMillis());
			String date = formatter.format(curDate);
	        List<Map<String, Object>> list=new ArrayList<Map<String,Object>>();  
	        setTodayRecord(date,mLocationNumber,list);
	        return list;  
	    }*/

	    /*public void setTodayRecord(String date,int locationNumber,List<Map<String, Object>> list){
	    	mDBAdapter.open();
	    	Cursor cursor = mDBAdapter.getParkingByStartTime(date);
	    	Map<String, Object> titleMap=new HashMap<String, Object>();
	    	titleMap.put("licensePlateNumber","牌照号码");
	    	titleMap.put("startTime","入场时间");
	    	titleMap.put("leaveTime", "离场时间");
	    	titleMap.put("paymentState","支付状态");
	    	titleMap.put("expense", "支付金额");
            list.add(titleMap); 
	    	Log.e("yifan", "count: " + cursor.getCount());
	    	Log.e("yifan", "locationNumber: " + locationNumber);
	        try {
	        	do{
	        	    	  Log.e("yifan", "dblocation: " + cursor.getInt(cursor.getColumnIndex("locationnumber")));
	        	    	  if(cursor.getInt(cursor.getColumnIndex("locationnumber"))==locationNumber ){
	        	    		  Log.e("yifan", "+1" );
	        	    		  Map<String, Object> map=new HashMap<String, Object>();
	        	    		  map.put("licensePlateNumber", cursor.getString(cursor.getColumnIndex("licenseplate")));
	        	    		  map.put("startTime", "入场: " + cursor.getString(cursor.getColumnIndex("starttime")));
	        	    		  if(cursor.getString(cursor.getColumnIndex("leavetime"))==null){
	        	    			  map.put("leaveTime", null);
	        	    		  }else{
		        	    		  map.put("leaveTime", "离场: " + cursor.getString(cursor.getColumnIndex("leavetime")));
	        	    		  }
	        	    		  map.put("paymentState", cursor.getString(cursor.getColumnIndex("paymentpattern")));
	        	    		  map.put("expense", cursor.getString(cursor.getColumnIndex("expense")));
	      		              list.add(map); 
	        	    	  }
	        	      }while(cursor.moveToNext());
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