package com.first.test.model;

import java.io.Serializable;

public class NumberPageRange implements Serializable{

	private static final long serialVersionUID = -5335763272339240471L;
	
	private int start;
	private int end;
	public int getStart() {
		return start;
	}
	public void setStart(int start) {
		this.start = start;
	}
	public int getEnd() {
		return end;
	}
	public void setEnd(int end) {
		this.end = end;
	}

	
  
}
