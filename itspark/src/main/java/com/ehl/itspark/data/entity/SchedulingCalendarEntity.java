package com.ehl.itspark.data.entity;

public class SchedulingCalendarEntity {

	private Long persionId;
	private String persionName;
	private SchedulingEntity schedulingEntity;
	
	public Long getPersionId() {
		return persionId;
	}
	public void setPersionId(Long persionId) {
		this.persionId = persionId;
	}
	public String getPersionName() {
		return persionName;
	}
	public void setPersionName(String persionName) {
		this.persionName = persionName;
	}
	public SchedulingEntity getSchedulingEntity() {
		return schedulingEntity;
	}
	public void setSchedulingEntity(SchedulingEntity schedulingEntity) {
		this.schedulingEntity = schedulingEntity;
	}
	
}
