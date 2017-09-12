package com.example.parking.view;

import com.example.parking.R;
import com.example.parking.R.id;
import com.example.parking.R.layout;

import android.app.ActionBar;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	private static final int LICENSE_ARRIVING_TYPE=101;
	private static final int LICENSE_LEAVING_TYPE=102;
	private static final int ATTENDANCE_TYPE_START=301;
	private static final int ATTENDANCE_TYPE_END=302;
    private static final String FILE_NAME_COLLECTOR = "save_pref_collector";
	private TextView mParkNumberTV;
	private TextView mUserNumberTV;
	private TextView mUserTimeTV;
	private Button mArrivingButton;
	private Button mLeavingButton;
	private Button mMangementButton;
	private Button mQueryButton;
	private Button mWorkAttendanceButton;
	private Button mUserCenterButton;
    private long mExitTime = 0;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mParkNumberTV = (TextView)findViewById(R.id.tv_park_number);
		mParkNumberTV.setText("车场编号:" + readCollector("parkNumber"));
		mUserNumberTV = (TextView)findViewById(R.id.tv_user_number);
		mUserNumberTV.setText("工号:" + readCollector("collectorNumber"));
		mUserTimeTV = (TextView)findViewById(R.id.tv_user_time);
		mUserTimeTV.setText("时间:" + readCollector("workStartTime") + "-" + readCollector("workEndTime"));
		mArrivingButton = (Button) findViewById(R.id.bt_arriving);
		mArrivingButton.setOnClickListener(new Button.OnClickListener(){
			public void onClick(View v){
				Intent intent = new Intent(MainActivity.this,InputLicenseActivity.class);
				Bundle bundle = new Bundle();
				bundle.putInt("type", LICENSE_ARRIVING_TYPE);
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});
		mLeavingButton = (Button) findViewById(R.id.bt_leaving);
		mLeavingButton.setOnClickListener(new Button.OnClickListener(){
			public void onClick(View v){
				Intent intent = new Intent(MainActivity.this,InputLicenseActivity.class);
				Bundle bundle = new Bundle();
				bundle.putInt("type", LICENSE_LEAVING_TYPE);
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});
		mMangementButton = (Button) findViewById(R.id.bt_parking_space);
		mMangementButton.setOnClickListener(new Button.OnClickListener(){
			public void onClick(View v){
				Intent intent = new Intent(MainActivity.this,ParkingSpaceActivity.class);
				startActivity(intent);
			}
		});
		mQueryButton = (Button) findViewById(R.id.bt_history_search);
		mQueryButton.setOnClickListener(new Button.OnClickListener(){
			public void onClick(View v){
				Intent intent = new Intent(MainActivity.this,ParkingHistorySearchActivity.class);
				startActivity(intent);
			}
		});
		mWorkAttendanceButton = (Button) findViewById(R.id.bt_work_attendance);
		mWorkAttendanceButton.setOnClickListener(new Button.OnClickListener(){
			public void onClick(View v){
				Intent intent = new Intent(MainActivity.this,WorkAttendanceActivity.class);
				Bundle bundle = new Bundle();
				bundle.putInt("attendancetype", ATTENDANCE_TYPE_END);
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});
		mUserCenterButton = (Button) findViewById(R.id.bt_user_account);
		mUserCenterButton.setOnClickListener(new Button.OnClickListener(){
			public void onClick(View v){
				Intent intent = new Intent(MainActivity.this,UserCenterActivity.class);
				startActivity(intent);
			}
		});
        IntentFilter filter = new IntentFilter();  
        filter.addAction("ExitApp");  
        filter.addAction("BackMain");  
        registerReceiver(mReceiver, filter); 
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
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){   
            if((System.currentTimeMillis() - mExitTime) > 2000){  
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();                                
                mExitTime = System.currentTimeMillis();   
            } else {
                Intent intentFinsh = new Intent();  
                intentFinsh.setAction("ExitApp");  
                sendBroadcast(intentFinsh); 
                exit();
                System.exit(0);
            }
            return true;   
        }
        return super.onKeyDown(keyCode, event);
    }
    
    public void exit(){
    	Intent startMain = new Intent(Intent.ACTION_MAIN);
    	startMain.addCategory(Intent.CATEGORY_HOME);
    	startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    	startActivity(startMain);
    	android.os.Process.killProcess(android.os.Process.myPid());
    }
    
    private String readCollector(String data) {
        SharedPreferences pref = getSharedPreferences(FILE_NAME_COLLECTOR, MODE_MULTI_PROCESS);
        String str = pref.getString(data, "");
        return str;
    }
}
