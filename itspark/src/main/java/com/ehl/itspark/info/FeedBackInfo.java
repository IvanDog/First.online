package com.ehl.itspark.info;

import com.ehl.itspark.common.CommonRequestHeader;

public class FeedBackInfo {
	private CommonRequestHeader header;
	private String feedContent;
	
	public CommonRequestHeader getheader() {
		return header;
	}
	public void setHeader(CommonRequestHeader header) {
		this.header = header;
	}
	public String getFeedContent(){
		return feedContent;
	}
	public void setFeedContent(String feedContent){
		this.feedContent = feedContent;
	}
}
