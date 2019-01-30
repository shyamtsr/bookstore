package com.ssreddy.bookstore.sevice;

import java.util.Set;

import com.ssreddy.bookstore.model.PasswordResetToken;
import com.ssreddy.bookstore.model.User;
import com.ssreddy.bookstore.model.UserRole;

public interface UserService {

	PasswordResetToken getPasswordResetToken(String token);
	
	void craetePasswordResetTokenForUser(final User user, final String token);
	
	User findByUserName(String userName);
	
	User findByEmail(String email);

	User createUser(User user, Set<UserRole> userRole)throws Exception;
	
}
