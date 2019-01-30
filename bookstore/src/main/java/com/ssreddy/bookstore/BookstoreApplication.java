package com.ssreddy.bookstore;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.ssreddy.bookstore.model.Role;
import com.ssreddy.bookstore.model.User;
import com.ssreddy.bookstore.model.UserRole;
import com.ssreddy.bookstore.sevice.UserService;
import com.ssreddy.bookstore.utility.SecurityUtility;

@SpringBootApplication
public class BookstoreApplication implements CommandLineRunner{

	@Autowired
	private UserService userServiceImpl;
	
	public static void main(String[] args) {
		SpringApplication.run(BookstoreApplication.class, args);
	}
	
	 @Override
	public void run(String... args) throws Exception {

		 User user1= new User();
		 user1.setFirstName("Shyam");
		 user1.setLastName("Reddy");
		 user1.setUserName("shyam");
		 user1.setPassword(SecurityUtility.passwordEncoder().encode("shyam"));
		 user1.setEmail("shyam.job11@gmail.com");
		 Set<UserRole> userRoles= new HashSet<>();
		 
		 Role role1= new Role();
		 role1.setRoleId(1);
		 role1.setName("ROLE_USER");
		 userRoles.add(new UserRole(user1, role1));
		 
		 userServiceImpl.createUser(user1, userRoles);
		 
		 
	}
}
