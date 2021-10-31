package com.tlcn.quizonline.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tlcn.quizonline.models.Answer;
import com.tlcn.quizonline.models.ClassSection;
import com.tlcn.quizonline.models.Classroom;
import com.tlcn.quizonline.models.Quiz;
import com.tlcn.quizonline.models.Test;
import com.tlcn.quizonline.services.ClassroomService;

@RestController
public class ClassroomPageController {

	@Autowired
	private ClassroomService classroomService;
	
	@GetMapping("/api/getClassByTeacherID")
	public List<Classroom> getClassByTeacherID(@RequestParam("id") String teacherID){
		return classroomService.getAllClassByTeacherID(teacherID);
	}
	
	@GetMapping("/api/getClassByTeacherRandom")
	public List<Classroom> getClassByTeacherRandom(){
		return classroomService.getAllClassByTeacherID("T-random");
	}
	
	@PostMapping("/addRandomClass")
	public Classroom addRandomCLass() {
		Classroom classroom = new Classroom("Random test 2","T-random",null);
		List<ClassSection> classSections = new ArrayList<ClassSection>();
		List<Quiz> quizs = new ArrayList<>();
		List<Answer> answers = new ArrayList<>();
		answers.add(new Answer("random answer 1",true));
		answers.add(new Answer("random answer 2",false));
		quizs.add(new Quiz("random quiz 1",false,answers));
		quizs.add(new Quiz("random quiz 2",false,answers));
		List<Test> tests = new ArrayList<>();
		tests.add(new Test(new ObjectId(), "Shrimp1", 30, 1, quizs));
		tests.add(new Test(new ObjectId(), "Shrimp2", 30, 1, quizs));
		
		classSections.add(new ClassSection("randome section 1",tests));
		classSections.add(new ClassSection("randome section 2",tests));
		classroom.setSections(classSections);
		
		classroomService.addClass(classroom);
		return classroom;
	}
	
	
	@GetMapping("/api/classroom")
	public ResponseEntity<Classroom> getClassroomByID(@RequestParam("id") String ID) {
		Classroom classroom = classroomService.getClassroomByID(ID);
		System.out.print(classroom.getID());
		return new ResponseEntity<Classroom>(classroom,HttpStatus.OK);
	}
	
	@PostMapping("/api/testUpdateClassroomName")
	public Classroom testUpdate(){
		String name = "Classroom test";
		String id = "";
		return classroomService.updateClassName(name, id);
	}
}
