package com.tlcn.quizonline.repositories;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.tlcn.quizonline.models.TestResult;

@Repository
public interface TestResultRepository extends MongoRepository<TestResult,String>{

	
	public List<TestResult> findByTestIdAndStudentId(String testId, String studentId);
	
	public List<TestResult> findByTestId(String testId);
}
