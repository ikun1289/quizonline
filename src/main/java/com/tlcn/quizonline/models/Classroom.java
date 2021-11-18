package com.tlcn.quizonline.models;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;

@Document(collection = "Classroom")
@Data
public class Classroom {
	@Id
	private ObjectId id = new ObjectId();
	private String name;
	private String teacherID;
	private List<String> sections = new ArrayList<String>();
	private	List<String> students = new ArrayList<>();
	
	
	public Classroom() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Classroom(String name, String teacherID, List<String> sections) {
		super();
		this.name = name;
		this.teacherID = teacherID;
		this.sections = sections;
	}
	
	public Classroom(String name, String teacherID, List<String> sections, List<String> students) {
		super();
		this.name = name;
		this.teacherID = teacherID;
		this.sections = sections;
		this.students = students;
	}
	
	
	
}
