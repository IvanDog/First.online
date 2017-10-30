package com.example.parking.info;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

    /** 
     * 基本请求体封装类 
     */  
    public class CommonRequestHeader {
    	public static final int REQUEST_COLLECTOR_LOGIN_CODE = 101;
    	public static final int REQUEST_COLLECTOR_LOGOUT_CODE = 102;
    	public static final int REQUEST_COLLECTOR_GET_WORK_CODE = 103;
    	public static final int REQUEST_COLLECTOR_GET_LOCATION_STATE_CODE = 104;
    	public static final int REQUEST_COLLECTOR_WORK_CLOCK_CODE = 105;
    	public static final int REQUEST_COLLECTOR_LICENSE_INPUT_CODE = 106;
    	public static final int REQUEST_COLLECTOR_NEW_PARKING_CODE = 107;
    	public static final int REQUEST_COLLECTOR_QUERY_EXPENSE_CODE = 108;
    	public static final int REQUEST_COLLECTOR_PAY_CODE = 109;
    	public static final int REQUEST_COLLECTOR_QUERY_PARKING_SPACE_CODE = 110;
    	public static final int REQUEST_COLLECTOR_QUERY_PARKING_INFORMATION_CODE = 111;
    	public static final int REQUEST_COLLECTOR_QUERY_TODAY_PARKING_RECORD_CODE = 112;
    	public static final int REQUEST_COLLECTOR_QUERY_HISTORY_PARKING_RECORD_CODE = 113;
    	public static final int REQUEST_COLLECTOR_MESSAGE_CENTER_CODE = 114;
    	public static final int REQUEST_COLLECTOR_RESET_PASSWORD_CODE = 115;
		public static final int REQUEST_COLLECTOR_QUERY_RESULT_CODE = 116;
        /** 
         * 请求码
         */  
        private int requestCode;  
        /** 
         * 账户
         */  
        private String account;  
        /** 
         * token
         */  
        private String token;   
      
//        public CommonRequestHeader() {  
//        	this.put("requestCode"," -1");  
//        	this.put("account", "");  
//        	this.put("token", "");  
//        }  
//      
//
        /** 
         * 为请求报文设置报头
         */  
        public void addRequestHeader(int requestCode,String account,String token) {  
        	setRequestCode(requestCode) ;  
        	setAccount(account);  
        	setToken(token);  
        }
//        /** 
//         * 获取请求码
//         */  
//        public int getRequestCode() {  
//        	return Integer.parseInt(String.valueOf(this.get("requestCode")));
//        }
//        /** 
//         * 获取账户名
//         */  
//        public String getAccount() {  
//        	return (String)this.get("account");
//        }
//        /** 
//         * 获取token
//         */  
//        public String getToken() {  
//        	return (String)this.get("token");
//        }


		public void setRequestCode(int requestCode) {
			this.requestCode = requestCode;
		}


		public void setAccount(String account) {
			this.account = account;
		}


		public void setToken(String token) {
			this.token = token;
		}


		public int getRequestCode() {
			return requestCode;
		}


		public String getAccount() {
			return account;
		}


		public String getToken() {
			return token;
		}
        
        
    }  