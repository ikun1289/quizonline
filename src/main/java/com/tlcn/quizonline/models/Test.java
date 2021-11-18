package com.tlcn.quizonline.models;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonUnwrapped;

import lombok.Data;

@Document(collection = "Test")
@Data
public class Test {
	
	@Id
	private ObjectId id = new ObjectId();
	private String name;
	private int time; //minute
	private int numbRetry = 1;
	 @JsonUnwrapped
	private List<Quiz> quizs = new ArrayList<Quiz>();
	
	
	
	public Test(ObjectId id, String name, int time, int numbRetry, List<Quiz> quizs) {
		super();
		this.id = id;
		this.name = name;
		this.time = time;
		this.numbRetry = numbRetry;
		this.quizs = quizs;
	}



	public Test() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	
}
