package com.ssreddy.bookstore.controller;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.ssreddy.bookstore.model.PasswordResetToken;
import com.ssreddy.bookstore.model.Role;
import com.ssreddy.bookstore.model.User;
import com.ssreddy.bookstore.model.UserRole;
import com.ssreddy.bookstore.service.UserSecurityService;
import com.ssreddy.bookstore.sevice.UserService;
import com.ssreddy.bookstore.utility.MailConstructor;
import com.ssreddy.bookstore.utility.SecurityUtility;

@Controller
public class HomeController {

	@Autowired
	private UserService userServiceImpl;

	@Autowired
	private UserSecurityService userSecurityService;
	
	@Autowired
	private JavaMailSender mailSender;
	
	@Autowired
	private MailConstructor mailConstructor;

	@RequestMapping(value = "/")
	public String home() {
		return "index";
	}

	@RequestMapping(value = "/myaccount")
	public String myAccount() {
		return "myAccount";
	}

	@RequestMapping(value = "/login")
	public String login(Model model) {
		model.addAttribute("classActiveLogin", true);
		return "myAccount";
	}

	@RequestMapping(value = "/forgetpwd")
	public String forgetPassowrd(Model model) {

		model.addAttribute("classActiveForgetPwd", true);
		return "myAccount";
	}

	@RequestMapping(value = "/newuser", method = RequestMethod.POST)
	public String newUserPost(HttpServletRequest request, @ModelAttribute("email") String userEmail,
			@ModelAttribute("userName") String userName, Model model) throws Exception {
		model.addAttribute("classActiveNewAccount", true);
		model.addAttribute("email", userEmail);
		model.addAttribute("userName", userName);

		if (userServiceImpl.findByUserName(userName) != null) {
			model.addAttribute("userNameExists", true);
			return "myAccount";
		}

		if (userServiceImpl.findByEmail(userEmail) != null) {
			model.addAttribute("email", true);
			return "myAccount";
		}

		User user = new User();

		user.setUserName(userName);
		user.setEmail(userEmail);

		String password = SecurityUtility.randomPassword();

		String encryptedPassword = SecurityUtility.passwordEncoder().encode(password);
		user.setPassword(encryptedPassword);

		Role role = new Role();
		role.setRoleId(1);
		role.setName("ROLE_USER");
		Set<UserRole> userRole = new HashSet<>();
		userRole.add(new UserRole(user, role));
		userServiceImpl.createUser(user, userRole);

		String token = UUID.randomUUID().toString();
		userServiceImpl.craetePasswordResetTokenForUser(user, token);
		String appUrl = "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();

		SimpleMailMessage email = mailConstructor.constructResetTokenEmail(appUrl, request.getLocale(), token, user,
				password);
		mailSender.send(email);

		model.addAttribute("emailSent", true);

		return "myAccount";
	}

	@RequestMapping(value = "/newuser")
	public String newUserAccount(Locale locale, @RequestParam("token") String token, Model model) {
		PasswordResetToken passToken = userServiceImpl.getPasswordResetToken(token);
		if (passToken == null) {
			String message = "Invalid Token.";
			model.addAttribute("message", message);
			return "redirect:/badRequest";
		}

		User user = passToken.getUser();

		String userName = user.getUserName();

		UserDetails userDetails = userSecurityService.loadUserByUsername(userName);

		Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(),
				userDetails.getAuthorities());

		SecurityContextHolder.getContext().setAuthentication(authentication);

		model.addAttribute("user", user);
		
		model.addAttribute("classActiveEdit", true);
		return "myProfile";
	}

}
