package com.tlcn.quizonline.models;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;

@Document(collection = "Section")
@Data
public class ClassSection {
	@Id
	private ObjectId _id= new ObjectId();
	private String name;
	private List<String> tests = new ArrayList<String>();
	
	
	public ClassSection() {
		super();
		// TODO Auto-generated constructor stub
	}
	public ClassSection(String name, List<String> tests) {
		super();
		this.name = name;
		this.tests = tests;
	}
	
	
	
	
}
