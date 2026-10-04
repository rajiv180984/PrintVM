package com.first.test.model;

import java.io.Serializable;
import java.util.Date;
import java.sql.Timestamp;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="Feedback")
public class Feedback implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4123904820187921062L;
	private String fid;
	@Indexed
	private String emailId;
	private String comment;
	private String suggestion;
	private float stars;
	private Date createOn;
	private String replyMsg;
	private Date replyOn;
	
	public String getFid() {
		return fid;
	}
	public void setFid(String fid) {
		this.fid = fid;
	}
	public String getEmailId() {
		return emailId;
	}
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getSuggestion() {
		return suggestion;
	}
	public void setSuggestion(String suggestion) {
		this.suggestion = suggestion;
	}
	public float getStars() {
		return stars;
	}
	public void setStars(float stars) {
		this.stars = stars;
	}
	public String getReplyMsg() {
		return replyMsg;
	}
	public void setReplyMsg(String replyMsg) {
		this.replyMsg = replyMsg;
	}
	public Date getCreateOn() {
		return createOn;
	}
	public void setCreateOn(Date createOn) {
		this.createOn = createOn;
	}
	public Date getReplyOn() {
		return replyOn;
	}
	public void setReplyOn(Date replyOn) {
		this.replyOn = replyOn;
	}
	
	
}
