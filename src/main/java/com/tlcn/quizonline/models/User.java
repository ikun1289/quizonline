package com.tlcn.quizonline.models;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Document(collection="User")
@Data
public class User {

	@Id
	private ObjectId id = new ObjectId();
	private String userName;
	private String passwd;
	private String name;
	private String email;
	private String phone;
	private Boolean gender;//true == male
	private String role;
	
	public User() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public User(ObjectId id, String userName, String passwd, String name, String email, String phone, Boolean gender, String role) {
		super();
		this.id = id;
		this.userName = userName;
		this.passwd = passwd;
		this.name = name;
		this.email = email;
		this.phone = phone;
		this.gender = gender;
		this.role = role;
	}
	public User(String userName, String passwd, String email, String role) {
		super();
		this.id = new ObjectId();
		this.userName = userName;
		this.passwd = passwd;
		this.email = email;
		this.phone = "";
		this.gender = false;
		this.role = role;
	}
	
}
