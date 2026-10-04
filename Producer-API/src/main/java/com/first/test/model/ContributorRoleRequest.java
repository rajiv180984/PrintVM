package com.first.test.model;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.NotEmpty;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.sun.istack.NotNull;

@Document(collection="ContributorRoleRequest")
public class ContributorRoleRequest implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6807269560214555689L;
	
	@Indexed
	private String emailId;
	@NotNull
	@NotEmpty
	private String Education;
	@NotNull
	@NotEmpty
	private String profession;
	@NotNull
	@NotEmpty
	private String machineAddress;
	private String machineIds;
	private String approverEmailId;
	private Date requestDate;
	private Date approveDate;
	
	public String getEmailId() {
		return emailId;
	}
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
	public String getEducation() {
		return Education;
	}
	public void setEducation(String education) {
		Education = education;
	}
	public String getProfession() {
		return profession;
	}
	public void setProfession(String profession) {
		this.profession = profession;
	}
	public String getMachineAddress() {
		return machineAddress;
	}
	public void setMachineAddress(String machineAddress) {
		this.machineAddress = machineAddress;
	}
	public String getMachineIds() {
		return machineIds;
	}
	public void setMachineIds(String machineIds) {
		this.machineIds = machineIds;
	}
	public String getApproverEmailId() {
		return approverEmailId;
	}
	public void setApproverEmailId(String approverEmailId) {
		this.approverEmailId = approverEmailId;
	}
	public Date getRequestDate() {
		return requestDate;
	}
	public void setRequestDate(Date requestDate) {
		this.requestDate = requestDate;
	}
	public Date getApproveDate() {
		return approveDate;
	}
	public void setApproveDate(Date approveDate) {
		this.approveDate = approveDate;
	}


}
