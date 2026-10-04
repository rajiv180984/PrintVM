package com.first.test.model;


import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Document(collection = "ShareDocForMachineByProfessionals")
@JsonInclude(Include.NON_NULL)
@JsonAutoDetect
public class ShareDocForMachineByProfessionals implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6753553064148001555L;
	private String docOwnerMobileNo;
	@Indexed
	private String docOwnerEmailId;
	private String machineId;
	@NotNull
	@NotEmpty
	private String docOwenerName;
	private String bucketName;
	@NotNull
	@NotEmpty
	private String machineIds;
	
	private String mobileNoList;
	private String securecode; // if( null means public doc)
	@NotNull
	@NotEmpty
	private String docTitle;
	private String s3Path;
	// Presigned Path for download
	private String fullDownloadPath;
	@NotNull
	@NotEmpty
	private String category;
	private String subCategory;
	private String scopeOfDoc;
	private Date expiration;
	private Date createdOn;
	private boolean isPublic;

	public String getDocOwnerMobileNo() {
		return docOwnerMobileNo;
	}

	public void setDocOwnerMobileNo(String mobileNoOfsharedBy) {
		this.docOwnerMobileNo = mobileNoOfsharedBy;
	}

	
	
	public String getDocOwnerEmailId() {
		return docOwnerEmailId;
	}

	public void setDocOwnerEmailId(String docOwnerEmailId) {
		this.docOwnerEmailId = docOwnerEmailId;
	}

	public String getDocOwenerName() {
		return docOwenerName;
	}

	public void setDocOwenerName(String docOwenerName) {
		this.docOwenerName = docOwenerName;
	}

	public String getBucketName() {
		return bucketName;
	}

	public void setBucketName(String bucketName) {
		this.bucketName = bucketName;
	}

	public String getMachineId() {
		return machineId;
	}

	public void setMachineId(String machineId) {
		this.machineId = machineId;
	}

	public String getMobileNoList() {
		return mobileNoList;
	}

	public void setMobileNoList(String mobileNoList) {
		this.mobileNoList = mobileNoList;
	}

	public String getSecurecode() {
		return securecode;
	}

	public void setSecurecode(String securecode) {
		this.securecode = securecode;
	}

	public String getDocTitle() {
		return docTitle;
	}

	public void setDocTitle(String docTitle) {
		this.docTitle = docTitle;
	}
	

	public String getS3Path() {
		return s3Path;
	}

	public void setS3Path(String s3Path) {
		this.s3Path = s3Path;
	}

	public String getScopeOfDoc() {
		return scopeOfDoc;
	}

	public void setScopeOfDoc(String scopeOfDoc) {
		this.scopeOfDoc = scopeOfDoc;
	}

	public Date getExpiration() {
		return expiration;
	}

	public void setExpiration(Date expiration) {
		if (expiration == null) {

			Date now = new Date();
			now.setTime(now.getTime() + 2592000000l);
			this.expiration = now;
		} else {
			this.expiration = expiration;
		}

	}

	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	
	public boolean isPublic() {
		return isPublic;
	}

	public void setPublic(boolean isPublic) {
		this.isPublic = isPublic;
	}

	
	public String getFullDownloadPath() {
		return fullDownloadPath;
	}

	public void setFullDownloadPath(String fullDownloadPath) {
		this.fullDownloadPath = fullDownloadPath;
	}
	

	
	public String getMachineIds() {
		return machineIds;
	}

	public void setMachineIds(String machineIds) {
		this.machineIds = machineIds;
	}

	public String getSubCategory() {
		return subCategory;
	}

	public void setSubCategory(String subCategory) {
		this.subCategory = subCategory;
	}

	@Override
	public String toString() {
		return "ShareDocForMachineByProfessionals [docOwnerMobileNo=" + docOwnerMobileNo + ", docOwnerEmailId="
				+ docOwnerEmailId + ", machineId=" + machineId + ", docOwenerName=" + docOwenerName + ", bucketName="
				+ bucketName + ", machineIds=" + machineIds + ", mobileNoList=" + mobileNoList + ", securecode="
				+ securecode + ", docTitle=" + docTitle + ", s3Path=" + s3Path + ", fullDownloadPath="
				+ fullDownloadPath + ", category=" + category + ", subCategory=" + subCategory + ", scopeOfDoc="
				+ scopeOfDoc + ", expiration=" + expiration + ", createdOn=" + createdOn + ", isPublic=" + isPublic
				+ "]";
	}


	
	

}
