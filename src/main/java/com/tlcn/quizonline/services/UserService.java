package com.tlcn.quizonline.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.tlcn.quizonline.models.User;
import com.tlcn.quizonline.repositories.UserRepository;
import com.tlcn.quizonline.security.CustomUserDetails;

import lombok.extern.java.Log;

@Service
public class UserService implements UserDetailsService {

	@Autowired
	UserRepository userRepository;
	@Autowired
	PasswordEncoder passwordEncoder;

	public List<User> getAllUser() {
		return userRepository.findAll();
	}

	public Optional<User> getUserById(String id) {
		return userRepository.findById(id);
	}

	public void addNewUser(User user) {
		user.setPasswd(passwordEncoder.encode(user.getPasswd()));
		userRepository.save(user);
	}

	public User findUsername(String username) {
		return userRepository.findByUserName(username);
	}

	public Boolean checkUsername(String username) {
		return  (userRepository.findByUserName(username) != null) ? true : false;
	}

	@Override
	public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		User user = userRepository.findByUserName(name);
		if (user == null) {
			System.out.println("User not found!");
			throw new UsernameNotFoundException("User not found!");
		} else {
			System.out.println("User found: " + user.getUserName() +" "+user.getPasswd());
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
			System.out.println("User found: " + user.get().getUserName() +" "+user.get().getPasswd());
		}
		return new CustomUserDetails(user.get());
	}
}

