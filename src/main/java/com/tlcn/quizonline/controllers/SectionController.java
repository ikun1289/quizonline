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
import com.tlcn.quizonline.models.Classroom;
import com.tlcn.quizonline.services.ClassSectionService;
import com.tlcn.quizonline.services.ClassroomService;

@RestController
public class SectionController {

	@Autowired
	ClassSectionService classSectionService;
	@Autowired
	ClassroomService ClassroomService;
	
	@PostMapping("/teacher/addClassSection")
	public ResponseEntity<String> addClassSection(@RequestParam("classId") String classId,
			@RequestBody ClassSection requestBody)
	{
		try {
			classSectionService.addNewClassSection(requestBody, classId);
		}catch (Exception e) {
			return new ResponseEntity<String>("Đã xảy ra lỗi",HttpStatus.BAD_REQUEST);
		}
		
		return new ResponseEntity<String>("Thêm chương mới thành công",HttpStatus.OK);
	}
	
	@GetMapping("/teacher/section")
	public ResponseEntity<ClassSection> getClassSectionDetail(@RequestParam("sectionId") String sectionId)
	{
		Optional<ClassSection> section= classSectionService.getClassSectionById(sectionId);
		if(section.isPresent())
		{
			return new ResponseEntity<ClassSection>(section.get(),HttpStatus.OK);
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
		Classroom classroom = ClassroomService.getClassBySectionId(sectionId);
		if(section.isPresent() && classroom!=null)
		{
			
			classSectionService.deleteSectionById(sectionId, classroom.getId().toHexString());
			return new ResponseEntity<String>("Xóa chương thành công",HttpStatus.OK);
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
