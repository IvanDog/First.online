package com.ehl.itspark.data.entity;

import java.util.Date;

public class ClockEntity {

	private Long id;
	private String no;
	private String parkNo;
	private String employeeNo;
	private Integer type;
	private Date clockTime;
	private Integer locationState;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getNo() {
		return no;
	}
	public void setNo(String no) {
		this.no = no;
	}
	public String getParkNo() {
		return parkNo;
	}
	public void setParkNo(String parkNo) {
		this.parkNo = parkNo;
	}
	public String getEmployeeNo() {
		return employeeNo;
	}
	public void SetEmployeeNo(String employeeNo) {
		this.employeeNo = employeeNo;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public Date getClockTime() {
		return clockTime;
	}
	public void setClockTime(Date clockTime) {
		this.clockTime = clockTime;
	}
	public Integer getLocationState() {
		return locationState;
	}
	public void setLocationState(Integer locationState) {
		this.locationState = locationState;
	}
	
}
