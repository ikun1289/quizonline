package com.tlcn.quizonline.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.tlcn.quizonline.models.ClassSection;
import com.tlcn.quizonline.models.Classroom;

@Repository
public interface ClassSectionRepository extends MongoRepository<ClassSection,String>{

}
