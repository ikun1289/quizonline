package com.tlcn.quizonline.models;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "Classroom")
public class Classroom {
	@Id
	private ObjectId id = new ObjectId();
	private String name;
	private String teacherID;
	private List<ClassSection> sections;
	
	
	public Classroom() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Classroom(String name, String teacherID, List<ClassSection> sections) {
		super();
		this.name = name;
		this.teacherID = teacherID;
		this.sections = sections;
	}
	public ObjectId getID() {
		return id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTeacherID() {
		return teacherID;
	}
	public void setTeacherID(String teacherID) {
		this.teacherID = teacherID;
	}
	public List<ClassSection> getSections() {
		return sections;
	}
	public void setSections(List<ClassSection> sections) {
		this.sections = sections;
	}
	
	
}
