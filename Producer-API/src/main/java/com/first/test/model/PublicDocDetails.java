package com.first.test.model;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection="PublicDocDetails")
public class PublicDocDetails implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2192217220286218478L;
	private String machineId;
	private String docId;
	private List<String> machineList;
	private String category;
	private String SubCategory;
	private String Name;
	private String desc;
	private String localpath;
	@Indexed(unique=true)
	private String s3path;
	private boolean isPublic;
    private String ownerEmailId;
    private String ownerMobileNo;
	
	public String getDocId() {
		return docId;
	}

	public void setDocId(String docId) {
		this.docId = docId;
	}

	public String getMachineId() {
		return machineId;
	}

	public void setMachineId(String machineId) {
		this.machineId = machineId;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getSubCategory() {
		return SubCategory;
	}

	public void setSubCategory(String subCategory) {
		SubCategory = subCategory;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getLocalpath() {
		return localpath;
	}

	public void setLocalpath(String localpath) {
		this.localpath = localpath;
	}

	public String getS3path() {
		return s3path;
	}

	public void setS3path(String s3path) {
		this.s3path = s3path;
	}

	public List<String> getMachineList() {
		return machineList;
	}

	public void setMachineList(List<String> machineList) {
		this.machineList = machineList;
	}

	public boolean isPublic() {
		return isPublic;
	}

	public void setPublic(boolean isPublic) {
		this.isPublic = isPublic;
	}

	
	public String getOwnerEmailId() {
		return ownerEmailId;
	}

	public void setOwnerEmailId(String ownerEmailId) {
		this.ownerEmailId = ownerEmailId;
	}

	public String getOwnerMobileNo() {
		return ownerMobileNo;
	}

	public void setOwnerMobileNo(String ownerMobileNo) {
		this.ownerMobileNo = ownerMobileNo;
	}

	@Override
	public String toString() {
		return "PublicDocDetails [machineId=" + machineId + ", docId=" + docId + ", machineList=" + machineList
				+ ", category=" + category + ", SubCategory=" + SubCategory + ", Name=" + Name + ", desc=" + desc
				+ ", localpath=" + localpath + ", s3path=" + s3path + ", isPublic=" + isPublic + "]";
	}

		
	

}
