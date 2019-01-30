package com.ssreddy.bookstore.sevice.impl;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssreddy.bookstore.model.PasswordResetToken;
import com.ssreddy.bookstore.model.User;
import com.ssreddy.bookstore.model.UserRole;
import com.ssreddy.bookstore.repository.PasswordResetTokenRepository;
import com.ssreddy.bookstore.repository.RoleRepository;
import com.ssreddy.bookstore.repository.UserRepository;
import com.ssreddy.bookstore.sevice.UserService;

@Service
public class UserServiceImpl implements UserService{
	
	private static final Logger LOG = LoggerFactory.getLogger(UserServiceImpl.class);
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private PasswordResetTokenRepository passwordResetTokenRepository;
	
	@Override
	public PasswordResetToken getPasswordResetToken(String token) {
		return passwordResetTokenRepository.findByToken(token);
	}
	
	@Override
	public void craetePasswordResetTokenForUser(User user, String token) {
		final PasswordResetToken resetMyToken= new PasswordResetToken(token, user);
		passwordResetTokenRepository.save(resetMyToken);
	}
	
	@Override
	public User findByUserName(String userName) {
		return userRepository.findByUserName(userName);
	}
	
	@Override
	public User findByEmail(String email) {
		return userRepository.findByEmail(email);
	}
	
	@Override
	public User createUser(User user, Set<UserRole> userRole) throws Exception {
		
		User localUser = userRepository.findByUserName(user.getUserName());
		
		if(localUser !=null) {
//			throw new Exception("user already exists. Nothing will be done.");
			LOG.info("user {} already exists. Nothing will be done.",user.getUserName());
		}else {
			for(UserRole role:userRole) {
				roleRepository.save(role.getRole());
			}
			
			user.getUserRole().addAll(userRole);
			
			localUser = userRepository.save(user);
			
		}
		
		return localUser;
	}
	
}
