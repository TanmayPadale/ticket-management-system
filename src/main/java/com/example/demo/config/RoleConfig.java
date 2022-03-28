package com.example.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.example.demo.Repository.RoleRepo;
import com.example.demo.Repository.UserRepo;
import com.example.demo.model.Role;
import com.example.demo.model.Users;

@Component
public class RoleConfig implements CommandLineRunner
{
	@Autowired
	private RoleRepo repo;
	
	@Autowired
	private UserRepo urep;

	@Override
	public void run(String... args) throws Exception 
	{
		// TODO Auto-generated method stub
		
		/*Role Admin=new Role();
		Admin.setRid(2);			//2nd entry
		Admin.setRole("user");
		repo.save(Admin);
		
		Admin.setRid(1);			//1st entry
		Admin.setRole("admin");
		repo.save(Admin);

		String token=Integer.toString(11);
		Users User=new Users();
		User.setUname("Tanmay");
		User.setPword("Tanmay123");
		User.setUid(0);
		User.setRole(Admin);
		User.setToken(token);
		urep.save(User);
		*/
	}
	
	
}