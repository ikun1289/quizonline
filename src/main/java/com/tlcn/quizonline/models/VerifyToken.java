package com.tlcn.quizonline.models;

import java.security.Timestamp;
import java.time.LocalDateTime;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Document(collection="VerifyToken")
@Data
public class VerifyToken {
	@Id
	private ObjectId id = new ObjectId();
	private String token;
	private String userId;
	private LocalDateTime expireAt;
	private	Timestamp timestamp;
	
	
	public VerifyToken() {
		super();
		// TODO Auto-generated constructor stub
	}


	public VerifyToken(String token, String userId, LocalDateTime expireAt, Timestamp timestamp) {
		super();
		this.token = token;
		this.userId = userId;
		this.expireAt = expireAt;
		this.timestamp = timestamp;
	}
	
	
}
