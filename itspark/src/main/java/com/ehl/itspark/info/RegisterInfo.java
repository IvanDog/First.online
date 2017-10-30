package com.ehl.itspark.info;

import com.ehl.itspark.common.CommonRequestHeader;

public class RegisterInfo {
	private CommonRequestHeader header;
	private String registerType;
	private String password;
	
	public CommonRequestHeader getheader() {
		return header;
	}
	public void setHeader(CommonRequestHeader header) {
		this.header = header;
	}
	public String getRegisterType() {
		return registerType;
	}
	public void setRegisterType(String registerType) {
		this.registerType = registerType;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	@Override
	public String toString() {
		return "RegisterInfo [header=" + header.toString() + ", registerType=" + registerType + ", password="
				+ password + "]";
	}
}
