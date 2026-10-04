package com.first.test.model;

import java.io.Serializable;

public class SecurePrint implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5551480571250382618L;
	private String docId;
	private String ownermobile;
	private String securitycode;
	private String shareMobileNo;
	public String getDocId() {
		return docId;
	}
	public void setDocId(String docId) {
		this.docId = docId;
	}
	public String getOwnermobile() {
		return ownermobile;
	}
	public void setOwnermobile(String ownermobile) {
		this.ownermobile = ownermobile;
	}
	public String getSecuritycode() {
		return securitycode;
	}
	public void setSecuritycode(String securitycode) {
		this.securitycode = securitycode;
	}
	public String getShareMobileNo() {
		return shareMobileNo;
	}
	public void setShareMobileNo(String shareMobileNo) {
		this.shareMobileNo = shareMobileNo;
	}
	@Override
	public String toString() {
		return "SecurePrint [docId=" + docId + ", ownermobile=" + ownermobile + ", securitycode=" + securitycode
				+ ", shareMobileNo=" + shareMobileNo + "]";
	}
	
	
}
