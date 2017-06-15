package com.example.parking;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class MobilePaymentFragment extends Fragment {
	    private static final int PAYMENT_TYPE_ALIPAY=202;
	    private static final int PAYMENT_TYPE_WECHATPAY=203;
	    private View mView;
	    private View mFragmentView;
	    private ImageView mPaymentIV;
 	    private int mType;
 		private DBAdapter mDBAdapter;
 		private long mCurrentRowID;
 		private String mLicensePlateNumber;
 		private int mLocationNumber;
 		private String mCarType;
 		private String mParkType;
 		private String mStartTime;
 		private String mLeaveTime;
 		private String mExpense;
 	    public MobilePaymentFragment(int type){
 	    	mType = type;
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
	    	mView = inflater.inflate(R.layout.fragment_mobile_payment, container, false);
	    	Intent intent = getActivity().getIntent();
	    	mLicensePlateNumber=intent.getExtras().getString("licenseplate");
	 		mLocationNumber = intent.getExtras().getInt("locationnumber");
	 		mCarType =  intent.getExtras().getString("cartype");
	 		mParkType = intent.getExtras().getString("parktype");
	 		mStartTime = intent.getExtras().getString("starttime");
	 		mLeaveTime = intent.getExtras().getString("leavetime");
			mExpense=intent.getExtras().getString("expense");
	    	mDBAdapter = new DBAdapter(getActivity());
	    	mFragmentView = (View)mView.findViewById(R.id.fm_mobile_payment);
	    	mPaymentIV=(ImageView)mView.findViewById(R.id.iv_two_dimensions_code);
	    	if(mType==PAYMENT_TYPE_ALIPAY){
	    		mFragmentView.setBackgroundResource(R.color.blue);
	    		mPaymentIV.setBackgroundResource(R.drawable.ic_alipay_two_dimensions_code);
	    	}else if(mType==PAYMENT_TYPE_WECHATPAY){
	    		mFragmentView.setBackgroundResource(R.color.green);
	    		mPaymentIV.setBackgroundResource(R.drawable.ic_alipay_two_dimensions_code);
	    	}
	    	mPaymentIV.setOnClickListener(new OnClickListener(){
	        	@Override
	        	public void onClick(View v){
	        		new SQLThread().start();
	        	}
	        });
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

	    public class SQLThread extends Thread {
	        @Override
	        public void run () {
	        	mDBAdapter.open();
	        	Cursor cursor = mDBAdapter.getParkingByLicensePlate(mLicensePlateNumber);
	            try {
	            	cursor.moveToFirst();
	            	if(cursor.getString(cursor.getColumnIndex("paymentpattern")).equals("未付")){
	       	             mCurrentRowID = cursor.getLong(cursor.getColumnIndex("_id"));
	       	          if(mDBAdapter.updateParking(mCurrentRowID, mLeaveTime, mExpense, "移动支付")){
	  	        		Intent intent = new Intent(getActivity(),MobilePaymentSuccessActivity.class);
	  	        		Bundle bundle = new Bundle();
	            		bundle.putString("licenseplate", mLicensePlateNumber);
	            		bundle.putInt("locationnumber", mLocationNumber);
	            		bundle.putString("cartype", mCarType);
	            		bundle.putString("parktype", mParkType);
	            		bundle.putString("starttime", mStartTime);
	            		bundle.putString("leavetime", mLeaveTime);
	            		bundle.putString("expense", mExpense);
	            		intent.putExtras(bundle);
		        		startActivity(intent);
	       	          }
	            	}
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
}
