package com.first.test.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
@JsonAutoDetect
public class PrintProperties implements Serializable{

	private static final long serialVersionUID = 9180822443722569305L;
	//page Count
	private int pageCount;
	// Print expression 
	private String pexpn;

	private boolean color;
	
	//paper size
	private String pSize;
	
	private String layout;
	
	private String collate;
	
	public int getPageCount() {
		return pageCount;
	}

	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}

	public String getPexpn() {
		return pexpn;
	}

	public void setPexpn(String pexpn) {
		this.pexpn = pexpn;
	}

	public boolean isColor() {
		return color;
	}

	public void setColor(boolean color) {
		this.color = color;
	}

	public String getpSize() {
		return pSize;
	}

	public void setpSize(String pSize) {
		this.pSize = pSize;
	}

	public String getLayout() {
		return layout;
	}

	public void setLayout(String layout) {
		this.layout = layout;
	}

	public String getCollate() {
		return collate;
	}

	public void setCollate(String collate) {
		this.collate = collate;
	}
	
}
