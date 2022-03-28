package com.example.demo.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
public class Ticket 
{
	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int tid;
	@Column 
	private String reason;
	@Column
	private String status;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uid")
	@JsonIgnore
	private Users user;
	
	
	public Ticket() {
		// TODO Auto-generated constructor stub
	}
	public String getUsers()
	{
		return user.getUsername();
	}
	public int getUser() {
		return user.getUid();
	}
	public void setUsers(Users users) {
		this.user = users;
	}
	public int getTid() {
		return tid;
	}
	public void setTid(int tid) {
		this.tid = tid;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	
}
