package com.example.parking.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.parking.R;
import com.example.parking.R.drawable;
import com.example.parking.R.id;
import com.example.parking.R.layout;

import android.R.color;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MessageCenterListAdapter extends BaseAdapter {  
	  
    private ArrayList<HashMap<String,Object>> data;  
    private LayoutInflater layoutInflater;  
    private Context context;
    public int clickPosition = -1;
    public Boolean flag = false;
    public MessageCenterListAdapter(Context context,ArrayList<HashMap<String, Object>> data){  
        this.context=context;  
        this.data=data;  
        this.layoutInflater=LayoutInflater.from(context);  
    }  
    /** 
     * 组件集合，对应list.xml中的控件 
     * @author Administrator 
     */  
    public final class Zujian{  
        private TextView messageCenterTitleTV;
        private TextView messageCenterDetailTV;
        private TextView messageCenterTimeTV;
        private ImageView enterMessageDetail;
    	private LinearLayout messageCenterHideDetail;
    	private TextView messageCenterDetailHideTV;
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
            convertView=layoutInflater.inflate(R.layout.list_message_center, null);  
            zujian.messageCenterTitleTV=(TextView)convertView.findViewById(R.id.tv_title_message_center);  
            zujian.messageCenterDetailTV=(TextView)convertView.findViewById(R.id.tv_detail_message_center);  
            zujian.messageCenterTimeTV=(TextView)convertView.findViewById(R.id.tv_time_message_center);  
            zujian.enterMessageDetail=(ImageView)convertView.findViewById(R.id.iv_enter_message_detail);
            zujian.messageCenterHideDetail=(LinearLayout)convertView.findViewById(R.id.list_message_center_hide);
            zujian.messageCenterDetailHideTV=(TextView)convertView.findViewById(R.id.tv_message_detail_hide);  
            convertView.setTag(zujian);  
        }else{  
            zujian=(Zujian)convertView.getTag();  
        }  
        //绑定数据  
        zujian.messageCenterTitleTV.setText((String)data.get(position).get("messageTitle")); 
        zujian.messageCenterDetailTV.setText((String)data.get(position).get("messageAbstract")); 
        zujian.messageCenterTimeTV.setText((String)(data.get(position).get("messageTime"))); 
        zujian.messageCenterDetailHideTV.setText((String)(data.get(position).get("messageDetail"))); 
        Drawable drawable = context.getResources().getDrawable(R.drawable.ic_error_outline_black_18dp);
        if("停车通知".equals((String)data.get(position).get("messageTitle"))){
        	drawable = context.getResources().getDrawable(R.drawable.ic_add_alert_black_18dp);
        }else if("考勤通知".equals((String)data.get(position).get("messageTitle"))){
        	drawable = context.getResources().getDrawable(R.drawable.ic_error_outline_black_18dp);
        }
		drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight()); 
		zujian.messageCenterTitleTV.setCompoundDrawables(drawable, null, null, null);
		if (clickPosition == position) {
		    if (zujian.enterMessageDetail.isSelected()) {
		    	    zujian.enterMessageDetail.setSelected(false);
		    	    zujian.enterMessageDetail.setImageResource(R.drawable.ic_chevron_right_black_24dp);
                    zujian.messageCenterHideDetail.setVisibility(View.GONE);
                    clickPosition=-1;
		    }else{       
		    	    zujian.enterMessageDetail.setSelected(true);
		    	    zujian.enterMessageDetail.setImageResource(R.drawable.ic_expand_more_black_24dp);
                    zujian.messageCenterHideDetail.setVisibility(View.VISIBLE);
              }
        } else {
        	zujian.messageCenterHideDetail.setVisibility(View.GONE);
        	zujian.enterMessageDetail.setSelected(false);
    	    zujian.enterMessageDetail.setImageResource(R.drawable.ic_chevron_right_black_24dp);
        }
		zujian.enterMessageDetail.setOnClickListener(new OnClickListener(){
		    @Override
		    public void onClick(View v){
		    	clickPosition = position;
		    	notifyDataSetChanged();
		    }
		});
        return convertView;  
    }  
  
} 