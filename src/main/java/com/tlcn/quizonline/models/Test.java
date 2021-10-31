package com.tlcn.quizonline.models;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

import lombok.Data;


@Data
public class Test {
	
	@Id
	private ObjectId id = new ObjectId();
	private String name;
	private int time; //minute
	private int numbRetry;
	private List<Quiz> quizs;
	
	
	public Test(ObjectId id, String name, int time, int numbRetry, List<Quiz> quizs) {
		super();
		this.id = id;
		this.name = name;
		this.time = time;
		this.numbRetry = numbRetry;
		this.quizs = quizs;
	}
	
	

}
