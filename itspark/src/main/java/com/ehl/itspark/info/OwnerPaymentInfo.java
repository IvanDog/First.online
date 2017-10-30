package com.ehl.itspark.info;

import com.ehl.itspark.common.CommonRequestHeader;

public class OwnerPaymentInfo {

	private CommonRequestHeader header;
	private String parkNumber;
	private String licensePlateNumber;
	private String parkingRecordID;
	private Integer paymentPattern;
	private String expense;
	private String password;
	
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
	
	public String getLicensePlateNumber() {
		return licensePlateNumber;
	}
	public void setLicensePlateNumber(String licensePlateNumber) {
		this.licensePlateNumber = licensePlateNumber;
	}
	
	public String getParkingRecordID() {
		return parkingRecordID;
	}
	public void setParkingRecordID(String parkingRecordID) {
		this.parkingRecordID = parkingRecordID;
	}
	
	public Integer getPaymentPattern() {
		return paymentPattern;
	}
	public void setPaymentPattern(Integer paymentPattern) {
		this.paymentPattern = paymentPattern;
	}
	
	public String getExpense() {
		return expense;
	}
	public void setExpense(String expense) {
		this.expense = expense;
	}
	
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	@Override
	public String toString() {
		return "PaymentInfo [header=" + header + ", parkingRecordID=" + parkingRecordID + ", paymentPattern="
				+ paymentPattern + ", expense=" + expense + "]";
	}
}

