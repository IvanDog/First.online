package com.ehl.itspark.data.entity;

public class AccountEntity {

	private Long id;
	private String accountNo;
	private Integer type;
	private double balance;
	private String ownerNo;

	private Integer coupon;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getAccountNo() {
		return accountNo;
	}
	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public double getBalance() {
		return balance;
	}
	public void setBalance(double balance) {
		this.balance = balance;
	}
	public String getOwnerNo() {
		return ownerNo;
	}
	public void setOwnerNo(String ownerNo) {
		this.ownerNo = ownerNo;
	}
	

	public Integer getCoupon() {
		return coupon;
	}
	public void setCoupon(Integer coupon) {
		this.coupon = coupon;
	}
	@Override
	public String toString(){
		return " accountNo:" + accountNo + " balance" + balance + " ownerNo:" + ownerNo + " coupon:" + coupon;
	}
}
