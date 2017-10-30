package com.ehl.itspark.info;

import com.ehl.itspark.common.CommonRequestHeader;

public class QueryBalanceInfo {

	private CommonRequestHeader header;
	
	public CommonRequestHeader getheader() {
		return header;
	}
	public void setHeader(CommonRequestHeader header) {
		this.header = header;
	}

	@Override
	public String toString() {
		return "PaymentInfo [header=" + header + "]";
	}
}

