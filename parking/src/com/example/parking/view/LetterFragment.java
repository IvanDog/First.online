package com.example.parking.view;

import com.example.parking.R;
import com.example.parking.R.id;
import com.example.parking.R.layout;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

public class LetterFragment extends Fragment{
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
        return inflater.inflate(R.layout.fragment_letter, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Button letterButtonA = (Button) getActivity().findViewById(R.id.letterButtonA);
        Button letterButtonB = (Button) getActivity().findViewById(R.id.letterButtonB);
        Button letterButtonC = (Button) getActivity().findViewById(R.id.letterButtonC);
        Button letterButtonD = (Button) getActivity().findViewById(R.id.letterButtonD);
        Button letterButtonE = (Button) getActivity().findViewById(R.id.letterButtonE);
        Button letterButtonF = (Button) getActivity().findViewById(R.id.letterButtonF);
        Button letterButtonG = (Button) getActivity().findViewById(R.id.letterButtonG);
        Button letterButtonH = (Button) getActivity().findViewById(R.id.letterButtonH);
        Button letterButtonI = (Button) getActivity().findViewById(R.id.letterButtonI);
        Button letterButtonJ = (Button) getActivity().findViewById(R.id.letterButtonJ);
        Button letterButtonK = (Button) getActivity().findViewById(R.id.letterButtonK);
        Button letterButtonL = (Button) getActivity().findViewById(R.id.letterButtonL);
        Button letterButtonM = (Button) getActivity().findViewById(R.id.letterButtonM);
        Button letterButtonN = (Button) getActivity().findViewById(R.id.letterButtonN);
        Button letterButtonO = (Button) getActivity().findViewById(R.id.letterButtonO);
        Button letterButtonP = (Button) getActivity().findViewById(R.id.letterButtonP);
        Button letterButtonQ = (Button) getActivity().findViewById(R.id.letterButtonQ);
        Button letterButtonR = (Button) getActivity().findViewById(R.id.letterButtonR);
        Button letterButtonS = (Button) getActivity().findViewById(R.id.letterButtonS);
        Button letterButtonT = (Button) getActivity().findViewById(R.id.letterButtonT);
        Button letterButtonU = (Button) getActivity().findViewById(R.id.letterButtonU);
        Button letterButtonV = (Button) getActivity().findViewById(R.id.letterButtonV);
        Button letterButtonW = (Button) getActivity().findViewById(R.id.letterButtonW);
        Button letterButtonX = (Button) getActivity().findViewById(R.id.letterButtonX);
        Button letterButtonY = (Button) getActivity().findViewById(R.id.letterButtonY);
        Button letterButtonZ = (Button) getActivity().findViewById(R.id.letterButtonZ);
        ImageButton backspaceButton = (ImageButton) getActivity().findViewById(R.id.bt_back_space_letter);
        letterButtonA.setOnClickListener(mOnClickListener);  
        letterButtonB.setOnClickListener(mOnClickListener); 
        letterButtonC.setOnClickListener(mOnClickListener); 
        letterButtonD.setOnClickListener(mOnClickListener); 
        letterButtonE.setOnClickListener(mOnClickListener); 
        letterButtonF.setOnClickListener(mOnClickListener); 
        letterButtonG.setOnClickListener(mOnClickListener); 
        letterButtonH.setOnClickListener(mOnClickListener); 
        letterButtonI.setOnClickListener(mOnClickListener); 
        letterButtonJ.setOnClickListener(mOnClickListener); 
        letterButtonK.setOnClickListener(mOnClickListener);  
        letterButtonL.setOnClickListener(mOnClickListener); 
        letterButtonM.setOnClickListener(mOnClickListener); 
        letterButtonN.setOnClickListener(mOnClickListener); 
        letterButtonO.setOnClickListener(mOnClickListener); 
        letterButtonP.setOnClickListener(mOnClickListener); 
        letterButtonQ.setOnClickListener(mOnClickListener); 
        letterButtonR.setOnClickListener(mOnClickListener); 
        letterButtonS.setOnClickListener(mOnClickListener); 
        letterButtonT.setOnClickListener(mOnClickListener); 
        letterButtonU.setOnClickListener(mOnClickListener); 
        letterButtonV.setOnClickListener(mOnClickListener); 
        letterButtonW.setOnClickListener(mOnClickListener); 
        letterButtonX.setOnClickListener(mOnClickListener); 
        letterButtonY.setOnClickListener(mOnClickListener); 
        letterButtonZ.setOnClickListener(mOnClickListener); 
        letterButtonZ.setOnClickListener(mOnClickListener);
        backspaceButton.setOnClickListener(mOnClickListener);
    }

    OnClickListener mOnClickListener = new OnClickListener() {  
        @Override  
        public void onClick(View v) {
        	int resId = v.getId();
        	EditText editText = (EditText) getActivity().findViewById(R.id.et_license_plate);
            switch (resId) {  
                case R.id.letterButtonA:  
            	    editText.append("A");
                    break;  
                case R.id.letterButtonB:  
                	editText.append("B");
                    break;
                case R.id.letterButtonC:  
                	editText.append("C");
                    break;
                case R.id.letterButtonD:  
                	editText.append("D");
                    break;  
                case R.id.letterButtonE:  
                	editText.append("E");
                    break;  
                case R.id.letterButtonF:  
                	editText.append("F");
                    break;  
                case R.id.letterButtonG:  
                	editText.append("G");
                    break;  
                case R.id.letterButtonH:  
                	editText.append("H");
                    break;  
                case R.id.letterButtonI:  
                	editText.append("I");
                    break;  
                case R.id.letterButtonJ:  
                	editText.append("J");
                    break;  
                case R.id.letterButtonK:  
            	    editText.append("K");
                    break;  
                case R.id.letterButtonL:  
                	editText.append("L");
                    break;
                case R.id.letterButtonM:  
                	editText.append("M");
                    break;
                case R.id.letterButtonN:  
                	editText.append("N");
                    break;  
                case R.id.letterButtonO:  
                	editText.append("O");
                    break;  
                case R.id.letterButtonP:  
                	editText.append("P");
                    break;  
                case R.id.letterButtonQ:  
                	editText.append("Q");
                    break;  
                case R.id.letterButtonR:  
                	editText.append("R");
                    break;  
                case R.id.letterButtonS:  
                	editText.append("S");
                    break;  
                case R.id.letterButtonT:  
                	editText.append("T");
                	break;
                case R.id.letterButtonU:  
                	editText.append("U");
                    break;  
                case R.id.letterButtonV:  
                	editText.append("V");
                    break;  
                case R.id.letterButtonW:  
                	editText.append("W");
                    break;  
                case R.id.letterButtonX:  
                	editText.append("X");
                    break;  
                case R.id.letterButtonY:  
                	editText.append("Y");
                    break;  
                case R.id.letterButtonZ:  
                	editText.append("Z");
                	break;
                case R.id.bt_back_space_letter:
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
