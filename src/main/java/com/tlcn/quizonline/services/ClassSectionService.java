package com.tlcn.quizonline.services;

import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.tlcn.quizonline.models.ClassSection;
import com.tlcn.quizonline.models.Classroom;
import com.tlcn.quizonline.models.Test;
import com.tlcn.quizonline.repositories.ClassSectionRepository;

@Service
public class ClassSectionService {
	@Autowired
	ClassSectionRepository classSectionRepository;
	@Autowired
	MongoTemplate mongoTemplate;
	@Autowired
	ClassroomService classroomService;
	
	public Optional<ClassSection> getClassSectionById(String id) {
		return classSectionRepository.findById(id);
	}
	
	public void addNewClassSection(ClassSection classSection, String classId)
	{
		classSection.set_id(new ObjectId());
		classSectionRepository.save(classSection);
		classroomService.addNewClassSectionToClass(classSection, classId);
	}
	
	public void deleteSectionById(String sectionId)
	{
		classSectionRepository.deleteById(sectionId);
	}
	
	public void addNewTestToSection(Test test, String sectionId) {
		Query query = new Query(Criteria.where("_id").is(sectionId));
		Update update = new Update().push("tests", test.getId());
		this.mongoTemplate.findAndModify(query, update, ClassSection.class);
	}
	
	public void updateNameOfSection(String name, String sectionId)
	{
		Query query = new Query(Criteria.where("id").is(sectionId));
		Update update = new Update().set("name", name);
		this.mongoTemplate.findAndModify(query, update, ClassSection.class);
	}

	public void editSection(ClassSection s) {
		classSectionRepository.save(s);
		
	}

}
