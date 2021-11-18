package com.tlcn.quizonline.repositories;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.tlcn.quizonline.models.ClassSection;
import com.tlcn.quizonline.models.Classroom;
import com.tlcn.quizonline.models.Test;


@Repository
public interface ClassroomRepository extends MongoRepository<Classroom,String>{

	public List<Classroom> findByTeacherID(String teacherID);
	
	public List<Classroom> findByStudents(String id);
	
}
