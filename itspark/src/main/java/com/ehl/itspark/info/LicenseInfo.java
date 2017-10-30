package com.ehl.itspark.info;

import com.ehl.itspark.common.CommonRequestHeader;

public class LicenseInfo {

	private CommonRequestHeader header;
	private String carType;
	private String parkNumber;
	private String licensePlateNumber;
	private Integer type;
	
	public CommonRequestHeader getheader() {
		return header;
	}
	public void setHeader(CommonRequestHeader header) {
		this.header = header;
	}
	
	public String getCarType() {
		return carType;
	}
	public void setCarType(String carType) {
		this.carType = carType;
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
	
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	
	@Override
	public String toString() {
		return "LicenseInfo [header=" + header + ", carType=" + carType + ", parkNumber="
				+ parkNumber + ", licensePlateNumber=" + licensePlateNumber + ", type=" + type + "]";
	}
}
