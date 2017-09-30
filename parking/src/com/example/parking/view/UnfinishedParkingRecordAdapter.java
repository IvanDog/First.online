package com.example.parking.view;

import java.util.ArrayList;
import java.util.HashMap;

import com.example.parking.R;
import com.example.parking.R.id;
import com.example.parking.R.layout;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class UnfinishedParkingRecordAdapter extends BaseAdapter {

	  
    private ArrayList<HashMap<String, Object>> data;  
    private LayoutInflater layoutInflater;  
    private Context context;  
    public int clickPosition = -1;
    public UnfinishedParkingRecordAdapter(Context context,ArrayList<HashMap<String, Object>> data){  
        this.context=context;  
        this.data=data;  
        this.layoutInflater=LayoutInflater.from(context);  
    }
    
    /** 
     * 组件集合，对应list.xml中的控件 
     * @author Administrator 
     */  
    public final class Zujian{  
    	private TextView licenseNumberTV; 
    	private TextView startTimeTV;
    	private TextView leaveTimeTV;
    	private TextView parkNumberTV;
    	private TextView paymentBillTV;
    }  
    @Override  
    public int getCount() {  
        return data.size();  
    }  
    /** 
     * 获得某一位置的数据 
     */  
    @Override  
    public Object getItem(int position) {  
        return data.get(position);  
    }  
    /** 
     * 获得唯一标识 
     */  
    @Override  
    public long getItemId(int position) {  
        return position;  
    }  
  
    @Override  
    public View getView(final int position, View convertView, ViewGroup parent) {  
        Zujian zujian=null;  
        if(convertView==null){  
            zujian=new Zujian();  
            //获得组件，实例化组件  
            convertView=layoutInflater.inflate(R.layout.list_unfinished_parking_detail, null);  
            zujian.licenseNumberTV=(TextView)convertView.findViewById(R.id.tv_licensePlateNumber_unfinished); 
            zujian.startTimeTV=(TextView)convertView.findViewById(R.id.tv_startTime_unfinished); 
            zujian.leaveTimeTV = (TextView)convertView.findViewById(R.id.tv_leaveTime_unfinished); 
            zujian.parkNumberTV = (TextView)convertView.findViewById(R.id.tv_park_number_unfinished);  
            zujian.paymentBillTV = (TextView)convertView.findViewById(R.id.tv_payment_bill_unfinished);  
            convertView.setTag(zujian);  
        }else{  
            zujian=(Zujian)convertView.getTag();  
        }  
        //绑定数据  
        zujian.licenseNumberTV.setText((String)data.get(position).get("licensePlateNumber"));
        zujian.startTimeTV.setText((String)data.get(position).get("startTime"));
        zujian.leaveTimeTV.setText((String)data.get(position).get("leaveTime"));
        zujian.parkNumberTV.setText((String)data.get(position).get("parkNumber"));
        zujian.paymentBillTV.setText((String)data.get(position).get("expense"));
        return convertView;
    }  

}
