package com.ehl.itspark.data.entity;

import java.math.BigDecimal;

public class FeeRateEntity {

	private long id;
	//费率编号
	private String no;
	//停车场编号
	private String parkNo;
	private String parkName;
	//车辆类型
	private int plateType;
	private String plateTypeStr;
	//计费方式：1按次，2按时间
	private int type;
	private String typeStr;
	//按次金额
	private BigDecimal feeByCount;
	//免费时长
	private int freeTimeLen;
	//时间间隔
	private int timeLen;
	//时间间隔内费用
	private BigDecimal feeByTime;
	//最高限额
	private BigDecimal maxFee;
	
	private String descript;
	
	private String feeImg;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
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
	public int getPlateType() {
		return plateType;
	}
	public void setPlateType(int plateType) {
		this.plateType = plateType;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public BigDecimal getFeeByCount() {
		return feeByCount;
	}
	public void setFeeByCount(BigDecimal feeByCount) {
		this.feeByCount = feeByCount;
	}
	public int getFreeTimeLen() {
		return freeTimeLen;
	}
	public void setFreeTimeLen(int freeTimeLen) {
		this.freeTimeLen = freeTimeLen;
	}
	public int getTimeLen() {
		return timeLen;
	}
	public void setTimeLen(int timeLen) {
		this.timeLen = timeLen;
	}
	public BigDecimal getFeeByTime() {
		return feeByTime;
	}
	public void setFeeByTime(BigDecimal feeByTime) {
		this.feeByTime = feeByTime;
	}
	public BigDecimal getMaxFee() {
		return maxFee;
	}
	public void setMaxFee(BigDecimal maxFee) {
		this.maxFee = maxFee;
	}
	public String getDescript() {
		return descript;
	}
	public void setDescript(String descript) {
		this.descript = descript;
	}
	public String getFeeImg() {
		return feeImg;
	}
	public void setFeeImg(String feeImg) {
		this.feeImg = feeImg;
	}
	public String getParkName() {
		return parkName;
	}
	public void setParkName(String parkName) {
		this.parkName = parkName;
	}
	public String getPlateTypeStr() {
		return plateTypeStr;
	}
	public void setPlateTypeStr(String plateTypeStr) {
		this.plateTypeStr = plateTypeStr;
	}
	public String getTypeStr() {
		return typeStr;
	}
	public void setTypeStr(String typeStr) {
		this.typeStr = typeStr;
	}
	
}
