package com.example.parking.info;


public class PaymentInfo {

	private CommonRequestHeader header;
	private String parkNumber;
	private String licensePlateNumber;
	private String parkingRecordID;
	private Integer paymentPattern;
	private String paidMoney;
	private String couponID;
	private String password;
	private String tradeRecordID;
	private String IP;
	private String authCode;
	
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
	
	public String getCouponID() {
		return couponID;
	}
	public void setCouponID(String couponID) {
		this.couponID = couponID;
	}
	
	public String getPaidMoney() {
		return paidMoney;
	}
	public void setPaidMoney(String paidMoney) {
		this.paidMoney= paidMoney;
	}
	
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getTradeRecordID() {
		return tradeRecordID;
	}
	public void setTradeRecordID(String tradeRecordID) {
		this.tradeRecordID = tradeRecordID;
	}
	
	public String getIP() {
		return IP;
	}
	public void setIP(String IP) {
		this.IP= IP;
	}
	
	public String getAuthCode() {
		return authCode;
	}
	public void setAuthCode(String authCode) {
		this.authCode= authCode;
	}
}