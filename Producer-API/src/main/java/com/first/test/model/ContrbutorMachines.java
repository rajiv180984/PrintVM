package com.first.test.model;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="ContrbutorMachines")
public class ContrbutorMachines implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6364140901431465855L;

	private List<String> machineIds;
	private String custId;
	private String emailId;
	public List<String> getMachineIds() {
		return machineIds;
	}
	
	public void setMachineIds(List<String> machineIds) {
		this.machineIds = machineIds;
	}
	public String getCustId() {
		return custId;
	}
	public void setCustId(String custId) {
		this.custId = custId;
	}
	public String getEmailId() {
		return emailId;
	}
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
	
	
}
