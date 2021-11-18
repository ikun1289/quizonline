package com.tlcn.quizonline.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.tlcn.quizonline.models.Test;


@Repository
public interface TestRepository extends MongoRepository<Test,String>{

}
