package com.tlcn.quizonline.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tlcn.quizonline.models.TestResult;
import com.tlcn.quizonline.repositories.TestResultRepository;

@Service
public class TestResultService {

	@Autowired
	TestResultRepository testResultRepository;
	
	public Optional<TestResult> getTestResultById(String id)
	{
		return testResultRepository.findById(id);
	}
	
	public void saveTestResult(TestResult testResult)
	{
		testResultRepository.save(testResult);
	}
	
	public void deleteTestResultById(String id)
	{
		testResultRepository.deleteById(id);
	}
	
	public List<TestResult> getTestResultByTestAndStudent(String testId, String studentId)
	{
		try {
			return testResultRepository.findByTestIdAndStudentId(testId, studentId);
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.getMessage());
			return null;
		} 
	}
	
	public List<TestResult> getTestResultByTestId(String testId)
	{
		return testResultRepository.findByTestId(testId);
	}
	
}
