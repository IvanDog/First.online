package com.example.parking;

import com.example.parking.ParkingInformationActivity.TimeThread;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.DateFormat;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class TestLeavingActivity extends Activity {
	private final int EVENT_DISPLAY_TIME = 101;
	private TextView mLicensePlateNumberTV;
	private TextView mCarTypeTV;
	private TextView mParkingTypeTV;
	private TextView mLocationNumberTV;
	private TextView mStartTimeTV;
	private TextView mLeaveTimeTV;
	private TextView mExpenseTV;
	private Button mOkButton;
	private Button mCancelButton;
	private DBAdapter mDBAdapter;
	private int mLocationNumber;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_leaving);
		mLicensePlateNumberTV = (TextView) findViewById(R.id.licensePlateNumberTV);
		mCarTypeTV=(TextView) findViewById(R.id.carTypeTV);
		mParkingTypeTV=(TextView) findViewById(R.id.parkingTypeTV);
		mLocationNumberTV=(TextView) findViewById(R.id.locationNumberTV);
		mStartTimeTV=(TextView) findViewById(R.id.startTimeTV);
		mLeaveTimeTV=(TextView) findViewById(R.id.leaveTimeTV);
		mExpenseTV=(TextView) findViewById(R.id.expenseTV);
		mDBAdapter = new DBAdapter(this);
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		mLocationNumber = bundle.getInt("locationNumber");
		new SQLThread().start();
	}

	public class SQLThread extends Thread {
        @Override
        public void run () {
        	mDBAdapter.open();
        	Cursor cursor = mDBAdapter.getParking(mLocationNumber);
            try {
           		         String licensePlate=cursor.getString(cursor.getColumnIndex("licenseplate"));
           		         mLicensePlateNumberTV.setText("车牌号:" + licensePlate);
           		         String carType=cursor.getString(cursor.getColumnIndex("cartype"));
           		         mCarTypeTV.setText("汽车类型:" + carType);
           		         String parkingType=cursor.getString(cursor.getColumnIndex("parkingtype"));
           		         mParkingTypeTV.setText("停车类型:" + parkingType);
           		         String locatioNumber=cursor.getString(cursor.getColumnIndex("locationnumber"));
           		         mLocationNumberTV.setText("车位号:" + locatioNumber);
           		         String startTime=cursor.getString(cursor.getColumnIndex("starttime"));
           		         mStartTimeTV.setText("入场时间:" + startTime);
           		         new TimeThread().start();
            }
            catch (Exception e) {
                    e.printStackTrace();
            } finally{
                	if(cursor!=null){
                		cursor.close();
                    }
            }
        }
    }

	public class TimeThread extends Thread {
        @Override
        public void run () {
            do {
                try {
                    Thread.sleep(1000);
                    Message msg = new Message();
                    msg.what = EVENT_DISPLAY_TIME;
                    mHandler.sendMessage(msg);
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
                case EVENT_DISPLAY_TIME:
                    CharSequence sysTimeStr = DateFormat.format("yyyy-MM-dd HH:mm:ss", System.currentTimeMillis());
                    mLeaveTimeTV.setText("离场时间：" + sysTimeStr);
                    break;
                default:
                    break;
            }
        }
    };
}
