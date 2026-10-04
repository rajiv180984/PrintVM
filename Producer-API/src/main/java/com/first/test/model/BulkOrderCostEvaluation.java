package com.first.test.model;

import java.io.Serializable;

public class BulkOrderCostEvaluation implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3485804135853871835L;
	private float printCost;
	private float coverPrintCost;
	private float bindingCost;
	// totalPrintBindCost = printCost + coverPrintCost + bindingCost
	private float totalPrintBindCost;
	private float gst;
	private String gstPerchent;
	private float shippingCost;
    // total totalPrintCost + shippingCost + gst
	private float orderCost;
    
	public float getPrintCost() {
		return printCost;
	}
	public void setPrintCost(float printCost) {
		this.printCost = printCost;
	}
	public float getShippingCost() {
		return shippingCost;
	}
	public void setShippingCost(float shippingCost) {
		this.shippingCost = shippingCost;
	}
	public float getCoverPrintCost() {
		return coverPrintCost;
	}
	public void setCoverPrintCost(float coverPrintCost) {
		this.coverPrintCost = coverPrintCost;
	}
	public float getBindingCost() {
		return bindingCost;
	}
	public void setBindingCost(float bindingCost) {
		this.bindingCost = bindingCost;
	}
	
	public float getTotalPrintBindCost() {
		return totalPrintBindCost;
	}
	public void setTotalPrintBindCost(float totalPrintBindCost) {
		this.totalPrintBindCost = totalPrintBindCost;
	}
	public float getGst() {
		return gst;
	}
	public void setGst(float gst) {
		this.gst = gst;
	}
	public float getOrderCost() {
		return orderCost;
	}
	public void setOrderCost(float orderCost) {
		this.orderCost = orderCost;
	}
	public String getGstPerchent() {
		return "2.5%";
	}
	public void setGstPerchent(String gstPerchent) {
		this.gstPerchent = "2.5%";
	}
	
	
	
}
