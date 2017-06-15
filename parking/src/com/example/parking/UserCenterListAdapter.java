package com.example.parking;

import java.util.List;
import java.util.Map;

import android.R.color;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class UserCenterListAdapter extends BaseAdapter {  
	  
    private List<Map<String, Object>> data;  
    private LayoutInflater layoutInflater;  
    private Context context;  
    public UserCenterListAdapter(Context context,List<Map<String, Object>> data){  
        this.context=context;  
        this.data=data;  
        this.layoutInflater=LayoutInflater.from(context);  
    }  
    /** 
     * 组件集合，对应list.xml中的控件 
     * @author Administrator 
     */  
    public final class Zujian{  
        public TextView userCenterFunctionTV; 
        public ImageView userCenterSpreadFunctionIV; 
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
            convertView=layoutInflater.inflate(R.layout.list_function_user_center, null);  
            zujian.userCenterFunctionTV=(TextView)convertView.findViewById(R.id.tv_funtion_user_center);  
            zujian.userCenterSpreadFunctionIV=(ImageView)convertView.findViewById(R.id.iv_enter_function_user_center);  
            convertView.setTag(zujian);  
        }else{  
            zujian=(Zujian)convertView.getTag();  
        }  
        //绑定数据  
        zujian.userCenterFunctionTV.setText((String)data.get(position).get("userCenterFunction"));  
        zujian.userCenterSpreadFunctionIV.setImageResource((Integer)data.get(position).get("userCenterFunctionSpreadImage"));
		Drawable drawable = context.getResources().getDrawable((Integer)data.get(position).get("userCenterFunctionImage"));
		drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight()); 
		zujian.userCenterFunctionTV.setCompoundDrawables(drawable, null, null, null);
        return convertView;  
    }  
  
} 