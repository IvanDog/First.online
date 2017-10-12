package com.example.parking.view;

import com.example.parking.R;
import com.example.parking.R.id;
import com.example.parking.R.layout;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

public class LocationFragment extends Fragment {
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
        return inflater.inflate(R.layout.fragment_location, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Button locationButtonTJ = (Button) getActivity().findViewById(R.id.locationButtonTJ);
        Button locationButtonBJ = (Button) getActivity().findViewById(R.id.locationButtonBJ);
        Button locationButtonSH = (Button) getActivity().findViewById(R.id.locationButtonSH);
        Button locationButtonHB = (Button) getActivity().findViewById(R.id.locationButtonHB);
        Button locationButtonSD = (Button) getActivity().findViewById(R.id.locationButtonSD);
        Button locationButtonSX = (Button) getActivity().findViewById(R.id.locationButtonSX);
        
        Button locationButtonCQ = (Button) getActivity().findViewById(R.id.locationButtonCQ);
        Button locationButtonSC = (Button) getActivity().findViewById(R.id.locationButtonSC);
        Button locationButtonLN = (Button) getActivity().findViewById(R.id.locationButtonLN);
        Button locationButtonJL = (Button) getActivity().findViewById(R.id.locationButtonJL);
        Button locationButtonHLJ = (Button) getActivity().findViewById(R.id.locationButtonHLJ);
        Button locationButtonNMG = (Button) getActivity().findViewById(R.id.locationButtonNMG);
        
        Button locationButtonJS = (Button) getActivity().findViewById(R.id.locationButtonJS);
        Button locationButtonZJ = (Button) getActivity().findViewById(R.id.locationButtonZJ);
        Button locationButtonAH = (Button) getActivity().findViewById(R.id.locationButtonAH);
        Button locationButtonFJ = (Button) getActivity().findViewById(R.id.locationButtonFJ);
        Button locationButtonJX = (Button) getActivity().findViewById(R.id.locationButtonJX);
        Button locationButtonHeNan = (Button) getActivity().findViewById(R.id.locationButtonHeNan);
        
        Button locationButtonHuBei = (Button) getActivity().findViewById(R.id.locationButtonHuBei);
        Button locationButtonHuNan = (Button) getActivity().findViewById(R.id.locationButtonHuNan);
        Button locationButtonGD = (Button) getActivity().findViewById(R.id.locationButtonGD);
        Button locationButtonGX = (Button) getActivity().findViewById(R.id.locationButtonGX);
        Button locationButtonHaiNan = (Button) getActivity().findViewById(R.id.locationButtonHaiNan);
        Button locationButtonGZ = (Button) getActivity().findViewById(R.id.locationButtonGZ);
      
        Button locationButtonYN = (Button) getActivity().findViewById(R.id.locationButtonYN);
        Button locationButtonXZ = (Button) getActivity().findViewById(R.id.locationButtonXZ);
        Button locationButtonGS = (Button) getActivity().findViewById(R.id.locationButtonGS);
        Button locationButtonSXX = (Button) getActivity().findViewById(R.id.locationButtonSXX);
        Button locationButtonQH = (Button) getActivity().findViewById(R.id.locationButtonQH);
        Button locationButtonNX = (Button) getActivity().findViewById(R.id.locationButtonNX);
        Button locationButtonXJ = (Button) getActivity().findViewById(R.id.locationButtonXJ);
        
        ImageButton backspaceButton = (ImageButton) getActivity().findViewById(R.id.bt_back_space_location);
        locationButtonTJ.setOnClickListener(mOnClickListener);  
        locationButtonBJ.setOnClickListener(mOnClickListener); 
        locationButtonSH.setOnClickListener(mOnClickListener); 
        locationButtonHB.setOnClickListener(mOnClickListener); 
        locationButtonSD.setOnClickListener(mOnClickListener); 
        locationButtonSX.setOnClickListener(mOnClickListener); 
        
        locationButtonCQ.setOnClickListener(mOnClickListener);  
        locationButtonSC.setOnClickListener(mOnClickListener); 
        locationButtonLN.setOnClickListener(mOnClickListener); 
        locationButtonJL.setOnClickListener(mOnClickListener); 
        locationButtonHLJ.setOnClickListener(mOnClickListener); 
        locationButtonNMG.setOnClickListener(mOnClickListener); 
        
        locationButtonJS.setOnClickListener(mOnClickListener);  
        locationButtonZJ.setOnClickListener(mOnClickListener); 
        locationButtonAH.setOnClickListener(mOnClickListener); 
        locationButtonFJ.setOnClickListener(mOnClickListener); 
        locationButtonJX.setOnClickListener(mOnClickListener); 
        locationButtonHeNan.setOnClickListener(mOnClickListener); 
        
        locationButtonHuBei.setOnClickListener(mOnClickListener);  
        locationButtonHuNan.setOnClickListener(mOnClickListener); 
        locationButtonGD.setOnClickListener(mOnClickListener); 
        locationButtonGX.setOnClickListener(mOnClickListener); 
        locationButtonHaiNan.setOnClickListener(mOnClickListener); 
        locationButtonGZ.setOnClickListener(mOnClickListener); 
        
        locationButtonYN.setOnClickListener(mOnClickListener);  
        locationButtonXZ.setOnClickListener(mOnClickListener); 
        locationButtonGS.setOnClickListener(mOnClickListener); 
        locationButtonSX.setOnClickListener(mOnClickListener); 
        locationButtonQH.setOnClickListener(mOnClickListener); 
        locationButtonNX.setOnClickListener(mOnClickListener); 
        locationButtonXJ.setOnClickListener(mOnClickListener); 
        
        backspaceButton.setOnClickListener(mOnClickListener);
    }

