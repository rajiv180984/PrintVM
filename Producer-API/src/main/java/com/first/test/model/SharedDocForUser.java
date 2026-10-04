package com.first.test.model;

import java.io.Serializable;

/*		
 * If doc is shared with some users. Doc Receiver can go to machine and initiate print if machine is not belongs to partner
 */
public class SharedDocForUser implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8706103050456698962L;
	private String mobileNodocSharedBy;
	private String mobileNoDocSharedFor;
	private String code;
	private String docURL;
	public String getMobileNodocSharedBy() {
		return mobileNodocSharedBy;
	}
	public void setMobileNodocSharedBy(String mobileNodocSharedBy) {
		this.mobileNodocSharedBy = mobileNodocSharedBy;
	}
	public String getMobileNoDocSharedFor() {
		return mobileNoDocSharedFor;
	}
	public void setMobileNoDocSharedFor(String mobileNoDocSharedFor) {
		this.mobileNoDocSharedFor = mobileNoDocSharedFor;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getDocURL() {
		return docURL;
	}
	public void setDocURL(String docURL) {
		this.docURL = docURL;
	}
	
	
}
