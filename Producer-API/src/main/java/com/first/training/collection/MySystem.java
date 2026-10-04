package com.first.training.collection;

public class MySystem {
	private int id;
	private String hostname;
	private String spec;
	private String Integer;

	public MySystem(int id, String hostname, String spec) {
		super();
		this.id = id;
		this.hostname = hostname;
		this.spec = spec;
	}
	

	@Override
	public String toString() {
		return "MySystem [id=" + id + ", hostname=" + hostname + ", spec=" + spec + "]";
	}

	
}
