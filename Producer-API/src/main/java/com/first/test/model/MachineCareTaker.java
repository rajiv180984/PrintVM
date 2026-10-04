package com.first.test.model;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.lang.NonNull;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Document(collection = "machineCareTaker")
@JsonInclude(Include.NON_NULL)
@JsonAutoDetect
public class MachineCareTaker implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5376276517033248068L;
	@Indexed(unique=true)
	private String caretakerId;
	private String Name;
	@Indexed(unique=true)
	@NonNull
	private String mobile;
	private String mobile2;
	@NonNull
	private String email;
	private String address;
	@NonNull
	private String AdharCardNumber;
	private List<String> listOfMachine;
	@NonNull
	private String location;
	private String status;
	private String payment;
	private String IdDocPath;
	private long regsiterTimestamp;
	
	
	
	public String getCaretakerId() {
		return caretakerId;
	}
	public void setCaretakerId(String caretakerId) {
		this.caretakerId = caretakerId;
	}
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getMobile2() {
		return mobile2;
	}
	public void setMobile2(String mobile2) {
		this.mobile2 = mobile2;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getAdharCardNumber() {
		return AdharCardNumber;
	}
	public void setAdharCardNumber(String adharCardNumber) {
		AdharCardNumber = adharCardNumber;
	}
	
	
	public List<String> getListOfMachine() {
		return listOfMachine;
	}
	public void setListOfMachine(List<String> listOfMachine) {
		this.listOfMachine = listOfMachine;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getPayment() {
		return payment;
	}
	public void setPayment(String payment) {
		this.payment = payment;
	}
	public String getIdDocPath() {
		return IdDocPath;
	}
	public void setIdDocPath(String idDocPath) {
		IdDocPath = idDocPath;
	}
	public long getRegsiterTimestamp() {
		return regsiterTimestamp;
	}
	public void setRegsiterTimestamp(long regsiterTimestamp) {
		this.regsiterTimestamp = regsiterTimestamp;
	}
	
	
	
}
