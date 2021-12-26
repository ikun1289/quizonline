package com.tlcn.quizonline.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.tlcn.quizonline.models.Classroom;
import com.tlcn.quizonline.models.User;

@Service
public class ActivityService {

	@Autowired
	MongoTemplate mongoTemplate;
	@Autowired
	UserService userService;
	@Autowired
	ClassroomService classroomService;
	
	public void addNewRecentClass(Classroom classroom, String userId)
	{
		Query query = new Query(Criteria.where("id").is(userId));
		Update update = new Update().pull("recentClass", classroom);
		Update update2 = new Update().push("recentClass", classroom);
		this.mongoTemplate.updateFirst(query, update, User.class);
		this.mongoTemplate.updateFirst(query, update2, User.class);
	}
	
	public List<Classroom> getClassListFromActivity(String userId)
	{
		Optional<User> uOptional = userService.getUserById(userId);
		if(uOptional.isPresent())
		{
			return uOptional.get().getRecentClass();
		}
		return new ArrayList<>();
	}
}
