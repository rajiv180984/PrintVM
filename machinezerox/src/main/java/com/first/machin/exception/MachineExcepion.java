package com.first.machin.exception;

public class MachineExcepion extends RuntimeException {
	private String msg;
	private int code;
	public MachineExcepion(String msg, int code) {
		super();
		this.msg = msg;
		this.code = code;
	}
	
	
	public MachineExcepion(String msg) {
		super();
		this.msg = msg;
	}


	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	

}
