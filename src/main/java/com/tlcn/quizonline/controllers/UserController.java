package com.tlcn.quizonline.controllers;

import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.tlcn.quizonline.JWT.JwtAuthenticationFilter;
import com.tlcn.quizonline.JWT.JwtTokenProvider;
import com.tlcn.quizonline.models.User;
import com.tlcn.quizonline.services.UserService;

@RestController
public class UserController {

	@Autowired
	UserService userService;
	@Autowired
	PasswordEncoder passwordEncoder;
	
	@GetMapping("/user/detail")
	public ResponseEntity<?> getUserDetail(HttpServletRequest request)
	{
		String jwt = new JwtAuthenticationFilter().getJwtFromRequest(request);
		String userId = new JwtTokenProvider().getUserIdFromJWT(jwt);
		
		Optional<User> user = userService.getUserById(userId);
		if(user.isPresent())
		{
			User u = user.get();
			u.setId(null);
			u.setPassword(null);
			return new ResponseEntity<User>(user.get(),HttpStatus.FOUND);
		}
		else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("This user not exist!");
		}
	}
	
	@PostMapping("/user/changepass")
	public ResponseEntity<?> changePass(HttpServletRequest request, @RequestBody Map<String, String> userMap)
	{
		String oldPass;
		String newPass;
		try
		{
			oldPass = userMap.get("oldpass");
			newPass = userMap.get("newpass");
		}
		catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
		if(newPass.length()<8)
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("New password cannot be shorter than 8 characters!");
		System.out.println(oldPass+" "+newPass);
		String jwt = new JwtAuthenticationFilter().getJwtFromRequest(request);
		String userId = new JwtTokenProvider().getUserIdFromJWT(jwt);
		Optional<User> user = userService.getUserById(userId);
		if(user.isPresent())
		{
			User uChangePass = user.get();
			String passwd = uChangePass.getPassword();
			if(passwordEncoder.matches(oldPass, passwd))
			{
				uChangePass.setPassword(newPass);
				userService.addNewUser(uChangePass);
				return ResponseEntity.status(HttpStatus.OK).body("Change pass complete!");
			}
			else
			{
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Old password not match!");
			}
				
		}
		else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("This user not exist!");
		}
	}
	
//	@PostMapping("/user/editprofile")
//	public ResponseEntity<?> editProfile(HttpServletRequest request, @RequestBody User userMap)
//	{
//		String jwt = new JwtAuthenticationFilter().getJwtFromRequest(request);
//		String userId = new JwtTokenProvider().getUserIdFromJWT(jwt);
//		if(validateUser(userMap))
//		{
//			
//		}
//	}
//
//	private Boolean validateUser(User userMap) {
//		if(userMap.getName().isEmpty() && userMap.getName().length()<6)
//			return false;
//		if(userMap.get)
//		return true;
//	}
	
	@GetMapping("/student/authorize")
	public ResponseEntity<?> authorizeStudent()
	{
		return new ResponseEntity<String>(HttpStatus.OK);
	}
	
	@GetMapping("/teacher/authorize")
	public ResponseEntity<?> authorizeTeacher()
	{
		return new ResponseEntity<String>(HttpStatus.OK);
	}
}
