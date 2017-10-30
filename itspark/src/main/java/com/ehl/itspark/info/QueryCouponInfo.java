package com.ehl.itspark.info;

import com.ehl.itspark.common.CommonRequestHeader;

public class QueryCouponInfo {

	private CommonRequestHeader header;
	private String expensePrimary;
	
	public CommonRequestHeader getheader() {
		return header;
	}
	public void setHeader(CommonRequestHeader header) {
		this.header = header;
	}
	
	public String getExpensePrimary() {
		return expensePrimary;
	}
	public void setExpensePrimary(String expensePrimary) {
		this.expensePrimary = expensePrimary;
	}

	@Override
	public String toString() {
		return "QueryCouponInfo [header=" + header + ", expensePrimary=" + expensePrimary + "]";
	}
}

