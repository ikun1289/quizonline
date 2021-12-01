package com.tlcn.quizonline.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import javax.mail.MessagingException;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.tlcn.quizonline.models.Classroom;
import com.tlcn.quizonline.models.User;
import com.tlcn.quizonline.models.VerifyToken;
import com.tlcn.quizonline.repositories.UserRepository;
import com.tlcn.quizonline.security.CustomUserDetails;

import lombok.extern.java.Log;


@Service
public class UserService implements UserDetailsService {

	@Autowired
	UserRepository userRepository;
	@Autowired
	PasswordEncoder passwordEncoder;
	@Autowired
	VerifyTokenService tokenService;
	@Autowired
	private MongoTemplate mongoTemplate;
	@Autowired
	EmailService emailService;

	public List<User> getAllUser() {
		return userRepository.findAll();
	}

	public Optional<User> getUserById(String id) {
		return userRepository.findById(id);
	}

	public void addNewUser(User user) {
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		userRepository.save(user);
	}

	public User findUsername(String username) {
		return userRepository.findByUserName(username);
	}

	public User findByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	public Boolean checkUsername(String username) {
		return (userRepository.findByUserName(username) != null) ? true : false;
	}

	public Boolean checkUserByEmail(String email) {
		return userRepository.findByEmail(email) != null ? true : false;
	}
	
	

	@Override
	public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		User user = userRepository.findByUserName(name);
		if (user == null) {
			System.out.println("User not found!");
			throw new UsernameNotFoundException("User not found!");
		} else {
			System.out.println("User found: " + user.getUserName() + " " + user.getPassword());
		}
		return new CustomUserDetails(user);

	}

	public UserDetails loadUserByID(String userId) {
		// TODO Auto-generated method stub
		Optional<User> user = userRepository.findById(userId);
		if (!user.isPresent()) {
			System.out.println("User not found!");
			throw new UsernameNotFoundException("User not found!");
		} else {
			System.out.println("User found: " + user.get().getUserName() + " " + user.get().getPassword());
		}
		return new CustomUserDetails(user.get());
	}

	public User register(User user) // String siteURL
	{
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		user.setEnable(false);
		userRepository.save(user);
		return user;
	}

	public VerifyToken createToken(User user) {
		VerifyToken token = tokenService.createNewToken(user.getId().toHexString());
		tokenService.saveNewVerifyToken(token);
		return token;
	}

	public User enableUser(String id) {
		Query query = new Query(Criteria.where("id").is(id));
		Update update = new Update().set("enable", true);
		return this.mongoTemplate.findAndModify(query, update, User.class);
	}
}
