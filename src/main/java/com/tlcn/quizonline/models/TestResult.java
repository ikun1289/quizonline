package com.tlcn.quizonline.models;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Document
@Data
public class TestResult {
	@Id
	private ObjectId id = new ObjectId();
	private String studentId;
	private String testId;
	private int score;
	private long startTime;
	private long finishTime; 
	
	

}
