package com.ehl.itspark.data.entity;

public class OwnerEntity {

	private Long id;
	private String no;
	private String name;
	private String phone;
	private String iden;
	private String pword;
	private String payPword;
	private byte[] ownerImg;
	private String firstLicense;
	private String secondLicense;
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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getIden() {
		return iden;
	}
	public void setIden(String iden) {
		this.iden = iden;
	}
	public String getPword() {
		return pword;
	}
	public void setPword(String pword) {
		this.pword = pword;
	}
	public String getPayPword() {
		return payPword;
	}
	public void setPayPword(String payPword) {
		this.payPword = payPword;
	}
	public byte[] getOwnerImg() {
		return ownerImg;
	}
	public void setOwnerImg(byte[] ownerImg) {
		this.ownerImg = ownerImg;
	}
	public void setFirstLicense(String firstLicense) {
		this.firstLicense = firstLicense;
	}
	public String getFirstLicense() {
		return firstLicense;
	}
	public void setSecondLicense(String secondLicense) {
		this.secondLicense = secondLicense;
	}
	public String getSecondLicense() {
		return secondLicense;
	}
}
