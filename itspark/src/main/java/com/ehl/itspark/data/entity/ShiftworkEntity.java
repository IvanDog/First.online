package com.ehl.itspark.data.entity;

import java.util.Date;

public class ShiftworkEntity {

	/*
	 * 班次id
	 */
	private Long id;
	/*
	 * 班次名称
	 */
	private String name;
	/*
	 * 签到时间
	 */
	private Date signIn;
	private String signInStr;
	/*
	 * 允许迟到时长，分钟
	 */
	private int signInRemain;
	/*
	 * 签退时间
	 */
	private Date signOut;
	private String signOutStr;
	/*
	 * 允许早退时长，分钟
	 */
	private int signOutRemain;
	/*
	 * 签到地点经度
	 */
	private double signLon;
	/*
	 * 签到地点纬度
	 */
	private double signLat;
	/*
	 * 签到地点有效半径
	 */
	private int addrRadius;
	
	private String parkNo;
	private String parkName;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Date getSignIn() {
		return signIn;
	}
	public void setSignIn(Date signIn) {
		this.signIn = signIn;
	}
	public int getSignInRemain() {
		return signInRemain;
	}
	public void setSignInRemain(int signInRemain) {
		this.signInRemain = signInRemain;
	}
	public Date getSignOut() {
		return signOut;
	}
	public void setSignOut(Date signOut) {
		this.signOut = signOut;
	}
	public int getSignOutRemain() {
		return signOutRemain;
	}
	public void setSignOutRemain(int signOutRemain) {
		this.signOutRemain = signOutRemain;
	}
	public double getSignLon() {
		return signLon;
	}
	public void setSignLon(double signLon) {
		this.signLon = signLon;
	}
	public double getSignLat() {
		return signLat;
	}
	public void setSignLat(double signLat) {
		this.signLat = signLat;
	}
	public int getAddrRadius() {
		return addrRadius;
	}
	public void setAddrRadius(int addrRadius) {
		this.addrRadius = addrRadius;
	}
	public String getSignInStr() {
		return signInStr;
	}
	public void setSignInStr(String signInStr) {
		this.signInStr = signInStr;
	}
	public String getSignOutStr() {
		return signOutStr;
	}
	public void setSignOutStr(String signOutStr) {
		this.signOutStr = signOutStr;
	}
	public String getParkNo() {
		return parkNo;
	}
	public void setParkNo(String parkNo) {
		this.parkNo = parkNo;
	}
	public String getParkName() {
		return parkName;
	}
	public void setParkName(String parkName) {
		this.parkName = parkName;
	}
	
}