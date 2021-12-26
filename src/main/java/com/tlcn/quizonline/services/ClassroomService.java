package com.tlcn.quizonline.services;

import java.util.List;
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
import com.tlcn.quizonline.repositories.ClassroomRepository;

@Service
public class ClassroomService {

	@Autowired
	private ClassroomRepository cRepository;
	@Autowired
	private MongoTemplate mongoTemplate;
	
	public Classroom getClassroomByID(String ID)
	{
		Optional<Classroom> classroom = cRepository.findById(ID);
		if(classroom.isPresent())
		{
			return classroom.get();
		}
		else {
			return null;
		}
	}
	public List<Classroom> getAllClassByTeacherID(String teacherID)
	{
		List<Classroom> cList = cRepository.findByTeacherID(teacherID);
		return cList;
	}
	public void addClass(Classroom classroom)
	{
		cRepository.save(classroom);
	}
	public void deleteClassById(String classId)
	{
		cRepository.deleteById(classId);
	}
	
	public List<Classroom> getClassesByStudentID(String id) {
		return cRepository.findByStudents(id);
	}
	
	public void addNewClassSectionToClass(ClassSection section, String classId)
	{
		Query query = new Query(Criteria.where("id").is(classId));
		Update update = new Update().push("sections", section.get_id());
		this.mongoTemplate.findAndModify(query, update, Classroom.class);
	}
	
	public void addNewClassSectionsToClass(List<String> sectionsId, String classId)
	{
		Query query = new Query(Criteria.where("id").is(classId));
		Update update = new Update().push("sections").each(sectionsId);
		this.mongoTemplate.findAndModify(query, update, Classroom.class);
	}
	public void deleteSectionFromClass(String sectionsId, String classId)
	{
		Query query = new Query(Criteria.where("id").is(classId));
		Update update = new Update().pull("sections",sectionsId);
		this.mongoTemplate.findAndModify(query, update, Classroom.class);
	}
	
	public List<Classroom> getAllClassStudentAttend(String studentId) {
		Query query = new Query(Criteria.where("students").all(studentId));
		return this.mongoTemplate.find(query, Classroom.class);
	}
	
	public Classroom getClassBySectionId(String sectionId) {
//		Query query = new Query(Criteria.where("sections").in(sectionId));
//		return this.mongoTemplate.findOne(query, Classroom.class);
		return cRepository.findBySections(new ObjectId(sectionId));
	}
	
	public void editClassName(String classId, String teacherId, String name) {
		Query query = new Query(Criteria.where("id").is(classId).andOperator(Criteria.where("teacherID").is(teacherId)));
		Update update = new Update().set("name",name);
		this.mongoTemplate.findAndModify(query, update, Classroom.class);
		
	}
	
	public Classroom addNewStudentToClass(String studentId, String classId, String teacherId) {
		Query query = new Query(Criteria.where("id").is(classId).andOperator(Criteria.where("teacherID").is(teacherId)));
		Update update = new Update().addToSet("students", studentId);
		return this.mongoTemplate.findAndModify(query, update, Classroom.class);
	}
	
	public Classroom deleteStudentInClass(String studentId, String classId, String teacherId) {
		Query query = new Query(Criteria.where("id").is(classId).andOperator(Criteria.where("teacherID").is(teacherId)));
		Update update = new Update().pull("students", studentId);
		return this.mongoTemplate.findAndModify(query, update, Classroom.class);
	}
	public void getClassroomByListId(List<String> recentClassId) {
		// TODO Auto-generated method stub
		
	}
	
	
	
}
