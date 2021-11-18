package com.tlcn.quizonline.controllers;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tlcn.quizonline.JWT.JwtAuthenticationFilter;
import com.tlcn.quizonline.JWT.JwtTokenProvider;
import com.tlcn.quizonline.models.Classroom;
import com.tlcn.quizonline.services.ClassroomService;

@RestController
public class ClassroomPageController {

	@Autowired
	private ClassroomService classroomService;
	
	
	//teacher
	
	@GetMapping("/teacher/getClassByTeacherID")
	public List<Classroom> getClassByTeacherID(@RequestParam("id") String teacherID){
		return classroomService.getAllClassByTeacherID(teacherID);
	}
	
	@GetMapping("/teacher/getClassByTeacherRandom")
	public List<Classroom> getClassByTeacherRandom(){
		return classroomService.getAllClassByTeacherID("T-random");
	}
	
	@PostMapping("/teacher/addNewClass")
	public ResponseEntity<String> addNewCLass(HttpServletRequest request
			,@RequestBody Classroom classroom) {
		String jwtToken = new JwtAuthenticationFilter().getJwtFromRequest(request);
		String teacherId = new JwtTokenProvider().getUserIdFromJWT(jwtToken);
		classroom.setTeacherID(teacherId);
		classroom.setId(new ObjectId());
		classroom.setStudents(new ArrayList<String>());
		classroom.setSections(new ArrayList<>());
		
		classroomService.addClass(classroom);
		
		return new ResponseEntity<String>("Tạo classroom mới thành công",HttpStatus.CREATED);
	}
	
	@PostMapping("/teacher/editclass")
	public ResponseEntity<String> editclass(HttpServletRequest request
			,@RequestBody Classroom classroom, @RequestParam("id") String classId) {
		String jwtToken = new JwtAuthenticationFilter().getJwtFromRequest(request);
		String teacherId = new JwtTokenProvider().getUserIdFromJWT(jwtToken);
		
		classroomService.addClass(classroom);
		
		return new ResponseEntity<String>("Tạo classroom mới thành công",HttpStatus.CREATED);
	}
	//--end of teacher--
	
	//student
	
	@GetMapping("/student/getclasses")
	public ResponseEntity<?> getClassesStudent(HttpServletRequest request)
	{
		String jwtToken = new JwtAuthenticationFilter().getJwtFromRequest(request);
		String studentId = new JwtTokenProvider().getUserIdFromJWT(jwtToken);
		List<Classroom> classrooms = classroomService.getAllClassStudentAttend(studentId);
		return new ResponseEntity<List<Classroom>>(classrooms,HttpStatus.FOUND);
	}

	//--end of student
	
	
	
}
