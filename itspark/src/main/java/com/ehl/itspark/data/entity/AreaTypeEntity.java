package com.ehl.itspark.data.entity;

public class AreaTypeEntity {

	private  Long id;
	private String no;
	private String name;

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
	@Override
	public String toString() {
		return "AreaTypeEntity [id=" + id + ", no=" + no + ", name=" + name + "]";
	}
	
}
