package com.ehl.itspark.info;

import com.ehl.itspark.common.CommonRequestHeader;

public class RechargeInfo {

	private CommonRequestHeader header;
	private Integer paymentPattern;
	private String recharge;
	private String IP;
	
	public CommonRequestHeader getheader() {
		return header;
	}
	public void setHeader(CommonRequestHeader header) {
		this.header = header;
	}

	public Integer getPaymentPattern() {
		return paymentPattern;
	}
	public void setPaymentPattern(Integer paymentPattern) {
		this.paymentPattern = paymentPattern;
	}	
	
	public String getRecharge() {
		return recharge;
	}
	public void setRecharge(String recharge) {
		this.recharge= recharge;
	}
	
	public String getIP() {
		return IP;
	}
	public void setIP(String IP) {
		this.IP= IP;
	}
	
	@Override
	public String toString() {
		return "PaymentInfo [header=" + header + "paymentPattern=" + paymentPattern + "recharge=" + recharge + "]";
	}
}

