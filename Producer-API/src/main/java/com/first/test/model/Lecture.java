package com.first.test.model;

import java.io.Serializable;
import java.util.List;

public class Lecture implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6420064443469375239L;
    private String lectureId;
	private String subjectId;
	private String teacherId;
	private String TopicName;
	private String videoPath;
	private String lectureDate;
	private List<String> notesPath;
	private List<String> recomondationPath;
	
	
	
	public Lecture(String lectureId, String subjectId, String teacherId, String topicName, String videoPath,
			String lectureDate, List<String> notesPath, List<String> recomondationPath) {
		super();
		this.lectureId = lectureId;
		this.subjectId = subjectId;
		this.teacherId = teacherId;
		TopicName = topicName;
		this.videoPath = videoPath;
		this.lectureDate = lectureDate;
		this.notesPath = notesPath;
		this.recomondationPath = recomondationPath;
	}
	public String getSubjectId() {
		return subjectId;
	}
	public void setSubjectId(String subjectId) {
		this.subjectId = subjectId;
	}
	public String getTeacherId() {
		return teacherId;
	}
	public void setTeacherId(String teacherId) {
		this.teacherId = teacherId;
	}
	public String getTopicName() {
		return TopicName;
	}
	public void setTopicName(String topicName) {
		TopicName = topicName;
	}
	public String getVideoPath() {
		return videoPath;
	}
	public void setVideoPath(String videoPath) {
		this.videoPath = videoPath;
	}
	public List<String> getNotesPath() {
		return notesPath;
	}
	public void setNotesPath(List<String> notesPath) {
		
		this.notesPath = notesPath;
	}
	public List<String> getRecomondationPath() {
		return recomondationPath;
	}
	public void setRecomondationPath(List<String> recomondationPath) {
		this.recomondationPath = recomondationPath;
	}
	public String getLectureId() {
		return lectureId;
	}
	public void setLectureId(String lectureId) {
		this.lectureId = lectureId;
	}
	public String getLectureDate() {
		return lectureDate;
	}
	public void setLectureDate(String lectureDate) {
		this.lectureDate = lectureDate;
	}
	
	
	
	
	
}
