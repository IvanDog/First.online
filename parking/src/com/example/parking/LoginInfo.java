package com.example.parking;

public class LoginInfo {

	private CommonRequestHeader header;
	private String version;
	private String password;
	private String androidID;
	
	public CommonRequestHeader getheader() {
		return header;
	}
	public void setHeader(CommonRequestHeader header) {
		this.header = header;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getAndroidID() {
		return androidID;
	}
	public void setAndroidID(String androidID) {
		this.androidID = androidID;
	}
	@Override
	public String toString() {
		return "LoginInfo [header=" + header + ", version=" + version + ", password="
				+ password + ", androidID=" + androidID + "]";
	}
}
