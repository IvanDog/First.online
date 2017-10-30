package com.ehl.itspark.data.entity;

import java.util.Date;

public class ModifyCalendarEntity {

	private Long id;
	private Long persionId;
	private Date modifyTime;
	private Long oriShiftworkId;
	private Long newShiftworkId;
	private Date createTime;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getPersionId() {
		return persionId;
	}
	public void setPersionId(Long persionId) {
		this.persionId = persionId;
	}
	public Date getModifyTime() {
		return modifyTime;
	}
	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}
	public Long getOriShiftworkId() {
		return oriShiftworkId;
	}
	public void setOriShiftworkId(Long oriShiftworkId) {
		this.oriShiftworkId = oriShiftworkId;
	}
	public Long getNewShiftworkId() {
		return newShiftworkId;
	}
	public void setNewShiftworkId(Long newShiftworkId) {
		this.newShiftworkId = newShiftworkId;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
}
