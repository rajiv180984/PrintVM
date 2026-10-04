package com.first.test.model;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Document(collection = "machineDetails")
@JsonInclude(Include.NON_NULL)
@JsonAutoDetect
public class MachineDetails implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4525460053262020239L;
	@NotNull(message="Machine Id missine")
	@Indexed(unique=true)
	private String machineId;
	private String machineName;
	private String machineAddress;
	private String cityName;
	@NotNull(message="geolocation Missing ")
	private GeoLocation geoLocation;
	private String respberryDeviceId;
	private String partnerId;
	private String printerId;
	private String printerType;
	private String scannerId;
	private String copierId;
	@NotNull(message="careTakerId")
	private String careTakerId;
	// 
	private String businessType;
	private String QRcodeEnable;
	
	@NotNull(message="QRCodeId")
	private String qrCodeId;
	private String activeStatus;
	
	private long registerOn;
	private long modifiedOn;
	// Partner machine or Zerox Machine
	// if machine is belongs to zerox the caretaker id or It is partner Id
	@NotNull(message="machineBelongsTo")
	private String machineBelongsTo;
	
	public String getMachineId() {
		return machineId;
	}
	public void setMachineId(String machineId) {
		this.machineId = machineId;
	}
	public String getMachineName() {
		return machineName;
	}
	public void setMachineName(String machineName) {
		this.machineName = machineName;
	}
	public String getMachineAddress() {
		return machineAddress;
	}
	public void setMachineAddress(String machineAddress) {
		this.machineAddress = machineAddress;
	}
	
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	public GeoLocation getGeoLocation() {
		return geoLocation;
	}
	public void setGeoLocation(GeoLocation geoLocation) {
		this.geoLocation = geoLocation;
	}
	public String getRespberryDeviceId() {
		return respberryDeviceId;
	}
	public void setRespberryDeviceId(String respberryDeviceId) {
		this.respberryDeviceId = respberryDeviceId;
	}
	
	public String getPartnerId() {
		return partnerId;
	}
	public void setPartnerId(String partnerId) {
		this.partnerId = partnerId;
	}
	public String getPrinterId() {
		return printerId;
	}
	public void setPrinterId(String printerId) {
		this.printerId = printerId;
	}
	public String getPrinterType() {
		return printerType;
	}
	public void setPrinterType(String printerType) {
		this.printerType = printerType;
	}
	public String getScannerId() {
		return scannerId;
	}
	public void setScannerId(String scannerId) {
		this.scannerId = scannerId;
	}
	public String getCopierId() {
		return copierId;
	}
	public void setCopierId(String copierId) {
		this.copierId = copierId;
	}
	public String getCareTakerId() {
		return careTakerId;
	}
	public void setCareTakerId(String careTakerId) {
		this.careTakerId = careTakerId;
	}
	public String getBusinessType() {
		return businessType;
	}
	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}
	public String getQRcodeEnable() {
		return QRcodeEnable;
	}
	public void setQRcodeEnable(String qRcodeEnable) {
		QRcodeEnable = qRcodeEnable;
	}
	public String getActiveStatus() {
		return activeStatus;
	}
	public void setActiveStatus(String activeStatus) {
		this.activeStatus = activeStatus;
	}
	
	
	public long getRegisterOn() {
		return registerOn;
	}
	public void setRegisterOn(long registerOn) {
		this.registerOn = System.currentTimeMillis();
	}
	public long getModifiedOn() {
		return modifiedOn;
	}
	public void setModifiedOn(long modifiedOn) {
		this.modifiedOn = modifiedOn;
	}
	public String getMachineBelongsTo() {
		return machineBelongsTo;
	}
	public void setMachineBelongsTo(String machineBelongsTo) {
		this.machineBelongsTo = machineBelongsTo;
	}
	
	
	public String getQrCodeId() {
		return qrCodeId;
	}
	public void setQrCodeId(String qrCodeId) {
		this.qrCodeId = qrCodeId;
	}
	@Override
	public String toString() {
		return "MachineDetails [machineId=" + machineId + ", machineName=" + machineName + ", machineAddress="
				+ machineAddress + ", cityName=" + cityName + ", geoLocation=" + geoLocation + ", respberryDeviceId="
				+ respberryDeviceId + ", partnerId=" + partnerId + ", printerId=" + printerId + ", printerType="
				+ printerType + ", scannerId=" + scannerId + ", copierId=" + copierId + ", careTakerId=" + careTakerId
				+ ", businessType=" + businessType + ", QRcodeEnable=" + QRcodeEnable + ", QRCodeId=" + qrCodeId
				+ ", activeStatus=" + activeStatus + ", machineBelongsTo=" + machineBelongsTo + "]";
	}

	
}
