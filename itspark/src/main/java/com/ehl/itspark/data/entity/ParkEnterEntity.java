package com.ehl.itspark.data.entity;

import java.util.Date;

public class ParkEnterEntity {

	private Long id;
	private String flowNo;
	private String plate;
	private Date enterTime;
	private String parkNo;
	private byte[] img;
	private String parkLot;
	private String carType;
	private String parkType;
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
	public byte[] getImg() {
		return img;
	}
	public void setImg(byte[] img) {
		this.img = img;
	}
	public String getParkLot() {
		return parkLot;
	}
	public void setParkLot(String parkLot) {
		this.parkLot = parkLot;
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
	@Override
	public String toString(){
		return "ParkEnterEntity [plate=" + plate + ", enterTime=" + enterTime + ", carType="
				+ carType + ", parkType=" + parkType + "]";
	}
	
}
