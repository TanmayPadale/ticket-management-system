package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.Repository.TicketRepo;
import com.example.demo.Repository.UserRepo;
import com.example.demo.model.Message;
import com.example.demo.model.Ticket;
import com.example.demo.model.Users;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

@org.springframework.stereotype.Service
public class Service 
{

	@Autowired
	private UserRepo urepo;
	
	@Autowired
	private TicketRepo trepo;
	
	@Autowired
	private UserService service;
	
	//register new user
	public ResponseEntity<?> newUser( @RequestBody Users user)
	{
		try 
		{
		if(user.getUsername().equals("")||user.getRole().getRole().equals(""))
		{
			Message message=new Message("Username or Role empty");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
		}
		else
			return ResponseEntity.accepted().body(service.register(user));
		}
		catch(Exception e)
		{
			Message message=new Message("User already registered");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
		}
	}
	
	
	/**
	 * 
	 * @param request
	 * @param ticket
	 * @return
	 */
	
	public ResponseEntity<?> newTicket(HttpServletRequest request, @RequestBody Ticket ticket)
	{
		Users user;
		String requestTokenHeader = request.getHeader("Authorization");
		String token;
		
		if(ticket.getReason().equals("")||ticket.getReason().equals(null))
		{
			Message message=new Message("Ticket reason empty");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
		}

		if(requestTokenHeader!= null && requestTokenHeader.startsWith("Bearer "))
		{
			token=requestTokenHeader.substring(7);
			try 
			{
				String temp=token.substring(0,token.indexOf("-"));
				int uid=Integer.parseInt(token.substring(token.indexOf("-")+1,token.length()));
				user=urepo.findById(uid).get();
				
				if(!temp.equals("1")&&user.getRole().getRid()!=1)
				{
					ticket.setStatus("NEW");
					ticket.setUsers(user);
					trepo.save(ticket);
					
					Message message=new Message("Ticket Raised Succesfully");
					return ResponseEntity.status(HttpStatus.OK).body(message);
				}
				else
				{
					Message message=new Message("Admin not allowed to raise ticket");
					return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(message);
				}
			}
			catch(Exception e)
			{
				Message message=new Message("Token is null or token format is wrong");
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);

			}
		}
		else
		{
			Message message=new Message("Token is null or token format is wrong");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
		}
	}
	
	//withdraw new ticket
	public ResponseEntity<?> withdrawal(HttpServletRequest request, @PathVariable(value = "tid") int tid)
	{
		try
		{
		Users user;
		String requestTokenHeader = request.getHeader("Authorization");
		String token;
		
		if(requestTokenHeader!= null && requestTokenHeader.startsWith("Bearer "))
		{
			token=requestTokenHeader.substring(7);
			try 
			{
				String rid=token.substring(0,token.indexOf("-"));
				int uid=Integer.parseInt(token.substring(token.indexOf("-")+1,token.length()));
				user=urepo.findById(uid).get();
				
				if(service.userUpdate(uid,tid)==null)
				{
					Message message=new Message("You are not allowed to withdraw this ticket");
					return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(message);
				}
				
				if(!rid.equals("1")&&user.getRole().getRid()!=1)
				{
					return ResponseEntity.accepted().body(service.userUpdate(uid,tid));
				}
				else
				{

					Message message=new Message("Admin not allowed to withdraw ticket");
					return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(message);
				}
			}
			catch(Exception e)
			{
				Message message=new Message("Token is null or token format is wrong");
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);

			}
		}	
		else
		{
			Message message=new Message("Token is null or token format is wrong");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
		}
		}
		catch(Exception e)
		{
			Message message=new Message("Ticket with Id not present");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
		}
	}
	
	//ticket approval 
	public ResponseEntity<?> adminApproval(HttpServletRequest request, @RequestParam String status,  @PathVariable(value = "tid") int tid)
	{
		try 
		{
		Ticket ticket=trepo.findById(tid).get();
		Users user;
		String requestTokenHeader = request.getHeader("Authorization");
		String token;
		
		//Stop update of withdrawn tickets //could use switch case
		if(ticket.getStatus().equals("WITHDRAWN"))
		{
			Message message=new Message("Ticket was withdrawed by user");
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(message);
		}
		if(ticket.getStatus().equals("APPROVED"))
		{
			Message message=new Message("Ticket was approved by admin");
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(message);
		}
		if(ticket.getStatus().equals("REJECTED"))
		{
			Message message=new Message("Ticket was rejected by admin");
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(message);
		}
		
		//check token
		if(requestTokenHeader!= null && requestTokenHeader.startsWith("Bearer "))
		{
			token=requestTokenHeader.substring(7);
			try 
			{
				String temp=token.substring(0,token.indexOf("-"));
				int uid=Integer.parseInt(token.substring(token.indexOf("-")+1,token.length()));
				user=urepo.findById(uid).get();
				
				if(service.adminUpdate(status,tid)==null)//for wrong status input
				{
					Message message=new Message("Wrong status input");
					return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
				}
				
				if(temp.equals("1")&&user.getRole().getRid()==1)//check role & perform status update only from admin role
				{
					return ResponseEntity.accepted().body(service.adminUpdate(status,tid));
				}
				else
				{
					Message message=new Message("Only admin allowed to approve/reject ticket");
					return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(message);
				}
			}
			catch(Exception e)
			{
				Message message=new Message("Token is null or token format is wrong");
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
			}
		}	
		else
		{
			Message message=new Message("Token is null or token format is wrong");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
		}
		}
		catch(Exception e)
		{
			Message message=new Message("Ticket with Id not present");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
		}
	}
	
	//update reason of ticket
	public ResponseEntity<?> ticketUpdate(HttpServletRequest request, @RequestBody Ticket ticket, @PathVariable(value = "tid") int tid)
	{
		try
		{
		Ticket ticket2=trepo.findById(tid).get();
		Users user;
		String requestTokenHeader = request.getHeader("Authorization");
		String token;
		
		if(requestTokenHeader!= null && requestTokenHeader.startsWith("Bearer "))
		{
			token=requestTokenHeader.substring(7);
			try 
			{
				int uid=Integer.parseInt(token.substring(token.indexOf("-")+1,token.length()));
				user=urepo.findById(uid).get();
				
				if(ticket2.getUser()==uid&&user.getToken().equals(token))// check if ticket updating is of same user and if role of user was mistyped
				{
					ticket2.setReason(ticket.getReason());
					trepo.save(ticket2);
					
					Message message=new Message("Ticket was succesfully updated");
					return ResponseEntity.status(HttpStatus.OK).body(message);
				}
				else
				{
					Message message=new Message("You are not authorized to update ticket.");
					return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(message);
				}
			}
			catch(Exception e)
			{
				Message message=new Message("Token is null or token format is wrong");
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);

			}
		}	
		else
		{
			Message message=new Message("Token is null or token format is wrong");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
		}
		}
		catch(Exception e)
		{
			Message message=new Message("Ticket with Id not present");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
		}
	}
	
	//admin history of all tickets
	public ResponseEntity<?> getAllTickets(HttpServletRequest request)
	{
			Users user;
			String requestTokenHeader = request.getHeader("Authorization");
			String token;
			
			if(requestTokenHeader!= null && requestTokenHeader.startsWith("Bearer "))
			{
				token=requestTokenHeader.substring(7);
				try 
				{
					int uid=Integer.parseInt(token.substring(token.indexOf("-")+1,token.length()));
					user=urepo.findById(uid).get();
					if(user.getToken().equals(token)&&user.getRole().getRole().equals("admin"))
					{
						List<Ticket> ticket1=new ArrayList<Ticket>(trepo.findAll());
						return ResponseEntity.accepted().body(ticket1);
					}
					else
					{
						Message message=new Message("Users not authorized");
						return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(message);
					}
				}
				catch(Exception e)
				{
					Message message=new Message("Token is null or token format is wrong");
					return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);

				}
			}
			else
			{
				Message message=new Message("Token is null or token format is wrong");
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
			}
		
	}
	
	//checking status of particular ticket
	@Async("asyncExecutor")
	public CompletableFuture<ResponseEntity<?>> checkStatus(HttpServletRequest request,@PathVariable(value = "tid") int tid )
	{
			try 
			{	
				Users user;
				String requestTokenHeader = request.getHeader("Authorization");
				String token;
				
				if(requestTokenHeader!= null && requestTokenHeader.startsWith("Bearer "))
				{
					token=requestTokenHeader.substring(7);
					try 
					{
						int uid=Integer.parseInt(token.substring(token.indexOf("-")+1,token.length()));
						user=urepo.findById(uid).get();
						Ticket ticket=trepo.findById(tid).get();

					if(ticket.getUser()==uid||user.getRole().getRole().equals("admin"))
					{
						System.out.println("Thread in service " + Thread.currentThread().getName());
						
						return CompletableFuture.completedFuture(new ResponseEntity<>(ticket,HttpStatus.OK));
					}
					else
					{
						Message message=new Message("Other users not authorized");
						return CompletableFuture.completedFuture(new ResponseEntity<>(message,HttpStatus.UNAUTHORIZED));
					}
					}
					catch(Exception e)
					{
						System.out.println("Thread in service " + Thread.currentThread().getName());

						Message message=new Message("Token is null or token format is wrong");
						return CompletableFuture.completedFuture(new ResponseEntity<>(message,HttpStatus.BAD_REQUEST));
					}
			}
				else
				{
					System.out.println("Thread in service " + Thread.currentThread().getName());

					Message message=new Message("Token is null or token format is wrong");
					return CompletableFuture.completedFuture(new ResponseEntity<>(message,HttpStatus.BAD_REQUEST));
				}
			}
			catch(Exception e)
			{
				System.out.println("Thread in service " + Thread.currentThread().getName());

				Message message=new Message("Ticket with Id not present");
				return CompletableFuture.completedFuture(new ResponseEntity<>(message,HttpStatus.BAD_REQUEST));
			}
		}
	
	//User history of his tickets
	public ResponseEntity<?> getTickets(HttpServletRequest request,@PathVariable(value = "username") String username)
	{
		String requestTokenHeader = request.getHeader("Authorization");
		String token;
		if(requestTokenHeader!= null && requestTokenHeader.startsWith("Bearer "))
		{
			token=requestTokenHeader.substring(7);
			try 
			{
				String temp=token.substring(0,token.indexOf("-"));
				int uid=Integer.parseInt(token.substring(token.indexOf("-")+1,token.length()));
				Users user1=urepo.findByUsername(username);
				Users user2=urepo.findById(uid).get();
				
				if(temp.equals("2")&&user1.getUid()==user2.getUid())
				{	
					List<Ticket> tickets=new ArrayList<Ticket>();
					tickets=trepo.findAllByUser(user1);	
					return ResponseEntity.status(HttpStatus.ACCEPTED).body(tickets);

				}
				else
				{
					Message message=new Message("Other users not authorized");
					return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(message);
				}
			}
			catch(Exception e)
			{
				Message message=new Message("Token is null or token format is wrong");
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
			}
		
		}
		else
		{
			Message message=new Message("Token is null or token format is wrong");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
		}
	}
	
	
	
}
