package com.tlcn.quizonline.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import com.tlcn.quizonline.models.Answer;
import com.tlcn.quizonline.models.Classroom;
import com.tlcn.quizonline.models.Quiz;
import com.tlcn.quizonline.repositories.QuizRepository;

@Service
public class QuizService {
	@Autowired
	QuizRepository quizRepository;
	@Autowired
	MongoTemplate mongoTemplate;
	@Autowired
	TestService testService;
	
	public Optional<Quiz> getQuizById(String id) {
		return quizRepository.findById(id);
	}
	
	public void addNewQuiz(Quiz quiz, String testId)
	{
		quizRepository.save(quiz);
		testService.addNewQuizToTest(quiz, testId);
	}
	
	public void addManyQuiz(List<Quiz> quizs, String testId) {
		quizRepository.saveAll(quizs);
		List<String> quizId = new ArrayList<String>();
		for (Quiz quiz : quizs) {
			quizId.add(quiz.get_id().toHexString());
		}
		testService.addNewQuizsToTest(quizId, testId);
	}
}
