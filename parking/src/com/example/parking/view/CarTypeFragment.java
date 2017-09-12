package com.example.parking.view;

import com.example.parking.R;
import com.example.parking.R.color;
import com.example.parking.R.id;
import com.example.parking.R.layout;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
        mCarTypeBT.setOnClickListener(mOnClickListener);  
        mBusTypeBT.setOnClickListener(mOnClickListener); 
        mTruckTypeB.setOnClickListener(mOnClickListener);;
    }

    OnClickListener mOnClickListener = new OnClickListener() {  
        @Override  
        public void onClick(View v) {
        	int resId = v.getId();
            switch (resId) {  
                case R.id.bt_type_car:
                	if(mBusTypeBT.isSelected()){
                		mBusTypeBT.setSelected(false);
                		mBusTypeBT.setBackgroundColor(color.gray);
                	}
                	if(mTruckTypeB.isSelected()){
                		mTruckTypeB.setSelected(false);
                		mTruckTypeB.setBackgroundColor(color.gray);
                	}
                	mCarTypeBT.setSelected(true);
                	mCarTypeBT.setBackgroundColor(color.orange);
                	((InputLicenseActivity)getActivity()).setCarType(mCarTypeBT.getText().toString());
                    break;  
                case R.id.bt_type_bus:  
                	if(mCarTypeBT.isSelected()){
                		mCarTypeBT.setSelected(false);
                		mCarTypeBT.setBackgroundColor(color.gray);
                	}
                	if(mTruckTypeB.isSelected()){
                		mTruckTypeB.setSelected(false);
                		mTruckTypeB.setBackgroundColor(color.gray);
                	}
                	mBusTypeBT.setSelected(true);
                	mBusTypeBT.setBackgroundColor(color.orange);
                	((InputLicenseActivity)getActivity()).setCarType(mBusTypeBT.getText().toString());
                    break;
                case R.id.bt_type_truck:  
                	if(mCarTypeBT.isSelected()){
                		mCarTypeBT.setSelected(false);
                		mCarTypeBT.setBackgroundColor(color.gray);
                	}
                	if(mBusTypeBT.isSelected()){
                		mBusTypeBT.setSelected(false);
                		mBusTypeBT.setBackgroundColor(color.gray);
                	}
                	mTruckTypeB.setSelected(true);
                	mTruckTypeB.setBackgroundColor(color.orange);
                	((InputLicenseActivity)getActivity()).setCarType(mTruckTypeB.getText().toString());
                    break;
               }
        }  
    };

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