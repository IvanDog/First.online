package com.ehl.itspark.info;

import com.ehl.itspark.common.CommonRequestHeader;

public class ParkLotQueryInfo {

	private CommonRequestHeader header;
	private String parkNumber;
	
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
	
}
