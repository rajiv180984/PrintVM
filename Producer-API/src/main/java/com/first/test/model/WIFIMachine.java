package com.first.test.model;

import java.io.Serializable;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Document(collection="QRCodeDetails")
@JsonInclude(Include.NON_NULL)
@JsonAutoDetect
public class WIFIMachine implements Serializable{

	private static final long serialVersionUID = 1257962666410461013L;
	// It is ENCRYPTED Machine SSID
	private String machineName;
	// Machine encryped pwd and Same will be used for SSID, PWD encryption
	private String machineKey;
	// SSID
	private String ssid;
	// wifi password
	private String wifipwd;
	
	@Indexed(unique=true)
	private String machineId;
	// Not required to persist
	private String jobUrlforUpload;
	// enrpted reques deatail printJobId@@Email@@ etc
	private String publicCertKey;
	
	private String qrCodeFilePath;
	private String encryptedWifiSSID;
	@Indexed
	private String encryptedWifiPassword;
	
	// HIde This key
	private String machineSecreteKeyForEncrypion;
	
	private String key;
	
	private String wifiIPAdrress;
	
	public String getMachineName() {
		return machineName;
	}
	public void setMachineName(String machineName) {
		this.machineName = machineName;
	}
	public String getSsid() {
		return ssid;
	}
	public void setSsid(String ssid) {
		this.ssid = ssid;
	}

	public String getMachineId() {
		return machineId;
	}
	public void setMachineId(String machineId) {
		this.machineId = machineId;
	}
	public String getJobUrlforUpload() {
		return jobUrlforUpload;
	}
	public void setJobUrlforUpload(String jobUrlforUpload) {
		this.jobUrlforUpload = jobUrlforUpload;
	}
	public String getPublicCertKey() {
		return publicCertKey;
	}
	public void setPublicCertKey(String publicCertKey) {
		this.publicCertKey = publicCertKey;
	}
	public String getMachineKey() {
		return machineKey;
	}
	public void setMachineKey(String machineKey) {
		this.machineKey = machineKey;
	}
	public String getQrCodeFilePath() {
		return qrCodeFilePath;
	}
	public void setQrCodeFilePath(String qrCodeFilePath) {
		this.qrCodeFilePath = qrCodeFilePath;
	}
	public String getMachineSecreteKeyForEncrypion() {
		return machineSecreteKeyForEncrypion;
	}
	public void setMachineSecreteKeyForEncrypion(String machineSecreteKeyForEncrypion) {
		this.machineSecreteKeyForEncrypion = machineSecreteKeyForEncrypion;
	}
	public String getWifipwd() {
		return wifipwd;
	}
	public void setWifipwd(String wifipwd) {
		this.wifipwd = wifipwd;
	}
	
	
	public String getEncryptedWifiSSID() {
		return encryptedWifiSSID;
	}
	public void setEncryptedWifiSSID(String encryptedWifiSSID) {
		this.encryptedWifiSSID = encryptedWifiSSID;
	}
	public String getEncryptedWifiPassword() {
		return encryptedWifiPassword;
	}
	public void setEncryptedWifiPassword(String encryptedWifiPassword) {
		this.encryptedWifiPassword = encryptedWifiPassword;
	}
	
	
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	
	
	public String getWifiIPAdrress() {
		return wifiIPAdrress;
	}
	public void setWifiIPAdrress(String wifiIPAdrress) {
		this.wifiIPAdrress = wifiIPAdrress;
	}
	@Override
	public String toString() {
		return "WIFIMachine [machineName=" + machineName + ", machineKey=" + machineKey + ", ssid=" + ssid
				+ ", wifipwd=" + wifipwd + ", machineId=" + machineId + ", jobUrlforUpload=" + jobUrlforUpload
				+ ", publicCertKey=" + publicCertKey + ", qrCodeFilePath=" + qrCodeFilePath + ", encryptedWifiSSID="
				+ encryptedWifiSSID + ", encryptedWifiPassword=" + encryptedWifiPassword
				+ ", machineSecreteKeyForEncrypion=" + machineSecreteKeyForEncrypion + ", key=" + key + "]";
	}
	
	

}
