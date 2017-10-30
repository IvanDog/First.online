package com.ehl.itspark.common;

import java.util.ArrayList;
import java.util.HashMap;

public class CommonResponse {  
	  
    private String resCode;  
    private String resMsg;  
    private HashMap<String, Object> property;  
    private ArrayList<HashMap<String, Object>> list;  
  
    public CommonResponse() {  
        super();  
        resCode = "";  
        resMsg = "";  
        property = new HashMap<String, Object>();  
        list = new ArrayList<HashMap<String, Object>>();  
  
    }  
  
    public void setResult(String resCode, String resMsg) {  
        this.resCode = resCode;  
        this.resMsg = resMsg;  
    }  
  
    public String getResCode() {  
        return resCode;  
    }  
  
    public void setResCode(String resCode) {  
        this.resCode = resCode;  
    }  
  
    public String getResMsg() {  
        return resMsg;  
    }  
  
    public void setResMsg(String resMsg) {  
        this.resMsg = resMsg;  
    }  
  
    public HashMap<String, Object> getProperty() {  
        return property;  
    }  
  
    public void addListItem(HashMap<String, Object> map) {  
        list.add(map);  
    }  
      
    public ArrayList<HashMap<String, Object>> getList() {  
        return list;  
    }  
}
