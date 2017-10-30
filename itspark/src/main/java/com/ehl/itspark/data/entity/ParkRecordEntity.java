package com.ehl.itspark.data.entity;

import java.util.Date;

public class ParkRecordEntity {

	private Long id;
	private String flowNo;
	private String plate;
	private Date enterTime;
	private String parkNo;
	private String parkLot;
	private byte[] enterImg;
	private Date leaveTime;
	private byte[] leaveImg;
	private Integer flag;
	private double payMoney;
	
	private String carType;//{小型客车，大型客车，货车}
	private String parkType;//{普通停车，免费停车}
	private Integer paymentFlag;//“0”表示未支付，“1”表示已支付
	private String tradeFlowNo;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getFlowNo() {
		return flowNo;
	}
	public void setFlowNo(String flowNo) {
		this.flowNo = flowNo;
	}
	public String getPlate() {
		return plate;
	}
	public void setPlate(String plate) {
		this.plate = plate;
	}
	public Date getEnterTime() {
		return enterTime;
	}
	public void setEnterTime(Date enterTime) {
		this.enterTime = enterTime;
	}
	public String getParkNo() {
		return parkNo;
	}
	public void setParkNo(String parkNo) {
		this.parkNo = parkNo;
	}
	public String getParkLot() {
		return parkLot;
	}
	public void setParkLot(String parkLot) {
		this.parkLot = parkLot;
	}
	public byte[] getEnterImg() {
		return enterImg;
	}
	public void setEnterImg(byte[] enterImg) {
		this.enterImg = enterImg;
	}
	public Date getLeaveTime() {
		return leaveTime;
	}
	public void setLeaveTime(Date leaveTime) {
		this.leaveTime = leaveTime;
	}
	public byte[] getLeaveImg() {
		return leaveImg;
	}
	public void setLeaveImg(byte[] leaveImg) {
		this.leaveImg = leaveImg;
	}
	public Integer getFlag() {
		return flag;
	}
	public void setFlag(Integer flag) {
		this.flag = flag;
	}
	public double getPayMoney() {
		return payMoney;
	}
	public void setPayMoney(double payMoney) {
		this.payMoney = payMoney;
	}
	
	public String getCarType() {
		return carType;
	}
	public void setCarType(String carType) {
		this.carType = carType;
	}
	public String getParkType() {
		return parkType;
	}
	public void setParkType(String parkType) {
		this.parkType = parkType;
	}
	public Integer getPaymentFlag() {
		return paymentFlag;
	}
	public void setPaymentFlag(Integer paymentFlag) {
		this.paymentFlag = paymentFlag;
	}
	public String getTradeFlowNo() {
		return tradeFlowNo;
	}
	public void setTradeFlowNo(String tradeFlowNo) {
		this.tradeFlowNo = tradeFlowNo;
	}
	
}
