package com.example.parking;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.parking.ParkingPlaceListAdapter.Zujian;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class HistoryRecordListAdapter extends BaseAdapter {

    private List<HashMap<String, Object>> data;  
    private LayoutInflater layoutInflater;  
    private Context context;  
    public HistoryRecordListAdapter(Context context,List<HashMap<String, Object>> data){  
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
        public TextView parkingLocationTV; 
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
            convertView=layoutInflater.inflate(R.layout.list_record_history, null);  
            zujian.licensePlateNumberTV=(TextView)convertView.findViewById(R.id.tv_licensePlateNumber_record_history);  
            zujian.startTimeTV=(TextView)convertView.findViewById(R.id.tv_startTime_record_history);  
            zujian.leaveTimeTV=(TextView)convertView.findViewById(R.id.tv_leaveTime_record_history);  
            zujian.parkingLocationTV=(TextView)convertView.findViewById(R.id.tv_parking_location_record_history);  
            zujian.paymentBillTV=(TextView)convertView.findViewById(R.id.tv_payment_bill_record_history);  
            convertView.setTag(zujian);  
        }else{  
            zujian=(Zujian)convertView.getTag();  
        }  
        //绑定数据  
        zujian.licensePlateNumberTV.setText((String)data.get(position).get("licensePlateNumber"));  
        zujian.startTimeTV.setText((String)data.get(position).get("startTime"));  
        zujian.leaveTimeTV.setText((String)data.get(position).get("leaveTime"));  
        String parkingLocation = (String)data.get(position).get("parkingLocation");
        if(parkingLocation.equals("泊位")){
        	zujian.parkingLocationTV.setText(parkingLocation);  
        }else if(parkingLocation.length()==1){
            zujian.parkingLocationTV.setText("A000" + parkingLocation);  
        }else if(parkingLocation.length()==2){
            zujian.parkingLocationTV.setText("A00" + parkingLocation);  
        }
        zujian.paymentBillTV.setText((String)data.get(position).get("expense")); 
        return convertView;  
    }

}
