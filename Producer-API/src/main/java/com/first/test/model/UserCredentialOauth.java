package com.first.test.model;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.apache.commons.io.filefilter.FalseFileFilter;
import org.hibernate.annotations.AccessType;



@Entity
@Table(name="users")
@AccessType("field")
public class UserCredentialOauth implements Serializable{

	 /**
	 * 
	 */
	private static final long serialVersionUID = 5406081566030141602L;
	
	@Id
	@Column(name="id", nullable=false, unique=true )
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	
	@Column(name="username", unique=true, nullable=false)
	private String username;
	
	@Column
	private String password;
	
	@Column
	 private boolean enabled;
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public boolean isEnabled() {
		return enabled;
	}
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
	
}
