package com.first.test.model;

import java.io.Serializable;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="CostDerivative")
public class CostDerivative implements Serializable{
	
	private static final long serialVersionUID = -8959693097161571764L;
	private float costPerPage;
	private NumberPageRange pageRange;
	
	private String machineId;
	
	private String cityName;

	public float getCostPerPage() {
		return costPerPage;
	}

	public void setCostPerPage(float costPerPage) {
		this.costPerPage = costPerPage;
	}

	public NumberPageRange getPageRange() {
		return pageRange;
	}

	public void setPageRange(NumberPageRange pageRange) {
		this.pageRange = pageRange;
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

	@Override
	public int hashCode() {
		
		return Integer.parseInt(machineId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CostDerivative other = (CostDerivative) obj;
		if (cityName == null) {
			if (other.cityName != null)
				return false;
		} else if (!cityName.equals(other.cityName))
			return false;
		if (Float.floatToIntBits(costPerPage) != Float.floatToIntBits(other.costPerPage))
			return false;
		if (machineId == null) {
			if (other.machineId != null)
				return false;
		} else if (!machineId.equals(other.machineId))
			return false;
		if (pageRange == null) {
			if (other.pageRange != null)
				return false;
		} else if (!pageRange.equals(other.pageRange))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "CostDerivative [costPerPage=" + costPerPage + ", pageRange=" + pageRange + ", machineId=" + machineId
				+ ", cityName=" + cityName + "]";
	}
	
	
	

}
