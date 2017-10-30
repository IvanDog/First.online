package com.ehl.itspark.data.entity;

import java.util.Date;

public class ParkLotEntity {

	private Long id;
	private String no;
	private String parkNo;
	private Integer tranMode;
	private String tranModeStr;
	private Integer status;
	private String statusStr;
	private String plate;
	private Date updateTime;
	
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
	public Integer getTranMode() {
		return tranMode;
	}
	public void setTranMode(Integer tranMode) {
		this.tranMode = tranMode;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getPlate() {
		return plate;
	}
	public void setPlate(String plate) {
		this.plate = plate;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public String getTranModeStr() {
		return tranModeStr;
	}
	public void setTranModeStr(String tranModeStr) {
		this.tranModeStr = tranModeStr;
	}
	public String getStatusStr() {
		return statusStr;
	}
	public void setStatusStr(String statusStr) {
		this.statusStr = statusStr;
	}
	
	
}
