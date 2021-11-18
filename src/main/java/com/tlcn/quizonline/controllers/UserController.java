package com.tlcn.quizonline.controllers;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tlcn.quizonline.JWT.JwtAuthenticationFilter;
import com.tlcn.quizonline.JWT.JwtTokenProvider;
import com.tlcn.quizonline.models.User;
import com.tlcn.quizonline.services.UserService;

@RestController
public class UserController {

	@Autowired
	UserService userService;
	
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
	
}
