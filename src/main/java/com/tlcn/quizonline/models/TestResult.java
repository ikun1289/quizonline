package com.tlcn.quizonline.models;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Document
@Data
public class TestResult {
	@Id
	private ObjectId id = new ObjectId();
	@DBRef
	private User student;
	private String testId;
	private int score;
	private long startTime;
	private long finishTime; 
	
	

}
