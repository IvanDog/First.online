package com.ehl.itspark.info;

import com.ehl.itspark.common.CommonRequestHeader;

public class HistoryRecordSearchInfo {

	private CommonRequestHeader header;
	private String parkNumber;
	private Integer paymentPattern;
	private String date;

	
	public CommonRequestHeader getheader() {
		return header;
	}
	public void setHeader(CommonRequestHeader header) {
		this.header = header;
	}
	
	public String getParkNumber() {
		return parkNumber;
	}
	public void setParkNumber(String parkNumber) {
		this.parkNumber = parkNumber;
	}
	
	public Integer getPaymentPattern() {
		return paymentPattern;
	}
	public void setPaymentPattern(Integer paymentPattern) {
		this.paymentPattern = paymentPattern;
	}
	
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
}

