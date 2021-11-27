package com.tlcn.quizonline.models;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Document(collection="User")
@Data
public class User {

	@Id
	private ObjectId id = new ObjectId();
	private String userName;
	private String password;
	private String name;
	@Indexed(unique=true)
	private String email;
	private String phone;
	private String address;
	private Boolean gender;//true == male
	private String role;
	private Boolean enable;
	
	public User() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public User(ObjectId id, String userName, String passwd, String name, String email, String phone,String address, Boolean gender, String role) {
		super();
		this.id = id;
		this.userName = userName;
		this.password = passwd;
		this.name = name;
		this.email = email;
		this.phone = phone;
		this.address = address;
		this.gender = gender;
		this.role = role;
		this.enable = false;
	}
	public User(String userName, String passwd, String email, String name,String role) {
		super();
		this.id = new ObjectId();
		this.userName = userName;
		this.password = passwd;
		this.email = email;
		this.name = name;
		this.phone = "";
		this.gender = false;
		this.role = role;
		this.enable = false;
	}
	
}
