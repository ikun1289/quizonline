package com.tlcn.quizonline.models;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Document
public class Quiz {
	@Id
	private ObjectId _id= new ObjectId();
	private String content;
	private boolean quizType; //nếu muốn làm kiểu quiz multi với single answer
	private List<Answer> answers;
	
	
	public Quiz() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Quiz(String content, boolean quizType, List<Answer> answers) {
		super();
		this.content = content;
		this.quizType = quizType;
		this.answers = answers;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public boolean isQuizType() {
		return quizType;
	}
	public void setQuizType(boolean quizType) {
		this.quizType = quizType;
	}
	public List<Answer> getAnswers() {
		return answers;
	}
	public void setAnswers(List<Answer> answers) {
		this.answers = answers;
	}
	public ObjectId get_id() {
		return _id;
	}
	public void set_id(ObjectId _id) {
		this._id = _id;
	}
	
	
}
