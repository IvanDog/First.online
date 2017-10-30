package com.ehl.itspark.info;

import com.ehl.itspark.common.CommonRequestHeader;

public class UnBindLicenseInfo {
	private CommonRequestHeader header;
	private String licensePlateDismiss;
	
	public CommonRequestHeader getheader() {
		return header;
	}
	public void setHeader(CommonRequestHeader header) {
		this.header = header;
	}
	public String getLicensePlateDismiss(){
		return licensePlateDismiss;
	}
	public void setLicensePlateDismiss(String licensePlateDismiss){
		this.licensePlateDismiss = licensePlateDismiss;
	}
}
