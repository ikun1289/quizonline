package com.tlcn.quizonline.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.tlcn.quizonline.models.User;


public interface UserRepository extends MongoRepository<User,String>{

	public User findByUserName(String name);
	
}
