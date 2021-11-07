package com.tlcn.quizonline.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.tlcn.quizonline.models.User;

@Repository
public interface UserRepository extends MongoRepository<User,String>{

	public User findByUserName(String name);
	
	public User findByEmail(String email);
}
