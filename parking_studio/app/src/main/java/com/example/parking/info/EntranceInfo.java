package com.example.parking.info;


public class EntranceInfo {

	private CommonRequestHeader header;
	private String parkNumber;
	private String licensePlateNumber;
	private String parkingLocation;
	private String carType;
	private String parkType;
	private String enterTime;
	private byte[] enterImage;
	
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
	
	public String getParkingLocation() {
		return parkingLocation;
	}
	public void setParkingLocation(String parkingLocation) {
		this.parkingLocation = parkingLocation;
	}
	
	public String getCarType() {
		return carType;
	}
	public void setCarType(String carType) {
		this.carType = carType;
	}
	
	public String getParkType() {
		return parkType;
	}
	public void setParkType(String parkType) {
		this.parkType = parkType;
	}
	
	public 	String getEnterTime() {
		return enterTime;
	}
	public void setEnterTime(String enterTime) {
		this.enterTime = enterTime;
	}
	
	public byte[] getEnterImage() {
		return enterImage;
	}
	public void setEnterImage(byte[] enterImage) {
		this.enterImage = enterImage;
	}
}
