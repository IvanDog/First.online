package com.ehl.itspark.info;

import com.ehl.itspark.common.CommonRequestHeader;

public class ClockInfo {

	private CommonRequestHeader header;
	private String parkNo;
	private int clockType;
	private String clockTime;
	private Integer locationState;

	public CommonRequestHeader getheader() {
		return header;
	}
	public void setHeader(CommonRequestHeader header) {
		this.header = header;
	}
	public String getParkNo() {
		return parkNo;
	}
	public void setParkNo(String parkNo) {
		this.parkNo = parkNo;
	}
	public int getClockType() {
		return clockType;
	}
	public void setClockType(int clockType) {
		this.clockType = clockType;
	}
	public String getClockTime() {
		return clockTime;
	}
	public void setClockTime(String clockTime) {
		this.clockTime = clockTime;
	}
	public Integer getLocationState() {
		return locationState;
	}
	public void setLocationState(Integer locationState) {
		this.locationState = locationState;
	}
}
