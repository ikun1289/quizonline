package com.tlcn.quizonline.models;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Answer {

	@Id
	private ObjectId _id= new ObjectId();
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
	public String getAnswer() {
		return answer;
	}
	public void setAnswer(String answer) {
		this.answer = answer;
	}
	public boolean isCorrect() {
		return correct;
	}
	public void setCorrect(boolean correct) {
		this.correct = correct;
	}
	public ObjectId get_id() {
		return _id;
	}
	public void set_id(ObjectId _id) {
		this._id = _id;
	}
	
	
}
