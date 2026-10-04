package com.first.test.model;

import java.io.Serializable;

public class MySubject extends LearningDomain implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2548342407854359016L;
	private int id;
	private String name;
	private String type;
	private String teacherId;
	private String teacherName;

	// private Perfromance performance;
	
	
		
	public MySubject(int id, String name, String type, String teacherId, String teacherName) {
		super();
		this.id = id;
		this.name = name;
		this.type = type;
		this.teacherId = teacherId;
		this.teacherName = teacherName;
	}


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getType() {
		return type;
	}


	public void setType(String type) {
		this.type = type;
	}


	public String getTeacherId() {
		return teacherId;
	}

	public void setTeacherId(String teacherId) {
		this.teacherId = teacherId;
	}

	public String getTeacherName() {
		return teacherName;
	}

	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}

}
