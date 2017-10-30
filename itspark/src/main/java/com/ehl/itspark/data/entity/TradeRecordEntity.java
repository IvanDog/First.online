package com.ehl.itspark.data.entity;

import java.util.Date;

public class TradeRecordEntity {

	private Long id;
	private String flowNo;
	private String industryFlag;
	private Integer tradeType;
	private String tradeFlag;
	private Double payMoney;
	private Double paidMoney;
	private Double refund;
	private Double fee;
	private String couponID;
	private Integer result;
	private Integer payMode;
	private Date orderTime;
	private String orderTimeStr;
	private Date paymentTime;
	private String paymentStr;
	private String detail;
	private String serviceFlow;
	private String serviceEntityFlow;
	private String billID;
	
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
	public String getIndustryFlag() {
		return industryFlag;
	}
	public void setIndustryFlag(String industryFlag) {
		this.industryFlag = industryFlag;
	}
	public Integer getTradeType() {
		return tradeType;
	}
	public void setTradeType(Integer tradeType) {
		this.tradeType = tradeType;
	}
	public String getTradeFlag() {
		return tradeFlag;
	}
	public void setTradeFlag(String tradeFlag) {
		this.tradeFlag = tradeFlag;
	}
	public Double getPayMoney() {
		return payMoney;
	}
	public void setPayMoney(double payMoney) {
		this.payMoney = payMoney;
	}
	public Double getPaidMoney() {
		return paidMoney;
	}
	public void setPaidMoney(double paidMoney) {
		this.paidMoney = paidMoney;
	}
	public Double getRefund() {
		return refund;
	}
	public void setRefund(double refund) {
		this.refund = refund;
	}
	public Double getFee() {
		return fee;
	}
	public void setFee(double fee) {
		this.fee = fee;
	}
	public String getCouponID() {
		return couponID;
	}
	public void setCouponID(String couponID) {
		this.couponID = couponID;
	}
	public Integer getResult() {
		return result;
	}
	public void setResult(Integer result) {
		this.result = result;
	}
	public Integer getPayMode() {
		return payMode;
	}
	public void setPayMode(Integer payMode) {
		this.payMode = payMode;
	}
	public Date getOrderTime() {
		return orderTime;
	}
	public void setOrderTime(Date orderTime) {
		this.orderTime = orderTime;
	}
	public Date getPaymentTime() {
		return paymentTime;
	}
	public void setPaymentTime(Date paymentTime) {
		this.paymentTime = paymentTime;
	}
	public String getDetail() {
		return detail;
	}
	public void setDetail(String detail) {
		this.detail = detail;
	}
	public String getServiceFlow() {
		return serviceFlow;
	}
	public void setServiceFlow(String serviceFlow) {
		this.serviceFlow = serviceFlow;
	}
	public String getServiceEntityFlow() {
		return serviceEntityFlow;
	}
	public void setServiceEntityFlow(String serviceEntityFlow) {
		this.serviceEntityFlow = serviceEntityFlow;
	}
	public String getOrderTimeStr() {
		return orderTimeStr;
	}
	public void setOrderTimeStr(String orderTimeStr) {
		this.orderTimeStr = orderTimeStr;
	}
	public String getPaymentStr() {
		return paymentStr;
	}
	public void setPaymentStr(String paymentStr) {
		this.paymentStr = paymentStr;
	}
	public String getBillID() {
		return billID;
	}
	public void setBillID(String billID) {
		this.billID = billID;
	}
	
}
