package com.tlcn.quizonline.controllers;

import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

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
	
	@PostMapping("/teacher/deleteSection")
	public ResponseEntity<?> deleteSection(HttpServletRequest request, @RequestParam("sectionId") String sectionId)
	{
		Optional<ClassSection> section= classSectionService.getClassSectionById(sectionId);
		if(section.isPresent())
		{
			classSectionService.deleteSectionById(sectionId);
			return new ResponseEntity<String>("Xóa chương thành công",HttpStatus.FOUND);
		}
		else
		{
			return new ResponseEntity<String>("Chương không có tồn tại",HttpStatus.NOT_FOUND);
		}
			
	}
	
	@PostMapping("/teacher/editSection")
	public ResponseEntity<?> editSection(HttpServletRequest request, @RequestParam("sectionId") String sectionId, @RequestBody Map<String, String> body)
	{
		Optional<ClassSection> section= classSectionService.getClassSectionById(sectionId);
		if(section.isPresent())
		{
			ClassSection s = section.get();
			s.setName(body.get("name"));
			classSectionService.editSection(s);
			return new ResponseEntity<String>("Xóa chương thành công",HttpStatus.FOUND);
		}
		else
		{
			return new ResponseEntity<String>("Chương không có tồn tại",HttpStatus.NOT_FOUND);
		}
			
	}
	
	
}
