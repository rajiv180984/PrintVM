package com.first.test.model;

import java.io.Serializable;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.sun.istack.NotNull;

// This Pojo Store and important for verification of 
//Print Request what payment calculate and deducted from Account

@Document(collection ="PrintCostPaymentRequest")
@JsonInclude(Include.NON_NULL)
@JsonAutoDetect
public class PrintCostPaymentRequest implements Serializable{

	private static final long serialVersionUID = -881606770947766454L;
	
	private double lat;
	private double longt;
	@NotNull
	@NotEmpty
	private String emailId;
	private String printJobId;
	private String orderId;
	private String enoughBalance;
	private float totalCost;
    private int numberOfPage;
	private String machineId;
	private String cityName;
    private String promocode;
    private int noOfCopy;
    private boolean isColor;
    //FIXME
    // 2-3,4-5/ ALL
    private String printExprn;
    private String status;
    
    private String certKey;
    
    
    public float getTotalCost() {
		return totalCost;
	}
	public void setTotalCost(float totalCost) {
		this.totalCost = totalCost;
	}
	public int getNumberOfPage() {
		return numberOfPage;
	}
	public void setNumberOfPage(int numberOfPage) {
		this.numberOfPage = numberOfPage;
	}
	public String getMachineId() {
		return machineId;
	}
	public void setMachineId(String machineId) {
		this.machineId = machineId;
	}
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	public String getPromocode() {
		return promocode;
	}
	public void setPromocode(String promocode) {
		this.promocode = promocode;
	}
	
	
	public boolean isColor() {
		return isColor;
	}
	public void setColor(boolean isColor) {
		this.isColor = isColor;
	}
	
	public double getLat() {
		return lat;
	}
	public void setLat(double lat) {
		this.lat = lat;
	}
	public double getLongt() {
		return longt;
	}
	public void setLongt(double longt) {
		this.longt = longt;
	}
	
	public String getEmailId() {
		return emailId;
	}
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
	public String getPrintJobId() {
		return printJobId;
	}
	public void setPrintJobId(String printJobId) {
		this.printJobId = printJobId;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getEnoughBalance() {
		return enoughBalance;
	}
	public void setEnoughBalance(String enoughBalance) {
		this.enoughBalance = enoughBalance;
	}
	
	
	public String getPrintExprn() {
		return printExprn;
	}
	public void setPrintExprn(String printExprn) {
		this.printExprn = printExprn;
	}
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	
	public int getNoOfCopy() {
		return noOfCopy;
	}
	public void setNoOfCopy(int noOfCopy) {
		this.noOfCopy = noOfCopy;
	}
	@Override
	public String toString() {
		return "PrintCostPaymentRequest [lat=" + lat + ", longt=" + longt + ", emailId=" + emailId + ", printJobId="
				+ printJobId + ", orderId=" + orderId + ", enoughBalance=" + enoughBalance + ", totalCost=" + totalCost
				+ ", numberOfPage=" + numberOfPage + ", machineId=" + machineId + ", cityName=" + cityName
				+ ", promocode=" + promocode + ", isColor=" + isColor + ", printExprn=" + printExprn + ", status="
				+ status + "]";
	}
	
	
    
}
