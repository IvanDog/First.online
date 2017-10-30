package com.ehl.itspark.data.entity;

public class PersonAccessEntity {

	private long id;
	private String no;
	private String name;
	private String pword;
	private boolean hasSet;
	
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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPword() {
		return pword;
	}
	public void setPword(String pword) {
		this.pword = pword;
	}
	public boolean isHasSet() {
		return hasSet;
	}
	public void setHasSet(boolean hasSet) {
		this.hasSet = hasSet;
	}
	
}
