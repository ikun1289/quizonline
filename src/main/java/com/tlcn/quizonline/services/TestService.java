package com.tlcn.quizonline.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationExpression;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.ConditionalOperators;
import org.springframework.data.mongodb.core.aggregation.TypedAggregation;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.mongodb.BasicDBObject;
import com.tlcn.quizonline.models.ClassSection;
import com.tlcn.quizonline.models.Classroom;
import com.tlcn.quizonline.models.Quiz;
import com.tlcn.quizonline.models.Test;
import com.tlcn.quizonline.repositories.ClassroomRepository;
import com.tlcn.quizonline.repositories.TestRepository;

@Service
public class TestService {

	@Autowired
	TestRepository testRepository;
	@Autowired
	MongoTemplate mongoTemplate;
	@Autowired
	ClassSectionService classSectionService;

	public Optional<Test> getTestById(String id) {
		return testRepository.findById(id);
	}
	
	public void addNewTest(Test test, String sectionId)
	{
		test.setId(new ObjectId());
		testRepository.save(test);
		classSectionService.addNewTestToSection(test, sectionId);
	}
	
	public void addNewQuizToTest(Quiz quiz, String testId)
	{
		Query query = new Query(Criteria.where("id").is(testId));
		Update update = new Update().push("quizs", quiz.get_id());
		this.mongoTemplate.findAndModify(query, update, Test.class);
	}

	public void addNewQuizsToTest(List<String> quizId, String testId)
	{
		Query query = new Query(Criteria.where("id").is(testId));
		Update update = new Update().push("quizs").each(quizId);
		this.mongoTemplate.findAndModify(query, update, Test.class);
	}
	
	public void updateTest(Test test)
	{
		testRepository.save(test);
	}
	
}
