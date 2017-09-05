package com.example.parking;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

public class HistoryRecordFragment extends Fragment {
	public static final int TYPE_FINISHED_PAYMENT_STATE_MOBILE_WECHAT = 101;
	public static final int TYPE_FINISHED_PAYMENT_STATE_MOBILE_ALI = 102;
	public static final int TYPE_FINISHED_PAYMENT_STATE_CASH = 103;
	public static final int TYPE_FINISHED_PAYMENT_STATE_ACCOUNT = 104;
	public static final int TYPE_UNFINISHED_PAYMENT_STATE_LEAVE = 105;
	public static final int NO_HISTORY_PARKING_RECORD =201;
	public static final int EVENT_DISPLAY_QUERY_RESULT = 202;
	public static final int EVENT_DISPLAY_REQUEST_TIMEOUT = 203;
	public static final int EVENT_DISPLAY_CONNECT_TIMEOUT = 204;
	public static final int EVENT_SET_ADAPTER = 205;
	public static String LOG_TAG = "HistoryRecordFragment";
	private View mView;
	private ListView mListView;
	private TextView mEmptyNotifyTV;
	private int mType;
	private String mDate;
	private DBAdapter mDBAdapter;
	private String mParkingNumber = "P1234";
    private UserQueryTask mQueryTask = null;
    private ArrayList<HashMap<String, Object>> mList = new ArrayList<HashMap<String, Object>>();
    private ProgressDialog mProgressDialog = null;
    private HistoryRecordListAdapter mHistoryRecordListAdapter;
	public HistoryRecordFragment(int type,String date){
		mType = type;
		mDate = date;
	}
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
	    	mView = inflater.inflate(R.layout.fragment_record_history, container, false);
	        mListView=(ListView)mView.findViewById(R.id.list_record_history);  
	        mEmptyNotifyTV=(TextView)mView.findViewById(R.id.tv_notify_history_record_list_empty);  
	        //mDBAdapter = new DBAdapter(getActivity());
	        //List<Map<String, Object>> list=getData();  
	        //mListView.setAdapter(new HistoryRecordListAdapter(getActivity(), mList)); 
			mQueryTask = new UserQueryTask();
			mQueryTask.execute((Void) null);
			Log.e("clientHistoryQuery","mQueryTask execute");  
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
	    
		private Handler mHandler = new Handler() {
		    @Override
		    public void handleMessage (Message msg) {
		        super.handleMessage(msg);
		        switch (msg.what) {
		            case NO_HISTORY_PARKING_RECORD:
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
		            	mHistoryRecordListAdapter= new HistoryRecordListAdapter(getActivity(), mList);
		    	        mListView.setAdapter(mHistoryRecordListAdapter); 
		    	        mHistoryRecordListAdapter.notifyDataSetChanged();
		            	break;
		            default:
		                break;
		        }
		    }
		};
		
