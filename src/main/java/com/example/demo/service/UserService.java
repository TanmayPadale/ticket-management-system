package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.example.demo.Repository.TicketRepo;
import com.example.demo.Repository.UserRepo;
import com.example.demo.model.Message;
import com.example.demo.model.Ticket;
import com.example.demo.model.Users;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

@Service
public class UserService 
{	
	@Autowired
	private TicketRepo trepo;
	
	@Autowired
	private UserRepo urepo;
	
	private Ticket ticket;
	
	//For ticket withdrawal
	public Message userUpdate(int uid,int tid)
	{
		ticket=trepo.findById(tid).get();
		
		if(ticket.getUser()!=uid)
			return null;
		
		ticket.setStatus("WITHDRAWN");
		trepo.save(ticket);
		
		Message message=new Message("Ticket Withdrawn Succesfully");
		return message;
	}
	
	//For new user registration
	public Message register(Users users)
	{
		if(users.getRole().getRole().equals("user"))
			users.getRole().setRid(2);
		else if(users.getRole().getRole().equals("admin"))
			users.getRole().setRid(1);
		
		urepo.save(users);
		String token=Integer.toString(users.getRole().getRid())+"-"+Integer.toString(users.getUid());
		users.setToken(token);
		urepo.save(users);
		
		Message message=new Message("New user created succesfully, your token is - "+token);
		return message;
	}
	
	//For Approval status of tickets
	public Message adminUpdate(String status,int tid)
	{
		ticket=trepo.findById(tid).get();
		
		if(status.equals("approve"))
		{	
			ticket.setStatus("APPROVED");
			trepo.save(ticket);
			
			Message message=new Message("Ticket approved succesfully");
			return message;
		}
		else if(status.equals("hold"))
		{	
			ticket.setStatus("HOLD");
			trepo.save(ticket);
			
			Message message=new Message("Ticket held succesfully");
			return message;
		}
		else if(status.equals("reject"))
		{	
			ticket.setStatus("REJECTED");
			trepo.save(ticket);
			
			Message message=new Message("Ticket rejected succesfully");
			return message;
		}
		else
			return null;
	}
}
