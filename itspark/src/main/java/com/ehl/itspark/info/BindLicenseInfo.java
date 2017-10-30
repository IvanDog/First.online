package com.ehl.itspark.info;

import com.ehl.itspark.common.CommonRequestHeader;

public class BindLicenseInfo {
	private CommonRequestHeader header;
	private String licensePlateBind;
	private String carType;
	
	public CommonRequestHeader getheader() {
		return header;
	}
	public void setHeader(CommonRequestHeader header) {
		this.header = header;
	}
	public String getLicensePlateBind(){
		return licensePlateBind;
	}
	public void setLicensePlateBind(String licensePlateBind){
		this.licensePlateBind = licensePlateBind;
	}
	public String getCarType() {
		return carType;
	}
	public void setCarType(String carType) {
		this.carType = carType;
	}
}
