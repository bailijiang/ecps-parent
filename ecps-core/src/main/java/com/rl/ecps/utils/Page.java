package com.rl.ecps.utils;

import java.util.List;

public class Page {
	
	private int pageNo = 1;
	
	private int pageSize = 5;
	
	//当前qc下的总记录数
	private int totalCount = 0;
	
	//当前qc下的总页数
	private int totalPage = 1;
	
	private int startNum = 0;
	
	private int endNum = 0;
	
	private List<?> list;

	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public int getTotalPage() {
		totalPage = totalCount / pageSize;
		if(totalCount == 0 || totalCount % pageSize != 0){
			totalPage++;
		}
		
		return totalPage;
	}

	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}

	public int getStartNum() {
		return (pageNo - 1) * pageSize;
	}

	public void setStartNum(int startNum) {
		this.startNum = startNum;
	}

	public int getEndNum() {
		return pageNo * pageSize + 1;
	}

	public void setEndNum(int endNum) {
		this.endNum = endNum;
	}

	public List<?> getList() {
		return list;
	}

	public void setList(List<?> list) {
		this.list = list;
	}
	
	
}
