package com.example.parking.view;

import com.example.parking.R;
import com.example.parking.R.color;
import com.example.parking.R.id;
import com.example.parking.R.layout;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

public class CarTypeFragment extends Fragment{
	
    private Button mCarTypeBT;
    private Button mBusTypeBT;
    private Button mTruckTypeB ;
    
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
        return inflater.inflate(R.layout.fragment_car_type, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mCarTypeBT = (Button) getActivity().findViewById(R.id.bt_type_car);
        mBusTypeBT = (Button) getActivity().findViewById(R.id.bt_type_bus);
        mTruckTypeB = (Button) getActivity().findViewById(R.id.bt_type_truck);
        mCarTypeBT.setOnClickListener(
    	    new OnClickListener(){
    		    public void onClick(View v) {
            	    if(mCarTypeBT.isSelected()){
                	    mCarTypeBT.setSelected(false);
                	    ((InputLicenseActivity)getActivity()).setCarType(null);
            	    }else{
                	    if(mBusTypeBT.isSelected()){
                		mBusTypeBT.setSelected(false);
                	}
                	if(mTruckTypeB.isSelected()){
                		mTruckTypeB.setSelected(false);
                	}
                	mCarTypeBT.setSelected(true);
                	((InputLicenseActivity)getActivity()).setCarType(mCarTypeBT.getText().toString());
            	}
    		}
        });
    
        mBusTypeBT.setOnClickListener(
        	    new OnClickListener(){
        		    public void onClick(View v) {
                	    if(mBusTypeBT.isSelected()){
                		    Log.e("yifan","1");
                		    mBusTypeBT.setSelected(false);
                    	    ((InputLicenseActivity)getActivity()).setCarType(null);
                	    }else{
                		    Log.e("yifan","2");
                    	    if(mCarTypeBT.isSelected()){
                    		mCarTypeBT.setSelected(false);
                    	}
                    	if(mTruckTypeB.isSelected()){
                    		mTruckTypeB.setSelected(false);
                    	}
                    	mBusTypeBT.setSelected(true);
                    	((InputLicenseActivity)getActivity()).setCarType(mBusTypeBT.getText().toString());
                	}
        		}
        });
    
        mTruckTypeB.setOnClickListener(
        	    new OnClickListener(){
        		    public void onClick(View v) {
                	    if(mTruckTypeB.isSelected()){
                		    Log.e("yifan","1");
                		    mTruckTypeB.setSelected(false);
                    	    ((InputLicenseActivity)getActivity()).setCarType(null);
                	    }else{
                		    Log.e("yifan","2");
                    	    if(mCarTypeBT.isSelected()){
                    		    mCarTypeBT.setSelected(false);
                    	    }
                    	if(mBusTypeBT.isSelected()){
                    		mBusTypeBT.setSelected(false);
                    	}
                    	mTruckTypeB.setSelected(true);
                    	((InputLicenseActivity)getActivity()).setCarType(mTruckTypeB.getText().toString());
                	}
        		}
        });
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
}