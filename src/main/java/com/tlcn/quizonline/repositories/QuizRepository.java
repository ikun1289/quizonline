package com.tlcn.quizonline.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.tlcn.quizonline.models.Quiz;

@Repository
public interface QuizRepository extends MongoRepository<Quiz,String>{

}
