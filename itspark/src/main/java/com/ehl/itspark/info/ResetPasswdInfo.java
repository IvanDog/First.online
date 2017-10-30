package com.ehl.itspark.info;

import com.ehl.itspark.common.CommonRequestHeader;

public class ResetPasswdInfo {
	private CommonRequestHeader header;
	private String password;
	private String newPassword;
	
	public CommonRequestHeader getheader() {
		return header;
	}
	public void setHeader(CommonRequestHeader header) {
		this.header = header;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getNewPassword() {
		return newPassword;
	}
	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}
}