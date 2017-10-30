package com.ehl.itspark.info;

import com.ehl.itspark.common.CommonRequestHeader;

public class SetNickNameInfo {
	private CommonRequestHeader header;
	private String nickName;
	
	public CommonRequestHeader getheader() {
		return header;
	}
	public void setHeader(CommonRequestHeader header) {
		this.header = header;
	}
	
	public String getNickName(){
		return nickName;
	}
	public void setNickName(String nickName){
		this.nickName = nickName;
	}
}
