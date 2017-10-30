package com.ehl.itspark.data.entity;

import java.util.Date;

public class ParkEntity {

	/**
	 * id
	 */
	private Long id;
	/**
	 * 编号
	 */
	private String no;
	/**
	 * 名称
	 */
	private String name;
	/**
	 * 经度
	 */
	private double lon;
	/**
	 * 纬度
	 */
	private double lat;
	/**
	 * 总车位
	 */
	private int totalSpace;
	/**
	 * 区划
	 */
	private AreaEntity area;
	/**
	 * 区域类型
	 */
	private AreaTypeEntity areaType;
	/**
	 * 机构
	 */
	private DepartmentEntity department;
	/**
	 * 剩余车位
	 */
	private int remainSpace;
	/**
	 * 车场logo
	 */
	private String picLogo;
	/**
	 * 认证状态
	 */
	private int certStatus;
	/**
	 * 商业状态
	 */
	private int businessStatus;
	/**
	 * 停车场类型：路内、路外
	 */
	private int type;
	/**
	 * 停车场结构：地上、地下、立体
	 */
	private int construct;
	/**
	 * 性质：小区、酒店
	 */
	private int nature;
	/**
	 * 管理方式：封闭、开放
	 */
	private int manageMode;
	/**
	 * 营业方式：专用、开放
	 */
	private int businessMode;
	/**
	 * 运营方式：收费、免费
	 */
	private int operateNature;
	/**
	 * 收费方式：人工、自动、人工+自动
	 */
	private int chargeMode;
	/**
	 * 支付方式：
	 */
	private String payMode;
	/**
	 * 结算方式：联网结算、场内结算
	 */
	private int settleMode;
	/**
	 * 车位上报方式
	 */
	private int lotTranMode;
	/**
	 * 营业时间
	 */
	private String businessTime;
	/**
	 * 费率编号
	 */
	private String chargeNo;
	/**
	 * 车位数据更新时间
	 */
	private Date updateTime;
	/**
	 * 地址
	 */
	private String address;
	
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
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
	public double getLon() {
		return lon;
	}
	public void setLon(double lon) {
		this.lon = lon;
	}
	public double getLat() {
		return lat;
	}
	public void setLat(double lat) {
		this.lat = lat;
	}
	public int getTotalSpace() {
		return totalSpace;
	}
	public void setTotalSpace(int totalSpace) {
		this.totalSpace = totalSpace;
	}
	public AreaEntity getArea() {
		return area;
	}
	public void setArea(AreaEntity area) {
		this.area = area;
	}
	public AreaTypeEntity getAreaType() {
		return areaType;
	}
	public void setAreaType(AreaTypeEntity areaType) {
		this.areaType = areaType;
	}
	public DepartmentEntity getDepartment() {
		return department;
	}
	public void setDepartment(DepartmentEntity department) {
		this.department = department;
	}
	public int getRemainSpace() {
		return remainSpace;
	}
	public void setRemainSpace(int remainSpace) {
		this.remainSpace = remainSpace;
	}
	public String getPicLogo() {
		return picLogo;
	}
	public void setPicLogo(String picLogo) {
		this.picLogo = picLogo;
	}
	public int getCertStatus() {
		return certStatus;
	}
	public void setCertStatus(int certStatus) {
		this.certStatus = certStatus;
	}
	public int getBusinessStatus() {
		return businessStatus;
	}
	public void setBusinessStatus(int businessStatus) {
		this.businessStatus = businessStatus;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getConstruct() {
		return construct;
	}
	public void setConstruct(int construct) {
		this.construct = construct;
	}
	public int getNature() {
		return nature;
	}
	public void setNature(int nature) {
		this.nature = nature;
	}
	public int getManageMode() {
		return manageMode;
	}
	public void setManageMode(int manageMode) {
		this.manageMode = manageMode;
	}
	public int getBusinessMode() {
		return businessMode;
	}
	public void setBusinessMode(int businessMode) {
		this.businessMode = businessMode;
	}
	public int getOperateNature() {
		return operateNature;
	}
	public void setOperateNature(int operateNature) {
		this.operateNature = operateNature;
	}
	public int getChargeMode() {
		return chargeMode;
	}
	public void setChargeMode(int chargeMode) {
		this.chargeMode = chargeMode;
	}
	public String getPayMode() {
		return payMode;
	}
	public void setPayMode(String payMode) {
		this.payMode = payMode;
	}
	public int getSettleMode() {
		return settleMode;
	}
	public void setSettleMode(int settleMode) {
		this.settleMode = settleMode;
	}
	public int getLotTranMode() {
		return lotTranMode;
	}
	public void setLotTranMode(int lotTranMode) {
		this.lotTranMode = lotTranMode;
	}
	public String getBusinessTime() {
		return businessTime;
	}
	public void setBusinessTime(String businessTime) {
		this.businessTime = businessTime;
	}
	public String getChargeNo() {
		return chargeNo;
	}
	public void setChargeNo(String chargeNo) {
		this.chargeNo = chargeNo;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	@Override
	public String toString() {
		return "ParkEntity [id=" + id + ", no=" + no + ", name=" + name + ", lon=" + lon + ", lat=" + lat
				+ ", totalSpace=" + totalSpace + ", area=" + area.toString() + ", areaType=" + areaType.toString() + ", department="
				+ department.toString() + ", remainSpace=" + remainSpace + ", picLogo=" + picLogo + ", certStatus=" + certStatus
				+ ", businessStatus=" + businessStatus + ", type=" + type + ", construct=" + construct + ", nature="
				+ nature + ", manageMode=" + manageMode + ", businessMode=" + businessMode + ", operateNature="
				+ operateNature + ", chargeMode=" + chargeMode + ", payMode=" + payMode + ", settleMode=" + settleMode
				+ ", lotTranMode=" + lotTranMode + ", businessTime=" + businessTime + ", chargeNo=" + chargeNo
				+ ", updateTime=" + updateTime + ", address=" + address + "]";
	}
	
	
	
}
