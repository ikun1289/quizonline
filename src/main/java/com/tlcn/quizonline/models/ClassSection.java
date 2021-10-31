package com.tlcn.quizonline.models;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document
public class ClassSection {
	@Id
	private ObjectId _id= new ObjectId();
	private String name;
	private List<Test> tests;
	
	
	public ClassSection() {
		super();
		// TODO Auto-generated constructor stub
	}
	public ClassSection(String name, List<Test> tests) {
		super();
		this.name = name;
		this.tests = tests;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<Test> getQuizs() {
		return tests;
	}
	public void setQuizs(List<Test> tests) {
		this.tests = tests;
	}
	public ObjectId getID() {
		return _id;
	}
	public void setID(ObjectId iD) {
		_id = iD;
	}
	
	
	
}
