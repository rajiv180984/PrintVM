package com.first.test.service;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.first.test.model.LearningDomain;
import com.first.test.model.MySubject;

@JsonInclude(Include.NON_NULL)
@JsonAutoDetect
public class LearningVO extends LearningDomain implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 558242735490427060L;
	private String noticeId;
	private String classId;
	private String className;
	private String schoolId;
	private List<MySubject> subjects;
	public String getNoticeId() {
		return noticeId;
	}
	public void setNoticeId(String noticeId) {
		this.noticeId = noticeId;
	}
	public String getClassId() {
		return classId;
	}
	public void setClassId(String classId) {
		this.classId = classId;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public String getSchoolId() {
		return schoolId;
	}
	public void setSchoolId(String schoolId) {
		this.schoolId = schoolId;
	}
	public List<MySubject> getSubjects() {
		return subjects;
	}
	public void setSubjects(List<MySubject> subjects) {
		this.subjects = subjects;
	}
	
	
	

}
