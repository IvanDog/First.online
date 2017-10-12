package com.example.parking.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.parking.R;
import com.example.parking.R.id;
import com.example.parking.R.layout;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class TodayRecordListAdapter extends BaseAdapter {

    private ArrayList<HashMap<String, Object>> data;  
    private LayoutInflater layoutInflater;  
    private Context context;  
    public TodayRecordListAdapter(Context context,ArrayList<HashMap<String, Object>> data){  
        this.context=context;  
        this.data=data;  
        this.layoutInflater=LayoutInflater.from(context);  
    }  
    /** 
     * 组件集合，对应list.xml中的控件 
     * @author Administrator 
     */  
    public final class Zujian{  
        public TextView licensePlateNumberTV; 
        public TextView startTimeTV; 
        public TextView leaveTimeTV; 
        public TextView paymentStateTV; 
        public TextView paymentBillTV; 
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
    public View getView(int position, View convertView, ViewGroup parent) {  
        Zujian zujian=null;  
        if(convertView==null){  
            zujian=new Zujian();  
            //获得组件，实例化组件  
            convertView=layoutInflater.inflate(R.layout.list_record_today, null);  
            zujian.licensePlateNumberTV=(TextView)convertView.findViewById(R.id.tv_license_plate_number_record_today);  
            zujian.startTimeTV=(TextView)convertView.findViewById(R.id.tv_start_time_record_today);  
            zujian.leaveTimeTV=(TextView)convertView.findViewById(R.id.tv_leave_time_record_today);  
            zujian.paymentStateTV=(TextView)convertView.findViewById(R.id.tv_payment_state_record_today);  
            zujian.paymentBillTV=(TextView)convertView.findViewById(R.id.tv_payment_bill_record_today);  
            convertView.setTag(zujian);  
        }else{  
            zujian=(Zujian)convertView.getTag();  
        }  
        //绑定数据  
        zujian.licensePlateNumberTV.setText((String)data.get(position).get("licensePlateNumber"));  
        zujian.startTimeTV.setText((String)data.get(position).get("startTime"));  
        if(data.get(position).get("leaveTime")==null){
        	zujian.leaveTimeTV.setText("");
        }else{
            zujian.leaveTimeTV.setText( (String)data.get(position).get("leaveTime")); 
        }
        zujian.paymentStateTV.setText((String)data.get(position).get("paymentPattern"));  
        if(data.get(position).get("expense")==null){
            zujian.paymentBillTV.setText(null); 
        }else{
            zujian.paymentBillTV.setText((String)data.get(position).get("expense")); 
        }
        return convertView;  
    }

}
