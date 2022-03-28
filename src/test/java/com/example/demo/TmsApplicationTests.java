package com.example.demo;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withBadRequest;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.example.demo.Repository.RoleRepo;
import com.example.demo.Repository.TicketRepo;
import com.example.demo.Repository.UserRepo;
import com.example.demo.model.Role;
import com.example.demo.model.Ticket;
import com.example.demo.model.Users;
import com.example.demo.service.Service;

@SpringBootTest
class TmsApplicationTests {


	@Autowired
	private UserRepo urepo;
	@Autowired
	private TicketRepo trepo;
	@Autowired
	private RoleRepo rrepo;
	
	@Autowired
	private Service service;
	
	Role role=new Role();
	Users user=new Users();
	Ticket ticket=new Ticket();
	
	@BeforeEach
	void setUp()
	{
		role.setRid(2);
		role.setRole("user");
		rrepo.save(role);
		
		
		user.setUname("Tester");
		user.setPassword("123");
		user.setToken("2-10");
		user.setUid(10);
		user.setRole(role);
		urepo.save(user);
		
	}
	@Test
	void raise()
	{
		ticket.setReason("Testing 3");
		ticket.setStatus("HOLD");
		ticket.setUsers(user);
		trepo.save(ticket);
		//service.newTicket(null, ticket);
		//assertThat(found.);

		assertNotNull(trepo.findByReason("Testing 3"));
	}
	
	@AfterEach
	void tearDown()
	{
		this.trepo.delete(ticket);
	}
}
