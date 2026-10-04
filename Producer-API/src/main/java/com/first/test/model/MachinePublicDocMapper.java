package com.first.test.model;

import java.io.Serializable;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="MachinePublicDocMapper")
public class MachinePublicDocMapper implements Serializable{

	private static final long serialVersionUID = -4423353978180649732L;
	
	private String machineId;
	private String docId;
	private String S3URL;
	private String localUrl;
	private String isDeleted;
	public String getMachineId() {
		return machineId;
	}
	public void setMachineId(String machineId) {
		this.machineId = machineId;
	}
	public String getDocId() {
		return docId;
	}
	public void setDocId(String docId) {
		this.docId = docId;
	}
	public String getS3URL() {
		return S3URL;
	}
	public void setS3URL(String s3url) {
		S3URL = s3url;
	}
	public String getLocalUrl() {
		return localUrl;
	}
	public void setLocalUrl(String localUrl) {
		this.localUrl = localUrl;
	}
	public String getIsDeleted() {
		return isDeleted;
	}
	public void setIsDeleted(String isDeleted) {
		this.isDeleted = isDeleted;
	}
	

}
