package com.tlcn.quizonline.controllers;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.bson.types.ObjectId;
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
import com.tlcn.quizonline.models.ClassSection;
import com.tlcn.quizonline.models.Classroom;
import com.tlcn.quizonline.models.Quiz;
import com.tlcn.quizonline.models.Test;
import com.tlcn.quizonline.models.TestResult;
import com.tlcn.quizonline.models.User;
import com.tlcn.quizonline.payload.ScoreStatistics;
import com.tlcn.quizonline.payload.StudentScore;
import com.tlcn.quizonline.services.ClassSectionService;
import com.tlcn.quizonline.services.ClassroomService;
import com.tlcn.quizonline.services.TestResultService;
import com.tlcn.quizonline.services.TestService;
import com.tlcn.quizonline.services.UserService;

@RestController
public class TestController {

	@Autowired
	TestService testService;
	@Autowired
	UserService userService;
	@Autowired
	ClassSectionService sectionService;
	@Autowired
	ClassroomService ClassroomService;
	@Autowired
	TestResultService testResultService;

	// student
	@GetMapping("/student/starttest")
	public ResponseEntity<?> startDoTest(HttpServletRequest request, @RequestParam("id") String testId) {

		// check if user have done this test or not
		Optional<Test> test = testService.getTestById(testId);
		if (test.isPresent()) {
			String jwt = new JwtAuthenticationFilter().getJwtFromRequest(request);
			String studentId = new JwtTokenProvider().getUserIdFromJWT(jwt);

			List<TestResult> testResults = testResultService.getTestResultByTestAndStudent(testId, studentId);
			if (testResults.size() < test.get().getNumbRetry()) {
				User u = new User();
				u.setId(new ObjectId(studentId));
				TestResult testResult = new TestResult();
				testResult.setStartTime(Instant.now().toEpochMilli());
				testResult.setTestId(testId);
				testResult.setStudent(u);
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

		// check if test exist
		test = testService.getTestById(testId);
		if (!test.isPresent()) {
			return new ResponseEntity<String>("This test must have been deleted or smt.", HttpStatus.NOT_FOUND);
		}

		// check if u have started doing this test or not
		List<TestResult> testResults = testResultService.getTestResultByTestAndStudent(testId, studentId);
		if (testResults.size() == 0) {
			System.out.println("You havent started doing this test!");
			return new ResponseEntity<String>("You havent started doing this test", HttpStatus.BAD_REQUEST);
		}
		testResult = testResults.get(testResults.size() - 1);

		// check if numbRetry
		if (testResult.getFinishTime() > 0) {
			return new ResponseEntity<String>("You can only submit once", HttpStatus.BAD_REQUEST);
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
			for (Quiz p : test.get().getQuizs()) {
				Quiz q = quizSubmit.stream()
						.filter(quiz -> p.get_id().toHexString().equals(quiz.get_id().toHexString())).findFirst()
						.orElse(null);
				if (p != null) {
					// check answer của quiz được nộp
					score += checkAnswer(p, q); // p là quiz dưới db, q là quiz được submit
				}
			}
		}
		// update testResult
		testResult.setFinishTime(submitDate.getTime());
		testResult.setScore(score);
		System.out.println("Submit your test success!");
		System.out.println(testResult);
		testResultService.saveTestResult(testResult);
		return new ResponseEntity<String>("Submit your test success!\nTime in seconds: " + diffSeconds + " seconds.",
				HttpStatus.ACCEPTED);

	}

	// end student

	private int checkAnswer(Quiz p, Quiz q) {
		int numCorrect = 0;
		for (Answer answer : p.getAnswers()) {
			if (answer.isCorrect()) {
				Answer kq = null;
				kq = q.getAnswers().stream()
						.filter(answer2 -> answer.get_id().toHexString().equals(answer2.get_id().toHexString()))
						.findFirst().orElse(null);
				System.out.println("\nchecking answer...");
				System.out.println(answer);
				System.out.println(kq);
				if (kq == null)
					return 0;
				numCorrect++;
			}
		}
		System.out.println(q.getAnswers().size() + " " + numCorrect);
		if (numCorrect != q.getAnswers().size())
			return 0;
		return 1;
	}

	@GetMapping("/student/get-test")
	public ResponseEntity<?> getTestStudent(HttpServletRequest request, @RequestParam("testId") String testId) {
		String jwt = new JwtAuthenticationFilter().getJwtFromRequest(request);
		String studentId = new JwtTokenProvider().getUserIdFromJWT(jwt);
		Optional<Test> test = testService.getTestById(testId);
		if (test.isPresent()) {
			ClassSection section = sectionService.getSectionByTestId(testId);
			if (section != null) {
				Classroom classroom = ClassroomService.getClassBySectionId(section.get_id().toHexString());
				if (classroom != null && classroom.getStudents().contains(studentId)) {
					Test t = test.get();
					t.setQuizs(null);
					return new ResponseEntity<Test>(t, HttpStatus.OK);
				} else {
					return new ResponseEntity<String>("Lớp mà bài kiểm tra này nằm trong đã bị xóa hoặc không tồn tại",
							HttpStatus.BAD_REQUEST);
				}
			} else {
				return new ResponseEntity<String>("Chương mà bài kiểm tra này nằm trong đã bị xóa hoặc không tồn tại",
						HttpStatus.BAD_REQUEST);
			}

		}
		return new ResponseEntity<String>("Bài kiểm tra không tồn tại hoặc đã bị xóa", HttpStatus.NOT_FOUND);
	}

	@GetMapping("/student/get-score-statistics")
	public ResponseEntity<?> getScoreStatsStudent(HttpServletRequest request, @RequestParam("testId") String testId) {
		String jwt = new JwtAuthenticationFilter().getJwtFromRequest(request);
		String studentId = new JwtTokenProvider().getUserIdFromJWT(jwt);
		Optional<Test> test = testService.getTestById(testId);
		if (test.isPresent()) {
			List<TestResult> testResults = testResultService.getTestResultByTestAndStudent(testId, studentId);
			ClassSection section = sectionService.getSectionByTestId(testId);
			List<String> studentsId = new ArrayList<>();
			if (section != null) {
				Classroom classroom = ClassroomService.getClassBySectionId(section.get_id().toHexString());
				if (classroom != null) {
					studentsId = classroom.getStudents();
				} else {
					return new ResponseEntity<String>("Lớp mà bài kiểm tra này nằm trong đã bị xóa hoặc không tồn tại",
							HttpStatus.BAD_REQUEST);
				}
			} else {
				return new ResponseEntity<String>("Chương mà bài kiểm tra này nằm trong đã bị xóa hoặc không tồn tại",
						HttpStatus.BAD_REQUEST);
			}

			// thông kê điểm
			ScoreStatistics scoreStatistics = new ScoreStatistics();
			scoreStatistics.setNumberOfQuiz(test.get().getQuizs().size());
			scoreStatistics.setTestId(testId);
			scoreStatistics.setTestName(test.get().getName());
			scoreStatistics.setNumbStudentInClass(studentsId.size());

			List<StudentScore> scores = new ArrayList<StudentScore>();

			for (TestResult result : testResults) {
				StudentScore score = new StudentScore();
				score.setStudentId(result.getStudent().getId().toHexString());
				score.setRetry(1);
				score.setStudentName(result.getStudent().getName());
				score.setNumbCorrect(result.getScore());
				score.setTime((result.getFinishTime() - result.getStartTime()));

				scores.add(score);

			}
			scoreStatistics.setScores(scores);
			return new ResponseEntity<ScoreStatistics>(scoreStatistics, HttpStatus.OK);
		}
		return new ResponseEntity<String>("Bài kiểm tra không tồn tại", HttpStatus.NOT_FOUND);
	}

	// teacher

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

	@GetMapping("/teacher/get-test")
	public ResponseEntity<?> getTest(HttpServletRequest request, @RequestParam("testId") String testId) {
		String jwt = new JwtAuthenticationFilter().getJwtFromRequest(request);
		String teacherId = new JwtTokenProvider().getUserIdFromJWT(jwt);
		Optional<Test> test = testService.getTestById(testId);
		if (test.isPresent()) {
			ClassSection section = sectionService.getSectionByTestId(testId);
			if (section != null) {
				Classroom classroom = ClassroomService.getClassBySectionId(section.get_id().toHexString());
				if (classroom != null && classroom.getTeacherID().equals(teacherId)) {
					return new ResponseEntity<Test>(test.get(), HttpStatus.OK);
				} else {
					return new ResponseEntity<String>("Lớp mà bài kiểm tra này nằm trong đã bị xóa hoặc không tồn tại",
							HttpStatus.BAD_REQUEST);
				}
			} else {
				return new ResponseEntity<String>("Chương mà bài kiểm tra này nằm trong đã bị xóa hoặc không tồn tại",
						HttpStatus.BAD_REQUEST);
			}

		}
		return new ResponseEntity<String>("Bài kiểm tra không tồn tại hoặc đã bị xóa", HttpStatus.NOT_FOUND);
	}

	@GetMapping("/teacher/listscore")
	public ResponseEntity<?> listscore(@RequestParam("testId") String testId) {
		List<TestResult> testResults = testResultService.getTestResultByTestId(testId);
		return new ResponseEntity<List<TestResult>>(testResults, HttpStatus.OK);
	}

	@GetMapping("/teacher/get-score-statistics")
	public ResponseEntity<?> getScoreStats(@RequestParam("testId") String testId) {
		Optional<Test> test = testService.getTestById(testId);
		if (test.isPresent()) {
			List<TestResult> testResults = testResultService.getTestResultByTestId(testId);
			ClassSection section = sectionService.getSectionByTestId(testId);
			List<String> studentsId = new ArrayList<>();
			if (section != null) {
				Classroom classroom = ClassroomService.getClassBySectionId(section.get_id().toHexString());
				if (classroom != null) {
					studentsId = classroom.getStudents();
				} else {
					return new ResponseEntity<String>("Lớp mà bài kiểm tra này nằm trong đã bị xóa hoặc không tồn tại",
							HttpStatus.BAD_REQUEST);
				}
			} else {
				return new ResponseEntity<String>("Chương mà bài kiểm tra này nằm trong đã bị xóa hoặc không tồn tại",
						HttpStatus.BAD_REQUEST);
			}

			// thông kê điểm
			ScoreStatistics scoreStatistics = new ScoreStatistics();
			scoreStatistics.setNumberOfQuiz(test.get().getQuizs().size());
			scoreStatistics.setTestId(testId);
			scoreStatistics.setTestName(test.get().getName());
			scoreStatistics.setNumbStudentInClass(studentsId.size());

			HashMap<String, Integer> hashMap = new HashMap<>();

			List<StudentScore> scores = new ArrayList<StudentScore>();

			for (TestResult result : testResults) {
				StudentScore score = new StudentScore();
				score.setStudentId(result.getStudent().getId().toHexString());
				score.setRetry(1);
				score.setStudentName(result.getStudent().getName());
				score.setNumbCorrect(result.getScore());
				score.setTime(result.getFinishTime() - result.getStartTime());

				Integer checkExist = hashMap.get(score.getStudentId());

				if (checkExist == null) {
					hashMap.put(score.getStudentId(), scores.size());
					scores.add(score);
				} else {
					score.setRetry(score.getRetry() + 1);
					if (scores.get(checkExist).getNumbCorrect() > score.getNumbCorrect()) {
						score.setNumbCorrect(scores.get(checkExist).getNumbCorrect());
					}
					scores.set(checkExist, score);
				}
			}
			scoreStatistics.setScores(scores);
			return new ResponseEntity<ScoreStatistics>(scoreStatistics, HttpStatus.OK);
		}
		return new ResponseEntity<String>("Bài kiểm tra không tồn tại", HttpStatus.NOT_FOUND);
	}
	// end teacher

}
