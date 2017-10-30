package com.ehl.itspark.data.entity;

public class DepartmentEntity {

	private  Long id;
	private String no;
	private String name;
	private String parentNo;
	private String parentName;
	
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
	
	public String getNo() {
		return no;
	}
	public void setNo(String no) {
		this.no = no;
	}
	public String getParentNo() {
		return parentNo;
	}
	public void setParentNo(String parentNo) {
		this.parentNo = parentNo;
	}
	public String getParentName() {
		return parentName;
	}
	public void setParentName(String parentName) {
		this.parentName = parentName;
	}
	@Override
	public String toString() {
		return "DepartmentEntity [id=" + id + ", no=" + no + ", name=" + name + ", parentNo=" + parentNo
				+ ", parentName=" + parentName + "]";
	}
	
}
