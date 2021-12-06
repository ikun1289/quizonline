package com.tlcn.quizonline.repositories;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.tlcn.quizonline.models.ClassSection;

@Repository
public interface ClassSectionRepository extends MongoRepository<ClassSection,String>{

	public ClassSection findByTests(ObjectId id);
}
