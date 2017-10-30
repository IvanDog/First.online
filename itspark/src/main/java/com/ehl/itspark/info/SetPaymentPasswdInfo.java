package com.ehl.itspark.info;

import com.ehl.itspark.common.CommonRequestHeader;

public class SetPaymentPasswdInfo {
	private CommonRequestHeader header;
	private String password;
	private String paymentPassword;
	
	public CommonRequestHeader getheader() {
		return header;
	}
	public void setHeader(CommonRequestHeader header) {
		this.header = header;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getPaymentPassword() {
		return paymentPassword;
	}
	public void setPaymentPassword(String paymentPassword) {
		this.paymentPassword = paymentPassword;
	}
}