    OnClickListener mOnClickListener = new OnClickListener() {  
        @Override  
        public void onClick(View v) {
        	int resId = v.getId();
        	EditText editText = (EditText) getActivity().findViewById(R.id.et_license_plate);
            switch (resId) {  
                case R.id.locationButtonTJ:  
            	    editText.append("津");
                    break;  
                case R.id.locationButtonBJ:  
                	editText.append("京");
                    break;
                case R.id.locationButtonSH:  
                	editText.append("沪");
                    break;
                case R.id.locationButtonHB:  
                	editText.append("冀");
                    break;  
                case R.id.locationButtonSD:  
                	editText.append("鲁");
                    break;  
                case R.id.locationButtonSX:  
                	editText.append("晋");
                    break;
                case R.id.locationButtonCQ:  
            	    editText.append("渝");
                    break;  
                case R.id.locationButtonSC:  
                	editText.append("川");
                    break;
                case R.id.locationButtonLN:  
                	editText.append("辽");
                    break;
                case R.id.locationButtonJL:  
                	editText.append("吉");
                    break;  
                case R.id.locationButtonHLJ:  
                	editText.append("黑");
                    break;  
                case R.id.locationButtonNMG:  
                	editText.append("蒙");
                    break;
                case R.id.locationButtonJS:  
            	    editText.append("苏");
                    break;  
                case R.id.locationButtonZJ:  
                	editText.append("浙");
                    break;
                case R.id.locationButtonAH:  
                	editText.append("皖");
                    break;
                case R.id.locationButtonFJ:  
                	editText.append("闵");
                    break;  
                case R.id.locationButtonJX:  
                	editText.append("赣");
                    break;  
                case R.id.locationButtonHeNan:  
                	editText.append("豫");
                    break;
                case R.id.locationButtonHuBei:  
            	    editText.append("鄂");
                    break;  
                case R.id.locationButtonHuNan:  
                	editText.append("湘");
                    break;
                case R.id.locationButtonGD:  
                	editText.append("粤");
                    break;
                case R.id.locationButtonGX:  
                	editText.append("桂");
                    break;  
                case R.id.locationButtonHaiNan:  
                	editText.append("琼");
                    break;  
                case R.id.locationButtonGZ:  
                	editText.append("黔");
                    break;
                case R.id.locationButtonYN:  
                	editText.append("云");
                    break;
                case R.id.locationButtonXZ:  
            	    editText.append("藏");
                    break;  
                case R.id.locationButtonGS:  
                	editText.append("甘");
                    break;
                case R.id.locationButtonSXX:  
                	editText.append("陕");
                    break;
                case R.id.locationButtonQH:  
                	editText.append("青");
                    break;  
                case R.id.locationButtonNX:  
                	editText.append("宁");
                    break;  
                case R.id.locationButtonXJ:  
                	editText.append("新");
                	break;
                case R.id.bt_back_space_location:
                	String str = editText.getText().toString();
                    if (str!= null && !str.equals("")){   
                        String string = new String();   
                        if (str.length() > 1){     
                             string =   str.substring(0, str.length() - 1);    
                        }else {    
                             string =  null;   
                        }
                        editText.setText(string);
                    }
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
