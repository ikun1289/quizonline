package com.tlcn.quizonline.models;


import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Document(collection="VerifyToken")
@Data
public class VerifyToken {
	@Id
	private ObjectId id = new ObjectId();
	private String token;
	private String userId;
	@Indexed(expireAfterSeconds = 59)
	private String createDatetime;
	
	
	public VerifyToken() {
		super();
		// TODO Auto-generated constructor stub
	}


	public VerifyToken(String token, String userId, String expireAt) {
		super();
		this.token = token;
		this.userId = userId;
		this.createDatetime = expireAt;
	}
	
	
}
