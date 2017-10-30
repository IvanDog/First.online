package com.ehl.itspark.common;

import java.util.List;
import java.util.Map;

public class PageDTO<T> {

	private long rowSize;

	private List<T> data;

	private Map<String, Object> otherData;

	private int pageNum = 1;

	private int pageSize = 10;

	private long start;

	private long end;
	
	private int pageCount;
	

	public int getPageCount() {
		return pageCount;
	}

	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}

	public PageDTO() {
	}

	public PageDTO(Integer pageIndex, Integer pageSize) {
		if (pageIndex == null || pageIndex <= 0) {
			pageIndex = 1;
		}
		if (pageSize == null || pageSize <= 0) {
			pageSize = 10;
		}
		if (20 < pageSize.intValue()) {
			pageSize = 20;
		}
		long first = (pageIndex - 1) * pageSize;
		this.pageNum = pageIndex.intValue();
		this.pageSize = pageSize.intValue();
		if (first < 0) {
			first = 0;
		}
		this.start = first;
		this.end = pageIndex * pageSize;
	}

	public long getRowSize() {
		return rowSize;
	}

	public void setRowSize(long rowSize) {
		this.rowSize = rowSize;
	}

	public List<T> getData() {
		return data;
	}

	public void setData(List<T> data) {
		this.data = data;
	}

	public int getPageNum() {
		return pageNum;
	}

	public void setPageNum(int pageIndex) {
		this.pageNum = pageIndex;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public long getStart() {
		return start;
	}

	public void setStart(long start) {
		this.start = start;
	}

	public long getEnd() {
		return end;
	}

	public void setEnd(long end) {
		this.end = end;
	}

	public Map<String, Object> getOtherData() {
		return otherData;
	}

	public void setOtherData(Map<String, Object> otherData) {
		this.otherData = otherData;
	}

}
