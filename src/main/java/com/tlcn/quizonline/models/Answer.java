package com.tlcn.quizonline.models;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Document
@Data
public class Answer {

	@Id
	private ObjectId id= new ObjectId();
	private String answer;
	private boolean correct;
	
	
	
	public Answer() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Answer(String answer, boolean correct) {
		super();
		this.answer = answer;
		this.correct = correct;
	}
	
	
}
