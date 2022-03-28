package com.example.demo.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
public class Users 
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@JsonIgnore
	private int uid;
	
	@Column(unique=true) 
	private String username;
	@Column
	//@JsonIgnore
	private String password;
	@Column
	@JsonIgnore
	private String token;
	
	
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JsonIgnoreProperties({"password","hibernateLazyInitializer", "handler"})
	private List<Ticket> ticket=new ArrayList<Ticket>();
	
	@Override
	public String toString() {
		return "Users [uid=" + uid + "]";
	}
	@ManyToOne
    @JoinColumn(name = "rid")
	private Role role;
	
	public Role getRole() {
		return role;
	}
	
	public void setRole(Role role) {
		this.role = role;
	}
	
	public int getUid() {
		return uid;
	}
	
	public void setUid(int uid) {
		this.uid = uid;
	}
	public String getUsername() {
		return username;
	}
	public void setUname(String username) {
		this.username = username;
	}
	//@JsonIgnore
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String pword) {
		this.password = pword;
	}
	public int getUidByUname(String uname)
	{
		return uid;
	}
}
