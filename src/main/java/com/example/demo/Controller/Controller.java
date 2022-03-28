package com.example.demo.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Message;
import com.example.demo.model.Ticket;
import com.example.demo.model.Users;
import com.example.demo.service.Service;

import java.util.concurrent.CompletableFuture;

import javax.servlet.http.HttpServletRequest;



@RestController
@RequestMapping("/tms")
public class Controller 
{	
	@Autowired
	private Service serv;
	
	//API 1
	@PostMapping("/register")
	public ResponseEntity<?> newUser( @RequestBody Users user)
	{
		return serv.newUser(user); 
	}
	
	//API 2
	@PostMapping("/raise")
	public ResponseEntity<?> newTicket(HttpServletRequest request, @RequestBody Ticket ticket)
	{
		return serv.newTicket(request, ticket);
	}
	
	//API 3
	@GetMapping("/withdraw/{tid}")
	public ResponseEntity<?> withdrawal(HttpServletRequest request, @PathVariable(value = "tid") int tid)
	{
		return serv.withdrawal(request, tid);
	}
	
	//API 4
	@PutMapping("/ticketApproval/{tid}")
	public ResponseEntity<?> adminApproval(HttpServletRequest request, @RequestParam String status,  @PathVariable(value = "tid") int tid)
	{
		return serv.adminApproval(request, status, tid);
	}
	
	//API 5
	@PutMapping("/updateTicket/{tid}")
	public ResponseEntity<?> ticketUpdate(HttpServletRequest request, @RequestBody Ticket ticket, @PathVariable(value = "tid") int tid)
	{
		return serv.ticketUpdate(request, ticket, tid);
	}
	
	//API 6
	@GetMapping("/adminHistory")
	public ResponseEntity<?> getAllTickets(HttpServletRequest request)
	{
		return serv.getAllTickets(request);
	}

	
	//API 7
	@GetMapping("/checkStatus/{tid}")
	public ResponseEntity<?> checkStatus(HttpServletRequest request,@PathVariable(value = "tid") int tid )
	{
		CompletableFuture<ResponseEntity<?>> checkStatus= serv.checkStatus(request, tid);

		System.out.println("Thread in controller " + Thread.currentThread().getName());
		
		try {
			return checkStatus.get();
		} catch (Exception e) {
			System.out.println("Exception");
		}
		return new ResponseEntity<> (new Message("Async Get Books Eroor"), HttpStatus.OK);
	}
		
	
	//API 8
	@GetMapping("/userHistory/{username}")
	public ResponseEntity<?> getTickets(HttpServletRequest request,@PathVariable(value = "username") String username)
	{
		return serv.getTickets(request, username);
	}
	
}
