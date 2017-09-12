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
    	private TextView parkNumberTV;
    	private TextView licensenumberTV; 
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
            convertView=layoutInflater.inflate(R.layout.list_parking_detail, null);  
            zujian.parkNumberTV = (TextView)convertView.findViewById(R.id.tv_parkingnumber);  
            zujian.licensenumberTV=(TextView)convertView.findViewById(R.id.tv_licenseplatenumber);  
            convertView.setTag(zujian);  
        }else{  
            zujian=(Zujian)convertView.getTag();  
        }  
        //绑定数据  
        zujian.parkNumberTV.setText((String)data.get(position).get("parkNumber"));
        zujian.licensenumberTV.setText((String)data.get(position).get("licensePlateNumber"));
        return convertView;
    }  

}
