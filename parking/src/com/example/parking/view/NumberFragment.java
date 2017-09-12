package com.example.parking.view;

import com.example.parking.R;
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
import android.widget.EditText;
import android.widget.ImageButton;

public class NumberFragment extends Fragment{
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
        return inflater.inflate(R.layout.fragment_number, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Button numberButton1 = (Button) getActivity().findViewById(R.id.numberButton1);
        Button numberButton2 = (Button) getActivity().findViewById(R.id.numberButton2);
        Button numberButton3 = (Button) getActivity().findViewById(R.id.numberButton3);
        Button numberButton4 = (Button) getActivity().findViewById(R.id.numberButton4);
        Button numberButton5 = (Button) getActivity().findViewById(R.id.numberButton5);
        Button numberButton6 = (Button) getActivity().findViewById(R.id.numberButton6);
        Button numberButton7 = (Button) getActivity().findViewById(R.id.numberButton7);
        Button numberButton8 = (Button) getActivity().findViewById(R.id.numberButton8);
        Button numberButton9 = (Button) getActivity().findViewById(R.id.numberButton9);
        Button numberButton0 = (Button) getActivity().findViewById(R.id.numberButton0);
        ImageButton backspaceButton = (ImageButton) getActivity().findViewById(R.id.bt_back_space_number);
        numberButton1.setOnClickListener(mOnClickListener);  
        numberButton2.setOnClickListener(mOnClickListener); 
        numberButton3.setOnClickListener(mOnClickListener); 
        numberButton4.setOnClickListener(mOnClickListener); 
        numberButton5.setOnClickListener(mOnClickListener); 
        numberButton6.setOnClickListener(mOnClickListener); 
        numberButton7.setOnClickListener(mOnClickListener); 
        numberButton8.setOnClickListener(mOnClickListener); 
        numberButton9.setOnClickListener(mOnClickListener); 
        numberButton0.setOnClickListener(mOnClickListener);
        backspaceButton.setOnClickListener(mOnClickListener);
    }

    OnClickListener mOnClickListener = new OnClickListener() {  
        @Override  
        public void onClick(View v) {
        	int resId = v.getId();
        	EditText editText = (EditText) getActivity().findViewById(R.id.et_license_plate);
            switch (resId) {  
                case R.id.numberButton1:  
            	    editText.append("1");
                    break;  
                case R.id.numberButton2:  
                	editText.append("2");
                    break;
                case R.id.numberButton3:  
                	editText.append("3");
                    break;
                case R.id.numberButton4:  
                	editText.append("4");
                    break;  
                case R.id.numberButton5:  
                	editText.append("5");
                    break;  
                case R.id.numberButton6:  
                	editText.append("6");
                    break;  
                case R.id.numberButton7:  
                	editText.append("7");
                    break;  
                case R.id.numberButton8:  
                	editText.append("8");
                    break;  
                case R.id.numberButton9:  
                	editText.append("9");
                    break;  
                case R.id.numberButton0:  
                	editText.append("0");
                    break;
                case R.id.bt_back_space_number:
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