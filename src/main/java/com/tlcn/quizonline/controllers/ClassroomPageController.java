package com.tlcn.quizonline.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
import com.tlcn.quizonline.models.ClassSection;
import com.tlcn.quizonline.models.Classroom;
import com.tlcn.quizonline.models.Test;
import com.tlcn.quizonline.models.User;
import com.tlcn.quizonline.payload.ClassSectionDetail;
import com.tlcn.quizonline.services.ClassSectionService;
import com.tlcn.quizonline.services.ClassroomService;
import com.tlcn.quizonline.services.TestService;
import com.tlcn.quizonline.services.UserService;

@RestController
public class ClassroomPageController {

	@Autowired
	private ClassroomService classroomService;
	@Autowired 
	private ClassSectionService ClassSectionService;
	@Autowired
	private TestService testService;
	@Autowired
	private UserService userService;
	
	
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

	@GetMapping("/student/classdetail")
	public ResponseEntity<?> classDetailStudent(HttpServletRequest request, @RequestParam("id") String classId)
	{
		String jwtToken = new JwtAuthenticationFilter().getJwtFromRequest(request);
		String studentId = new JwtTokenProvider().getUserIdFromJWT(jwtToken);
		Classroom c = classroomService.getClassroomByID(classId);
		if(c!=null && c.getStudents().contains(studentId))
		{
			List<ClassSectionDetail> details = new ArrayList<>();
			for (String sectionId : c.getSections()) {
				ClassSectionDetail detail = new ClassSectionDetail();
				Optional<ClassSection> section = ClassSectionService.getClassSectionById(sectionId); 
				if(section.isPresent())
				{
					detail.setId(sectionId);
					detail.setName(section.get().getName());
					List<Test> detail_tests = new ArrayList<>();
					for (String testId : section.get().getTests()) {
						Optional<Test> test = testService.getTestById(testId);
						if(test.isPresent())
						{
							Test t = new Test();
							t.setId(test.get().getId());
							t.setName(test.get().getName());
							detail_tests.add(t);
						}
					}
					detail.setTests(detail_tests);
					details.add(detail);
				}
			}
			return new ResponseEntity<List<ClassSectionDetail>>(details,HttpStatus.OK);
		}
		return new ResponseEntity<String>("Lớp học không tồn tại hoặc bạn không nằm trong lớp học này",HttpStatus.BAD_REQUEST);
	}
	
	@GetMapping("/student/liststudentinclass")
	public ResponseEntity<?> listStudentInClass(HttpServletRequest request, @RequestParam("id") String classId)
	{
		String jwtToken = new JwtAuthenticationFilter().getJwtFromRequest(request);
		String studentId = new JwtTokenProvider().getUserIdFromJWT(jwtToken);
		Classroom c = classroomService.getClassroomByID(classId);
		if(c!=null && c.getStudents().contains(studentId))
		{
			List<User> listStudent = new ArrayList<>();
			for (String userId : c.getStudents()) {
				Optional<User> u = userService.getUserById(userId);
				if(u.isPresent())
				{
					User student = new User();
					student.setId(u.get().getId());
					student.setEmail(u.get().getEmail());
					student.setName(u.get().getName());
					listStudent.add(student);
				}
			}
			return new ResponseEntity<List<User>>(listStudent,HttpStatus.OK);
		}
		return new ResponseEntity<String>("Lớp học không tồn tại hoặc bạn không nằm trong lớp học này",HttpStatus.BAD_REQUEST);
	}
	//--end of student
	
	
	
}
