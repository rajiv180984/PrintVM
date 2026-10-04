package com.first.test.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Table;

import org.hibernate.annotations.AccessType;
//import org.springframework.data.annotation.Id;
import javax.persistence.Id;

@Entity
@Table(name="authorities")
@AccessType("field")
public class OauthAutherities implements Serializable {

	private static final long serialVersionUID = 3802499339572565606L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(name="username", unique=true, nullable=false)
	private String username;
	
	@Column
	private String authority;
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getAuthority() {
		return authority;
	}
	public void setAuthority(String authority) {
		this.authority = authority;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}


}
