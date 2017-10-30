package com.ehl.itspark.common;


public class CommonRequestHeader{
	private int requestCode;
	private String account;
	private String token;
	
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
	
	public int getRequestCode() {
		return requestCode;
	}
	public void setRequestCode(int requestCode) {
		this.requestCode = requestCode;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	@Override
	public String toString() {
		return "CommonRequestHeader [requestCode=" + requestCode + ", account=" + account + ", token="
				+ token + "]";
	}
}
