package com.example.parking.view;

import java.util.ArrayList;
import java.util.HashMap;

import com.example.parking.R;
import com.example.parking.R.id;
import com.example.parking.R.layout;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import android.os.Bundle;

import android.view.MenuItem;
import android.view.View;

import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;


public class UnfinishedParkingRecordActivity extends Activity {
	public static String LOG_TAG = "UnfinishedParkingRecordActivity";
	private ListView mListView;
    private ArrayList<HashMap<String, Object>> mList = new ArrayList<HashMap<String, Object>>();
    private UnfinishedParkingRecordAdapter mUnfinishedParkingRecordAdapter;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_unfinished_record);
	    Intent intent = getIntent();  
	    Bundle bundle = intent.getExtras();
	    mList = (ArrayList<HashMap<String, Object>>)bundle.getSerializable("list");  
        mListView=(ListView)findViewById(R.id.list_unfinished_parking_detail);  
        mListView.setOnItemClickListener(new OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
            	HashMap<String,Object> map=(HashMap<String,Object>)mListView.getItemAtPosition(arg2);
            	Intent intent = new Intent(UnfinishedParkingRecordActivity.this,LeavingActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("licensePlateNumber", (String)map.get("licensePlateNumber"));
				bundle.putString("startTime", (String)map.get("startTime"));
				bundle.putString("leaveTime", (String)map.get("leaveTime"));
				bundle.putString("parkNumber", (String)map.get("parkNumber"));
				bundle.putString("expense", (String)map.get("expense"));
				bundle.putString("carType", (String)map.get("carType"));
				bundle.putString("parkingRecordID", (String)map.get("parkingRecordID"));
				intent.putExtras(bundle);
				startActivity(intent);
            }
        });
        mUnfinishedParkingRecordAdapter= new UnfinishedParkingRecordAdapter(getApplicationContext(), mList);
        mListView.setAdapter(mUnfinishedParkingRecordAdapter); 
        mUnfinishedParkingRecordAdapter.notifyDataSetChanged();
        
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

}

