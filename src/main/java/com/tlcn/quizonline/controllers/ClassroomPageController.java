package com.tlcn.quizonline.controllers;

import java.io.Console;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
import com.tlcn.quizonline.payload.ClassDetail;
import com.tlcn.quizonline.payload.ClassSectionDetail;
import com.tlcn.quizonline.services.ActivityService;
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
	@Autowired
	private ActivityService ActivityService;

	// teacher

	@GetMapping("/teacher/getClasses")
	public ResponseEntity<?> getClassByTeacherID(HttpServletRequest request) {
		String jwtToken = new JwtAuthenticationFilter().getJwtFromRequest(request);
		String teacherId = new JwtTokenProvider().getUserIdFromJWT(jwtToken);
		List<Classroom> classrooms = classroomService.getAllClassByTeacherID(teacherId);
		return new ResponseEntity<List<Classroom>>(classrooms, HttpStatus.OK);
	}

	// @GetMapping("/teacher/getClassByTeacherRandom")
	// public List<Classroom> getClassByTeacherRandom(){
	// return classroomService.getAllClassByTeacherID("T-random");
	// }

	@PostMapping("/teacher/addNewClass")
	public ResponseEntity<String> addNewCLass(HttpServletRequest request, @RequestBody Classroom classroom) {
		String jwtToken = new JwtAuthenticationFilter().getJwtFromRequest(request);
		String teacherId = new JwtTokenProvider().getUserIdFromJWT(jwtToken);
		classroom.setTeacherID(teacherId);
		classroom.setId(new ObjectId());
		classroom.setStudents(new ArrayList<String>());
		classroom.setSections(new ArrayList<>());

		classroomService.addClass(classroom);

		return new ResponseEntity<String>("Tạo classroom mới thành công", HttpStatus.OK);
	}

	@PostMapping("/teacher/editclass")
	public ResponseEntity<String> editclass(HttpServletRequest request, @RequestBody Map<String, String> classroom,
			@RequestParam("id") String classId) {
		String jwtToken = new JwtAuthenticationFilter().getJwtFromRequest(request);
		String teacherId = new JwtTokenProvider().getUserIdFromJWT(jwtToken);

		if (classroom.get("name") == "" || classroom.get("name")==null)
			return new ResponseEntity<String>("Tên lớp không được để trống", HttpStatus.BAD_REQUEST);

		try {
			classroomService.editClassName(classId, teacherId, classroom.get("name"));
			return new ResponseEntity<String>("Chỉnh sửa classroom thành công", HttpStatus.OK);
		}catch (Exception e) {
			System.out.println(e);
			return new ResponseEntity<String>("Lỗi đã xảy ra khi cố gắng thay đổi tên class", HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping("/teacher/deleteClass")
	public ResponseEntity<String> deleteClass(HttpServletRequest request, @RequestParam("id") String classId) {
		String jwtToken = new JwtAuthenticationFilter().getJwtFromRequest(request);
		String teacherId = new JwtTokenProvider().getUserIdFromJWT(jwtToken);
		Classroom c = classroomService.getClassroomByID(classId);
		if (c != null && c.getTeacherID().equals(teacherId)) {
			classroomService.deleteClassById(classId);
			return new ResponseEntity<String>("Xóa class thành công", HttpStatus.OK);

		}
		return new ResponseEntity<String>(
				"Classroom với id:" + classId + " không tồn tại hoặc classroom này không thuộc quyền sở hữu của bạn",
				HttpStatus.BAD_REQUEST);
	}

	@GetMapping("/teacher/classdetail")
	public ResponseEntity<?> classDetailTeacher(HttpServletRequest request, @RequestParam("id") String classId) {
		String jwtToken = new JwtAuthenticationFilter().getJwtFromRequest(request);
		String teacherId = new JwtTokenProvider().getUserIdFromJWT(jwtToken);
		Classroom c = classroomService.getClassroomByID(classId);
		if (c != null && c.getTeacherID().equals(teacherId)) {
			ActivityService.addNewRecentClass(c, teacherId);
			ClassDetail classDetail = new ClassDetail();
			classDetail.setName(c.getName());
			classDetail.setListStudent(userService.listStudent(c.getStudents()));
			List<ClassSectionDetail> details = new ArrayList<>();
			for (String sectionId : c.getSections()) {
				ClassSectionDetail detail = new ClassSectionDetail();
				Optional<ClassSection> section = ClassSectionService.getClassSectionById(sectionId);
				if (section.isPresent()) {
					detail.setId(sectionId);
					detail.setName(section.get().getName());
					List<Test> detail_tests = new ArrayList<>();
					for (String testId : section.get().getTests()) {
						Optional<Test> test = testService.getTestById(testId);
						if (test.isPresent()) {
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
			classDetail.setSections(details);
			return new ResponseEntity<ClassDetail>(classDetail, HttpStatus.OK);
		}
		return new ResponseEntity<String>("Lớp học không tồn tại hoặc bạn không nằm trong lớp học này",
				HttpStatus.BAD_REQUEST);
	}
	
	@PostMapping("/teacher/add-student-to-class")
	public ResponseEntity<?> addStudentToClass(HttpServletRequest request, @RequestParam("id") String classId, @RequestBody Map<String, String> body) {
		String jwtToken = new JwtAuthenticationFilter().getJwtFromRequest(request);
		String teacherId = new JwtTokenProvider().getUserIdFromJWT(jwtToken);
		String studentId = body.get("studentId");
		Optional<User> checkStudent = userService.getUserById(studentId);
		if(!checkStudent.isPresent() || !checkStudent.get().getRole().equals("student")) {
			return new ResponseEntity<String>("Học sinh không tồn tại",
					HttpStatus.BAD_REQUEST);
		}
		Classroom c = classroomService.addNewStudentToClass(studentId, classId, teacherId);
		if(c!=null) {
			return new ResponseEntity<String>("Thêm học sinh mới thành công", HttpStatus.OK);
		}
		return new ResponseEntity<String>("Lớp học không tồn tại hoặc bạn không nằm trong lớp học này",
				HttpStatus.BAD_REQUEST);
	}
	
	@PostMapping("/teacher/remove-student-from-class")
	public ResponseEntity<?> deleteStudentFromClass(HttpServletRequest request, @RequestParam("id") String classId, @RequestBody Map<String, String> body) {
		String jwtToken = new JwtAuthenticationFilter().getJwtFromRequest(request);
		String teacherId = new JwtTokenProvider().getUserIdFromJWT(jwtToken);
		String studentId = body.get("studentId");
		Classroom c = classroomService.deleteStudentInClass(studentId, classId, teacherId);
		if(c!=null) {
			return new ResponseEntity<String>("Xóa học sinh thành công", HttpStatus.OK);
		}
		return new ResponseEntity<String>("Lớp học không tồn tại hoặc bạn không nằm trong lớp học này",
				HttpStatus.BAD_REQUEST);
	}
	// --end of teacher--

	// student

	@GetMapping("/student/getclasses")
	public ResponseEntity<?> getClassesStudent(HttpServletRequest request) {
		String jwtToken = new JwtAuthenticationFilter().getJwtFromRequest(request);
		String studentId = new JwtTokenProvider().getUserIdFromJWT(jwtToken);
		List<Classroom> classrooms = classroomService.getAllClassStudentAttend(studentId);
		return new ResponseEntity<List<Classroom>>(classrooms, HttpStatus.OK);
	}

	@GetMapping("/student/classdetail")
	public ResponseEntity<?> classDetailStudent(HttpServletRequest request, @RequestParam("id") String classId) {
		String jwtToken = new JwtAuthenticationFilter().getJwtFromRequest(request);
		String studentId = new JwtTokenProvider().getUserIdFromJWT(jwtToken);
		Classroom c = classroomService.getClassroomByID(classId);
		if (c != null && c.getStudents().contains(studentId)) {
			ActivityService.addNewRecentClass(c, studentId);
			ClassDetail classDetail = new ClassDetail();
			classDetail.setName(c.getName());
			classDetail.setListStudent(userService.listStudent(c.getStudents()));
			List<ClassSectionDetail> details = new ArrayList<>();
			for (String sectionId : c.getSections()) {
				ClassSectionDetail detail = new ClassSectionDetail();
				Optional<ClassSection> section = ClassSectionService.getClassSectionById(sectionId);
				if (section.isPresent()) {
					detail.setId(sectionId);
					detail.setName(section.get().getName());
					List<Test> detail_tests = new ArrayList<>();
					for (String testId : section.get().getTests()) {
						Optional<Test> test = testService.getTestById(testId);
						if (test.isPresent()) {
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
			classDetail.setSections(details);
			return new ResponseEntity<ClassDetail>(classDetail, HttpStatus.OK);
		}
		return new ResponseEntity<String>("Lớp học không tồn tại hoặc bạn không nằm trong lớp học này",
				HttpStatus.BAD_REQUEST);
	}

	@GetMapping("/student/liststudentinclass")
	public ResponseEntity<?> listStudentInClass(HttpServletRequest request, @RequestParam("id") String classId) {
		String jwtToken = new JwtAuthenticationFilter().getJwtFromRequest(request);
		String studentId = new JwtTokenProvider().getUserIdFromJWT(jwtToken);
		Classroom c = classroomService.getClassroomByID(classId);
		if (c != null && c.getStudents().contains(studentId)) {
			List<User> listStudent = new ArrayList<>();
			for (String userId : c.getStudents()) {
				Optional<User> u = userService.getUserById(userId);
				if (u.isPresent()) {
					User student = new User();
					student.setId(u.get().getId());
					student.setEmail(u.get().getEmail());
					student.setName(u.get().getName());
					listStudent.add(student);
				}
			}
			return new ResponseEntity<List<User>>(listStudent, HttpStatus.OK);
		}
		return new ResponseEntity<String>("Lớp học không tồn tại hoặc bạn không nằm trong lớp học này",
				HttpStatus.BAD_REQUEST);
	}
	
	// --end of student
	
	@GetMapping("/user/back-to-class-from-test")
	public ResponseEntity<?> backToClass(@RequestParam("testId") String testId) {
		ClassSection classSection = ClassSectionService.getSectionByTestId(testId);
		if(classSection == null)
		{
			return new ResponseEntity<String>("0", HttpStatus.OK);
		}
		Classroom classroom = classroomService.getClassBySectionId(classSection.get_id().toHexString());
		if(classroom == null)
		{
			return new ResponseEntity<String>("0", HttpStatus.OK);
		}
		return new ResponseEntity<String>(classroom.getId().toHexString(), HttpStatus.OK);
		
	}
	
	@GetMapping("/user/back-to-class-from-section")
	public ResponseEntity<?> backToClass2(@RequestParam("sectionId") String sectionId) {
		Classroom classroom = classroomService.getClassBySectionId(sectionId);
		if(classroom == null)
		{
			return new ResponseEntity<String>("0", HttpStatus.OK);
		}
		return new ResponseEntity<String>(classroom.getId().toHexString(), HttpStatus.OK);
		
	}

}
