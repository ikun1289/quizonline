package com.tlcn.quizonline.controllers;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tlcn.quizonline.JWT.JwtAuthenticationFilter;
import com.tlcn.quizonline.JWT.JwtTokenProvider;
import com.tlcn.quizonline.models.Answer;
import com.tlcn.quizonline.models.Classroom;
import com.tlcn.quizonline.models.Quiz;
import com.tlcn.quizonline.models.Test;
import com.tlcn.quizonline.models.TestResult;
import com.tlcn.quizonline.services.TestResultService;
import com.tlcn.quizonline.services.TestService;

@RestController
public class TestController {

	@Autowired
	TestService testService;
	@Autowired
	TestResultService testResultService;

	@GetMapping("/student/starttest")
	public ResponseEntity<?> startDoTest(HttpServletRequest request, @RequestParam("id") String testId) {

		// check if user have done this test or not
		Optional<Test> test = testService.getTestById(testId);
		if (test.isPresent()) {
			String jwt = new JwtAuthenticationFilter().getJwtFromRequest(request);
			String studentId = new JwtTokenProvider().getUserIdFromJWT(jwt);

			List<TestResult> testResults = testResultService.getTestResultByTestAndStudent(testId, studentId);
			if (testResults.size() < test.get().getNumbRetry()) {
				TestResult testResult = new TestResult();
				testResult.setStartTime(Instant.now().toEpochMilli());
				testResult.setTestId(testId);
				testResult.setStudentId(studentId);
				testResult.setFinishTime(0);

				testResultService.saveTestResult(testResult);

				return new ResponseEntity<Test>(test.get(), HttpStatus.OK);
			} else {
				return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Số lần làm vượt quá số lần cho phép!");
			}

		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Bài test không tồn tại!");
		}
	}

	@PostMapping("/student/submittest")
	public ResponseEntity<String> submitTest(HttpServletRequest request, @RequestParam("id") String testId,
			@RequestBody Optional<List<Quiz>> quizs) {
		String jwt = new JwtAuthenticationFilter().getJwtFromRequest(request);
		String studentId = new JwtTokenProvider().getUserIdFromJWT(jwt);
		TestResult testResult;
		Optional<Test> test;
		int score = 0;

		// check if u have started doing this test or not
		List<TestResult> testResults = testResultService.getTestResultByTestAndStudent(testId, studentId);
		if (testResults.size() == 0) {
			System.out.println("You havent started doing this test!");
			return new ResponseEntity<String>("You havent started doing this test", HttpStatus.BAD_REQUEST);
		}
		testResult = testResults.get(testResults.size() - 1);
		test = testService.getTestById(testId);
		if (!test.isPresent()) {
			return new ResponseEntity<String>("This test must have been deleted or smt.", HttpStatus.NOT_FOUND);
		}

		// check if time is acceptable
		Date date = new Date(testResult.getStartTime());
		Date submitDate = new Date();
		long diff = submitDate.getTime() - date.getTime();
		long diffSeconds = diff / 1000;
		System.out.println("Time in seconds: " + diffSeconds + " seconds.");
		if (diffSeconds > test.get().getTime() * 60) {
			System.out.println("Submit failed");
			return new ResponseEntity<String>(
					"Submit failed cuz you take too much time!\nTime in seconds: " + diffSeconds + " seconds.",
					HttpStatus.ACCEPTED);
		}

		// if everything is ok, time to check the answer submitted
		if (quizs.isPresent()) {
			List<Quiz> quizSubmit = quizs.get();
			// for mỗi quiz được nộp check quiz đó trong danh sách quiz của test
			for (Quiz q : test.get().getQuizs()) {
				Quiz p = quizSubmit.stream().filter(quiz -> q.get_id().toHexString().equals(quiz.get_id().toHexString())).findFirst().orElse(null);
				if (p != null) {
					// check answer của quiz được nộp
					score += checkAnswer(p,q); //p là quiz dưới bt, q là quiz được submit
				}
			}
		}
		//update testResult
		testResult.setFinishTime(submitDate.getTime());
		testResult.setScore(score);
		System.out.println("Submit your test success!");
		System.out.println(testResult);
		testResultService.saveTestResult(testResult);
		return new ResponseEntity<String>("Submit your test success!\nTime in seconds: " + diffSeconds + " seconds.",
				HttpStatus.ACCEPTED);


	}

	private int checkAnswer(Quiz p, Quiz q) {
		for (Answer answer : q.getAnswers()) {
			if (answer.isCorrect()) {
				Answer kq = null;
				kq = p.getAnswers().stream()
						.filter(answer2 -> answer.get_id().toHexString().equals(answer2.get_id().toHexString()))
						.findFirst().orElse(null);
				System.out.println("\nchecking answer...");
				System.out.println(answer);
				System.out.println(kq);
				if (kq == null)
					return 0;
			}
		}
		return 1;
	}

	@GetMapping("/student/testGetTest")
	public ResponseEntity<List<Classroom>> testGetTest() {
		System.out.println("testGetTest.../n");

		return new ResponseEntity<List<Classroom>>(HttpStatus.OK);
	}

	@PostMapping("/teacher/addNewTest")
	public ResponseEntity<String> addNewTest(@RequestParam("sectionId") String sectionid, @RequestBody Test test) {
		testService.addNewTest(test, sectionid);
		return new ResponseEntity<String>("Thêm bài kiểm tra mới thành công", HttpStatus.CREATED);

	}
	
	@PostMapping("/teacher/deleteTest")
	public ResponseEntity<String> deleteTest(@RequestParam("sectionId") String sectionid) {

		try {
			testService.deleteTestById(sectionid);
			return new ResponseEntity<String>("Xóa bài kiểm tra thành công", HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<String>("Đã xảy ra lỗi", HttpStatus.BAD_REQUEST);
		}
		
		
	} 

}
