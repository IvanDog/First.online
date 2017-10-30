package com.ehl.itspark.data.entity;

import java.util.Date;

public class CouponEntity {

	private Long id;
	private String couponID;
	private String couponTitle;
	private Double denomination;
	private Date startTime;
	private String startTimeStr;
	private Date endTime;
	private String endTimeStr;
	private String couponNotify;
	private String couponDetail;
	private Integer couponState;
	private String couponOwner;
	private Date createTime;
	private String remark;

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getCouponID() {
		return couponID;
	}
	public void setCouponID(String couponID) {
		this.couponID = couponID;
	}
	
	public String getCouponTitle() {
		return couponTitle;
	}
	public void setCouponTitle(String couponTitle) {
		this.couponTitle = couponTitle;
	}
	
	public double getDenomination() {
		return denomination;
	}
	
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	
	public String getCouponNotify(){
		return couponNotify;
	}
	public void setCouponNotify(String couponNotify) {
		this.couponNotify = couponNotify;
	}

	public String getCouponDetail() {
		return couponDetail;
	}
	public void setCouponDetail(String couponDetail) {
		this.couponDetail = couponDetail;
	}
	
	public  Integer getCouponState() {
		return couponState;
	}
	public String getCouponOwner() {
		return couponOwner;
	}
	public void setCouponOwner(String couponOwner) {
		this.couponOwner = couponOwner;
	}
	public String getStartTimeStr() {
		return startTimeStr;
	}
	public void setStartTimeStr(String startTimeStr) {
		this.startTimeStr = startTimeStr;
	}
	public String getEndTimeStr() {
		return endTimeStr;
	}
	public void setEndTimeStr(String endTimeStr) {
		this.endTimeStr = endTimeStr;
	}
	public void setDenomination(Double denomination) {
		this.denomination = denomination;
	}
	public void setCouponState(Integer couponState) {
		this.couponState = couponState;
	}
	
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
}