		/**
		 * Add for request query history and receive result
		 * */
		public boolean clientQuery(String date, String paymentPattern) throws ParseException, IOException, JSONException{
			Log.e("clientHistoryQuery","enter clientQuery");  
			HttpClient httpClient = new DefaultHttpClient();
			  httpClient.getParams().setIntParameter(  
	                  HttpConnectionParams.SO_TIMEOUT, 5000); // 请求超时设置,"0"代表永不超时  
			  httpClient.getParams().setIntParameter(  
	                  HttpConnectionParams.CONNECTION_TIMEOUT, 5000);// 连接超时设置 
			  String strurl = "http://" + this.getString(R.string.ip) + ":8080/itspark/collector/queryHistory/query";
			  HttpPost request = new HttpPost(strurl);
			  request.addHeader("Accept","application/json");
			//request.setHeader("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
			  request.setHeader("Content-Type", "application/json; charset=utf-8");
			  JSONObject param = new JSONObject();
			  HistoryRecordSearchInfo info = new HistoryRecordSearchInfo(); 
			  CommonRequestHeader header = new CommonRequestHeader();
			  header.addRequestHeader(CommonRequestHeader.REQUEST_COLLECTOR_QUERY_HISTORY_PARKING_RECORD_CODE,
					  ((ParkingHistorySearchActivity)getActivity()).readAccount(), ((ParkingHistorySearchActivity)getActivity()).readToken());
			  info.setHeader(header);
			  info.setParkNumber(((ParkingHistorySearchActivity)getActivity()).readCollector("parkNumber"));
			  info.setPaymentPattern(convertPaymentPattern(paymentPattern));
			  info.setDate(date);
			  StringEntity se = new StringEntity(JacksonJsonUtil.beanToJson(info), "UTF-8");
			  Log.e(LOG_TAG,"clientHistoryQuery-> param is " + JacksonJsonUtil.beanToJson(info));
			  request.setEntity(se);//发送数据
			  
			  try{
				  HttpResponse httpResponse = httpClient.execute(request);//获得响应
				  int code = httpResponse.getStatusLine().getStatusCode();
				  if(code==HttpStatus.SC_OK){
					  String strResult = EntityUtils.toString(httpResponse.getEntity());
					  Log.e(LOG_TAG,"clientHistoryQuery->strResult is " + strResult);
					  CommonResponse res = new CommonResponse(strResult);
					  Message msg = new Message();
  		              msg.what=EVENT_DISPLAY_QUERY_RESULT;
  		              msg.obj= res.getResMsg();
  		              mHandler.sendMessage(msg);
					  if(res.getResCode().equals("100")){
						  mList = res.getDataList();
						  return true;
					  }else{
				          return false;
					  } 
				}else{
				    Log.e(LOG_TAG, "clientHistoryQuery->error code is " + Integer.toString(code));
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
		 * 历史记录查询Task
		 * 
		 */
		public class UserQueryTask extends AsyncTask<Void, Void, Boolean> {
			@Override
			protected Boolean doInBackground(Void... params) {
				try{
					Log.e("clientQuery","UserQueryTask doInBackground");  
					String paymentState = null;
			    	if(mType==TYPE_FINISHED_PAYMENT_STATE_MOBILE_WECHAT){
			    		paymentState = "微信支付";
			    	}else if(mType==TYPE_FINISHED_PAYMENT_STATE_MOBILE_ALI){
			    		paymentState = "支付宝支付";
			    	}else if(mType==TYPE_FINISHED_PAYMENT_STATE_CASH){
			    		paymentState = "现金支付";
			    	}else if(mType==TYPE_FINISHED_PAYMENT_STATE_ACCOUNT){
			    		paymentState = "余额支付";
			    	}else if(mType==TYPE_UNFINISHED_PAYMENT_STATE_LEAVE){
			    		paymentState = "逃费";
			    	}
					Log.e(LOG_TAG,"UserQueryTask->payment is " + paymentState);  
					return clientQuery(mDate, paymentState);
				}catch(Exception e){
					Log.e(LOG_TAG,"UserQueryTask->exists exception ");  
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
				          msg.what=NO_HISTORY_PARKING_RECORD;
				          mHandler.sendMessage(msg);
				   }	
				}
			}

			@Override
			protected void onCancelled() {
				mQueryTask = null;
			}
			
		}
		 
		/**
		 * 显示进度框
		 */
		private void showProgressDialog() {
			if (mProgressDialog == null)
				mProgressDialog = new ProgressDialog(getActivity());
			    mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			    mProgressDialog.setIndeterminate(false);
			    mProgressDialog.setCancelable(true);
			    mProgressDialog.setMessage("正在查询");
			    mProgressDialog.show();
		    }

		/**
		 * 隐藏进度框
		 */
		private void dismissProgressDialog() {
			if (mProgressDialog != null) {
				mProgressDialog.dismiss();
			}
		}
		
		public Integer convertPaymentPattern(String paymentPattern){
			if("现金支付".equals(paymentPattern)){
				return  1;
			}else if("pos机支付".equals(paymentPattern)){
				return 2;
			}else if("微信支付".equals(paymentPattern)){
				return 3;
			}else if("支付宝支付".equals(paymentPattern)){
				return 4;
			}else if("余额支付".equals(paymentPattern)){
				return 5;
			}else{
				return 6;
			}
		}
}
