package com.first.test.model;

import java.io.Serializable;
import java.util.List;

import com.mongodb.client.model.geojson.Point;

public class GeoLocation implements Serializable {

	private static final long serialVersionUID = -3798358494892920827L;
	private String type;
	private List<Double> coordinates;
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = "Point";
	}
	public List<Double> getCoordinates() {
		return coordinates;
	}
	public void setCoordinates(List<Double> coordinates) {
		this.coordinates = coordinates;
	}
	
	
	
}
