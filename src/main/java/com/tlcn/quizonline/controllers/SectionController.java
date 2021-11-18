package com.tlcn.quizonline.controllers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tlcn.quizonline.models.ClassSection;
import com.tlcn.quizonline.services.ClassSectionService;

@RestController
public class SectionController {

	@Autowired
	ClassSectionService classSectionService;
	
	@PostMapping("/teacher/addClassSection")
	public ResponseEntity<String> addClassSection(@RequestParam("classId") String classId,
			@RequestBody ClassSection requestBody)
	{
		classSectionService.addNewClassSection(requestBody, classId);
		return new ResponseEntity<String>("Thêm chương mới thành công",HttpStatus.CREATED);
	}
	
	@GetMapping("/teacher/section")
	public ResponseEntity<ClassSection> getClassSectionDetail(@RequestParam("sectionId") String sectionId)
	{
		Optional<ClassSection> section= classSectionService.getClassSectionById(sectionId);
		if(section.isPresent())
		{
			return new ResponseEntity<ClassSection>(section.get(),HttpStatus.FOUND);
		}
		else
		{
			return new ResponseEntity<ClassSection>(HttpStatus.NOT_FOUND);
		}
			
	}
	
	
}
