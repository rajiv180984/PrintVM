package com.first.test.model;

import com.sun.istack.NotNull;

public class Binding {
	@NotNull
	private String bindingType;
	private String bindingNo;
	private String coverType;
	private Boolean covorPageColor;
	private String coverFile;
	public String getBindingType() {
		return bindingType;
	}
	public void setBindingType(String bindingType) {
		this.bindingType = bindingType;
	}
	public String getCoverType() {
		return coverType;
	}
	public void setCoverType(String coverType) {
		this.coverType = coverType;
	}
	public String getCoverFile() {
		return coverFile;
	}
	public void setCoverFile(String coverFile) {
		this.coverFile = coverFile;
	}
	public Boolean getCovorPageColor() {
		return covorPageColor;
	}
	public void setCovorPageColor(Boolean covorPageColor) {
		this.covorPageColor = covorPageColor;
	}
	public String getBindingNo() {
		return bindingNo;
	}
	public void setBindingNo(String bindingNo) {
		this.bindingNo = bindingNo;
	}
	
	
	
}
