package com.tlcn.quizonline.payload;

import java.util.List;

import lombok.Data;

@Data
public class ScoreStatistics {
	private String testId;
	private String testName;
	private int numberOfQuiz;
	private int numbStudentInClass =0;
	private List<StudentScore> scores;
}
