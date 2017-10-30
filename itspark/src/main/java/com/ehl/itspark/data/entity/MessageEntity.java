package com.ehl.itspark.data.entity;

import java.util.Date;

public class MessageEntity {

	private Long id;
	private String messageID;
	private String messageTitle;
	private String messageDetail;
	private String messageAbstract;
	private Integer messageState;
	private String messageOwner;
	private Integer messageOwnerType;
	private Date createTime;

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getMessageID() {
		return messageID;
	}
	public void setMessageID(String messageID) {
		this.messageID = messageID;
	}
	
	public String getMessageTitle() {
		return messageTitle;
	}
	public void setMessageTitle(String messageTitle) {
		this.messageTitle = messageTitle;
	}
	
	public String getMessageDetail(){
		return messageDetail;
	}
	public void setMessageDetail(String messageDetail) {
		this.messageDetail = messageDetail;
	}

	public String getMessageAbstract() {
		return messageAbstract;
	}
	public void setMessageAbstract(String messageAbstract) {
		this.messageAbstract = messageAbstract;
	}
	
	public  Integer getMessageState() {
		return messageState;
	}
	public void setMessageState(Integer messageState) {
		this.messageState = messageState;
	}
	
	public String getMessageOwner() {
		return messageOwner;
	}
	public void setMessageOwner(String messageOwner) {
		this.messageOwner = messageOwner;
	}
	
	public  Integer getMessageOwnerType() {
		return messageOwnerType;
	}
	public void setMessageOwnerType(Integer messageOwnerType) {
		this.messageOwnerType = messageOwnerType;
	}
	
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
}
