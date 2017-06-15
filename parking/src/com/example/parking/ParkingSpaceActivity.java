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

import com.example.parking.HistoryRecordFragment.UserQueryTask;

import android.R.color;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.ParseException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ParkingSpaceActivity extends Activity {
	private ProgressBar mProgressBar;
	private ListView mListView;
	private TextView mlicenseplatenumber;
	private String mLicenseNumber;
	private DBAdapter mDBAdapter;
	private TextView mTotalParkingNumberTV;
	private TextView mIdleParkingNumberTV;
	private  static final int MAX_LOCATION_SIZE = 10;
	private int mTotalLocationNumber;
	private int mIdleLocationNumber;
	
	public static final int EVENT_DISPLAY_QUERY_RESULT = 201;
	public static final int EVENT_DISPLAY_REQUEST_TIMEOUT = 202;
	public static final int EVENT_DISPLAY_CONNECT_TIMEOUT = 203;
	public static final int EVENT_SET_ADAPTER = 204;
	
    private static final String FILE_NAME_COLLECTOR = "save_pref_collector";
    private static final String FILE_NAME_TOKEN = "save_pref_token";
    
    private UserQueryTask mQueryTask = null;
    private ArrayList<HashMap<String, Object>> mList = new ArrayList<HashMap<String, Object>>();
    private ParkingPlaceListAdapter mParkingPlaceListAdapter;
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_parking_space_management);
		mDBAdapter = new DBAdapter(this);
		mProgressBar=(ProgressBar) findViewById(R.id.progressBar_horizontal_read_data);
		mTotalParkingNumberTV=(TextView)findViewById(R.id.tv_total_parking_number);
		mIdleParkingNumberTV=(TextView)findViewById(R.id.tv_idle_parking_number);
		mListView=(ListView)findViewById(R.id.list_parking_detail);  
        mlicenseplatenumber=(TextView)findViewById(R.id.tv_licenseplatenumber);
		mQueryTask = new UserQueryTask();
		mQueryTask.execute((Void) null);
        //List<Map<String, Object>> list=getData();  
        //mListView.setAdapter(new ParkingPlaceListAdapter(this, list)); 
        mListView.setOnItemClickListener(new OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                    long arg3) {
            	Map<String,Object> map=(Map<String,Object>)mListView.getItemAtPosition(arg2);
                String licensePlateNumber=(String)map.get("licensePlateNumber");
                int locationNumber=Integer.valueOf(map.get("parkingLocation").toString());
                if(licensePlateNumber.equals("")){
                	Intent intent = new Intent(ParkingSpaceActivity.this,TodayRecordActivity.class);
	        	    Bundle bundle = new Bundle();
	        	    bundle.putString("licensePlateNumber", licensePlateNumber);
	        	    bundle.putInt("locationNumber", locationNumber);
	        	    intent.putExtras(bundle);
	        	    startActivity(intent);
                }else{
                	Intent intent = new Intent(ParkingSpaceActivity.this,ParkingSpaceDetailActivity.class);
	        	    Bundle bundle = new Bundle();
	        	    bundle.putString("licensePlateNumber", licensePlateNumber);
	        	    bundle.putInt("locationNumber", locationNumber);
	        	    intent.putExtras(bundle);
	        	    startActivity(intent);
                }
            }
        });
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
    
    
    /**
	 * Add for request parking state and receive result
	 * */
	public boolean clientQuery() throws ParseException, IOException, JSONException{
		Log.e("clientParkingQuery","enter clientQuery");  
		HttpClient httpClient = new DefaultHttpClient();
		  httpClient.getParams().setIntParameter(  
                  HttpConnectionParams.SO_TIMEOUT, 5000); // 请求超时设置,"0"代表永不超时  
		  httpClient.getParams().setIntParameter(  
                  HttpConnectionParams.CONNECTION_TIMEOUT, 5000);// 连接超时设置 
		  //String strurl = "http://" + mIP + ":8080/ServletTest/QueryParkingSpaceServlet";
		  String strurl = "http://" + this.getString(R.string.ip) + ":8080/park/collector/queryParkingSpace/query";
		  HttpPost request = new HttpPost(strurl);
		  request.addHeader("Accept","application/json");
		  request.setHeader("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
		  JSONObject param = new JSONObject();
		  param.put("token", readToken());
		  param.put("parkingnumber",readCollector("parkNumber"));
		  StringEntity se = new StringEntity(param.toString(), "UTF-8");
		  request.setEntity(se);//发送数据
		  try{
			  HttpResponse httpResponse = httpClient.execute(request);//获得响应
			  int code = httpResponse.getStatusLine().getStatusCode();
			  if(code==HttpStatus.SC_OK){
				  String strResult = EntityUtils.toString(httpResponse.getEntity());
				  Log.e("clientParkingQuery","strResult is " + strResult);
				  CommonResponse res = new CommonResponse(strResult);
				  Log.e("clientParkingQuery","resCode is  " + res.getResCode());
				  Log.e("clientParkingQuery","resMsg is  " + res.getResMsg());
				  Log.e("clientParkingQuery","List is  " + res.getDataList());
				  Log.e("clientParkingQuery","Map is  " + res.getPropertyMap());
				  if(res.getResCode().equals("100")){
					  Log.e("clientParkingQuery","return true");  
					  mList = res.getDataList();
					  mIdleLocationNumber = Integer.parseInt(String.valueOf(res.getPropertyMap().get("idleLocationNumber")));
					  mTotalLocationNumber = Integer.parseInt(String.valueOf(res.getPropertyMap().get("totalLocationNumber")));
					  return true;
				  }else if(res.getResCode().equals("201")){
			          return false;
				  } 
			}else{
					  Log.e("clientParkingQuery", "error code is " + Integer.toString(code));
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
	 * 停车场泊位状态Task 
	 * 
	 */
	public class UserQueryTask extends AsyncTask<Void, Void, Boolean> {
		@Override
		protected Boolean doInBackground(Void... params) {
			try{
				Log.e("clientParkingQuery","UserQueryTask doInBackground");  
				clientQuery();
			}catch(Exception e){
				Log.e("clientParkingQuery","Query exists exception ");  
				e.printStackTrace();
			}
			return false;
		}

		@Override
		protected void onPostExecute(final Boolean success) {
			Log.e("clientParkingQuery","onPostExecute " + success.toString()); 
			mQueryTask = null;
			//if(success){
			    Message msg = new Message();
			    msg.what=EVENT_SET_ADAPTER;
			    mHandler.sendMessage(msg);
			//}
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
	            	mParkingPlaceListAdapter= new ParkingPlaceListAdapter(getApplicationContext(), mList);
	    	        mListView.setAdapter(mParkingPlaceListAdapter); 
	    	        mParkingPlaceListAdapter.notifyDataSetChanged();
	    			mProgressBar.setMax(mTotalLocationNumber);;
	    			mTotalParkingNumberTV.setText("车位总数：" + mTotalLocationNumber);
	    	        mProgressBar.setProgress(mIdleLocationNumber);
	    	        mIdleParkingNumberTV.setText("空闲车位：" + mIdleLocationNumber);
	            	break;
	            default:
	                break;
	        }
	    }
	};

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
    /*public List<Map<String, Object>> getData(){  
    List<Map<String, Object>> list=new ArrayList<Map<String,Object>>();  
    for (int i = 1; i <= MAX_LOCATION_SIZE; i++) {  
        Map<String, Object> map=new HashMap<String, Object>();  
        map.put("parkingNumber",  i+"");
        String licenseNumber = getLicenseNumber(i);
        map.put("licensePlateNumber", licenseNumber);
        if(licenseNumber!=null){
        	map.put("inUseIcon", R.drawable.ic_car_in_parking_24px);
        }
        if(licenseNumber!=null){
        	mIdleLocationNumber--;
        }
        list.add(map);  
    }
    mProgressBar.setProgress(mIdleLocationNumber);
    mIdleParkingNumberTV.setText("空闲车位：" + mIdleLocationNumber);
    return list;  
}*/

/*public String getLicenseNumber(int locationNumber){
	mDBAdapter.open();
	mLicenseNumber=null;
	Cursor cursor = mDBAdapter.getParkingByLocationNumber(locationNumber);
    try {
    	      cursor.moveToFirst();
    	      if(cursor.getString(cursor.getColumnIndex("paymentpattern")).equals("未付")){
  		          mLicenseNumber =  cursor.getString(cursor.getColumnIndex("licenseplate"));
    	      }
    }
    catch (Exception e) {
            e.printStackTrace();
    } finally{
        	if(cursor!=null){
        		cursor.close();
            }
    }
	return mLicenseNumber;
}*/
}
