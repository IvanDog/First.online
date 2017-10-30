package com.ehl.itspark.info;

import com.ehl.itspark.common.CommonRequestHeader;

public class SearchParkInfo {
	private CommonRequestHeader header;
	private Double longitude;
	private Double latitude;
	private int parkingType;
	private int range;
	
	public CommonRequestHeader getheader() {
		return header;
	}
	public void setHeader(CommonRequestHeader header) {
		this.header = header;
	}
	
	public Double getLongitude(){
		return longitude;
	}
	public void setLongitude(Double longitude){
		this.longitude = longitude;
	}
	
	public Double getLatitude(){
		return latitude;
	}
	public void setLatitude(Double latitude){
		this.latitude = latitude;
	}
	
	public int getParkingType(){
		return parkingType;
	}
	public void setParkingType(int parkingType){
		this.parkingType = parkingType;
	}
	
	public int getRange(){
		return range;
	}
	public void setRange(int range){
		this.range = range;
	}
}
