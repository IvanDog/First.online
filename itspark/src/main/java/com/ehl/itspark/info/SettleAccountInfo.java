package com.ehl.itspark.info;

import com.ehl.itspark.common.CommonRequestHeader;

public class SettleAccountInfo {

	private CommonRequestHeader header;
	private String parkNumber;
	private String licensePlateNumber;
	private String carType;
	private String leaveTime;
	private byte[] leaveImage;
	private String parkingEnterID;
	private String parkingRecordID;
	private String couponID;
	
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
	
	public String getCarType() {
		return carType;
	}
	public void setCarType(String carType) {
		this.carType = carType;
	}
	
	public 	String getLeaveTime() {
		return leaveTime;
	}
	public void setLeaveTime(String leaveTime) {
		this.leaveTime = leaveTime;
	}
	
	public byte[] getLeaveImage() {
		return leaveImage;
	}
	public void setLeaveImage(byte[] leaveImage) {
		this.leaveImage = leaveImage;
	}
	
	public String getParkingEnterID() {
		return parkingEnterID;
	}
	public void setParkingEnterID(String parkingEnterID) {
		this.parkingEnterID = parkingEnterID;
	}
	
	public String getParkingRecordID() {
		return parkingRecordID;
	}
	public void setParkingRecordID(String parkingRecordID) {
		this.parkingRecordID = parkingRecordID;
	}
	
	public String getCouponID() {
		return couponID;
	}
	public void setCouponID(String couponID) {
		this.couponID = couponID;
	}
	@Override
	public String toString(){
		return "SettleAccountInfo [header=" + header + ", parkNumber=" + parkNumber + ", licensePlateNumber="
				+ licensePlateNumber + ", parkingEnterID=" + parkingEnterID + "]";
	}
}

