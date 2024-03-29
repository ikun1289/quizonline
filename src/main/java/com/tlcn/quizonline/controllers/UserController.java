package com.tlcn.quizonline.controllers;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.tlcn.quizonline.JWT.JwtAuthenticationFilter;
import com.tlcn.quizonline.JWT.JwtTokenProvider;
import com.tlcn.quizonline.models.Classroom;
import com.tlcn.quizonline.models.User;
import com.tlcn.quizonline.payload.LoginResponse;
import com.tlcn.quizonline.services.UserService;

@RestController
public class UserController {

	@Autowired
	UserService userService;
	@Autowired
	PasswordEncoder passwordEncoder;

	@GetMapping("/user/detail")
	public ResponseEntity<?> getUserDetail(HttpServletRequest request) {
		String jwt = new JwtAuthenticationFilter().getJwtFromRequest(request);
		String userId = new JwtTokenProvider().getUserIdFromJWT(jwt);

		Optional<User> user = userService.getUserById(userId);
		if (user.isPresent()) {
			User u = user.get();
			u.setPassword(null);
			return new ResponseEntity<User>(user.get(), HttpStatus.OK);
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("This user not exist!");
		}
	}

	@PostMapping("/user/changepass")
	public ResponseEntity<?> changePass(HttpServletRequest request, @RequestBody Map<String, String> userMap) {
		String oldPass;
		String newPass;
		try {
			oldPass = userMap.get("oldpass");
			newPass = userMap.get("newpass");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Trường thông tin không hợp lệ");
		}
		if (newPass.length() < 8)
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body("Mật khẩu mới không được ít hơn 8 ký tự!");
		System.out.println(oldPass + " " + newPass);
		String jwt = new JwtAuthenticationFilter().getJwtFromRequest(request);
		String userId = new JwtTokenProvider().getUserIdFromJWT(jwt);
		Optional<User> user = userService.getUserById(userId);
		if (user.isPresent()) {
			User uChangePass = user.get();
			String passwd = uChangePass.getPassword();
			if (passwordEncoder.matches(oldPass, passwd)) {
				uChangePass.setPassword(newPass);
				userService.addNewUser(uChangePass);
				return ResponseEntity.status(HttpStatus.OK).body("Đổi mật khẩu thành công!");
			} else {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Mật khẩu cũ không khớp!");
			}

		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Tài khoản không tồn tại!");
		}
	}

	@PostMapping("/user/editprofile")
	public ResponseEntity<?> editProfile(HttpServletRequest request, @RequestBody User userMap) {
		String jwt = new JwtAuthenticationFilter().getJwtFromRequest(request);
		String userId = new JwtTokenProvider().getUserIdFromJWT(jwt);

		Optional<User> user = userService.getUserById(userId);
		if (user.isPresent()) {
			User u = user.get();
			u.setName(userMap.getName());
			u.setPhone(userMap.getPhone());
			u.setAddress(userMap.getAddress());
			u.setGender(userMap.getGender());
			userService.editUser(u);
		}
		return ResponseEntity.status(HttpStatus.OK).body("Chỉnh sửa thông tin thành công!");

	}

	private Boolean validateUser(User userMap) {
		if (userMap.getName().isEmpty() || userMap.getName().length() < 6)
			return false;
		if (userMap.getEmail().isEmpty() || userMap.getEmail().length() < 6)
			return false;
		return true;
	}

	@GetMapping("/student/authorize")
	public ResponseEntity<?> authorizeStudent() {
		return new ResponseEntity<String>(HttpStatus.OK);
	}

	@GetMapping("/teacher/authorize")
	public ResponseEntity<?> authorizeTeacher() {
		return new ResponseEntity<String>(HttpStatus.OK);
	}

	@GetMapping("/jwt-check-role")
	public ResponseEntity<?> jwtAuthorizeCheckRole(HttpServletRequest request) {
		String jwt = new JwtAuthenticationFilter().getJwtFromRequest(request);
		String userId = new JwtTokenProvider().getUserIdFromJWT(jwt);
		Optional<User> user = userService.getUserById(userId);
		if (user.isPresent()) {
			return new ResponseEntity<LoginResponse>(new LoginResponse(jwt, user.get().getRole()), HttpStatus.OK);
		}
		return new ResponseEntity<String>("JWT ko hợp lệ", HttpStatus.UNAUTHORIZED);
	}
	
	@GetMapping("/user/get-activities")
	public ResponseEntity<?> getActivities(HttpServletRequest request) {
		String jwt = new JwtAuthenticationFilter().getJwtFromRequest(request);
		String userId = new JwtTokenProvider().getUserIdFromJWT(jwt);
		Optional<User> user = userService.getUserById(userId);
		if (user.isPresent()) {
			if(user.get().getRole().equals("student")) {
				updateRecentClass(user.get(), userId);
				user = userService.getUserById(userId);
			}
			
			return new ResponseEntity<List<Classroom>>(user.get().getRecentClass(), HttpStatus.OK);
		}
		return new ResponseEntity<String>("Xác thực thất bại", HttpStatus.UNAUTHORIZED);
	}

	private void updateRecentClass(User user, String studentId) {
		List<Classroom> c = user.getRecentClass();
		System.out.println(c);
		Iterator<Classroom> Iclassroom = c.iterator();
		while (Iclassroom.hasNext()) {

		    Classroom classroom = Iclassroom.next();
		    if(!classroom.getStudents().contains(studentId))
		    	Iclassroom.remove();
		}
		
		user.setRecentClass(c);
		userService.editUser(user);
	}
	
}
