package com.example.parking.view;

import com.example.parking.R;
import com.example.parking.R.id;
import com.example.parking.R.layout;
import com.example.parking.R.string;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MobilePaymentSuccessActivity extends Activity {
	private static final int EVENT_PAYMENT_SUCCESS= 101;
	private static final int PAYMENT_TYPE_CASH=201;
	private static final int PAYMENT_TYPE_ALIPAY=202;
	private static final int PAYMENT_TYPE_WECHATPAY=203;
	private static final int PAYMENT_TYPE_MOBILE=204;
	private TextView mPaymentSuccessNotifyTV;
	private Button mPrintPreviewBT;
	private Button mPaymentSuccessBT;
    private String mLicensePlateNumber;
	//private int mLocationNumber;
	private String mCarType;
	private String mParkType;
	private String mStartTime;
	private String mLeaveTime;
	private String mExpense;
    private String mFeeScale;
	@Override  
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);  
        setContentView(R.layout.activity_payment_success);
    	Intent intent = getIntent();
    	mLicensePlateNumber=intent.getExtras().getString("licenseplate");
 		//mLocationNumber = intent.getExtras().getInt("locationnumber");
 		mCarType =  intent.getExtras().getString("cartype");
 		mParkType = intent.getExtras().getString("parktype");
 		mStartTime = intent.getExtras().getString("starttime");
 		mLeaveTime = intent.getExtras().getString("leavetime");
		mExpense=intent.getExtras().getString("expense");
        mFeeScale = intent.getExtras().getString("feescale");
 		mPaymentSuccessNotifyTV=(TextView)findViewById(R.id.tv_payment_success_notify);
 		mPaymentSuccessNotifyTV.setText(this.getString(R.string.payment_success_notify_fixed) + mExpense);
        mPrintPreviewBT=(Button)findViewById(R.id.bt_print_preview_mobile_payment_success);
        mPaymentSuccessBT=(Button)findViewById(R.id.bt_finish_payment_success);
        mPrintPreviewBT.setOnClickListener(new OnClickListener(){
        	@Override
        	public void onClick(View v){
        		Intent intent = new Intent(MobilePaymentSuccessActivity.this,PrintPreviewActivity.class);
        		Bundle bundle = new Bundle();
        		bundle.putInt("paytype", PAYMENT_TYPE_MOBILE);
        		bundle.putString("licenseplate", mLicensePlateNumber);
        		//bundle.putInt("locationnumber", mLocationNumber);
        		bundle.putString("cartype", mCarType);
        		bundle.putString("parktype", mParkType);
        		bundle.putString("starttime", mStartTime);
        		bundle.putString("leavetime", mLeaveTime);
        		bundle.putString("expense", mExpense);
                bundle.putString("expense", mExpense);
                bundle.putString("feescale", mFeeScale);
        		intent.putExtras(bundle);
        		startActivity(intent);
        	}
        });
        mPaymentSuccessBT.setOnClickListener(new OnClickListener(){
        	@Override
        	public void onClick(View v){
				Message msg = new Message();
                msg.what = EVENT_PAYMENT_SUCCESS;
                mHandler.sendMessage(msg);
                Intent intentBack = new Intent();
                intentBack.setAction("BackMain");
                sendBroadcast(intentBack);
        		Intent intent = new Intent(MobilePaymentSuccessActivity.this,MainActivity.class);
        		startActivity(intent);
        		finish();
        	}
        });
        getActionBar().setDisplayHomeAsUpEnabled(true);
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
                case EVENT_PAYMENT_SUCCESS:
                	Toast.makeText(getApplicationContext(), "收款成功", Toast.LENGTH_SHORT).show();
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
