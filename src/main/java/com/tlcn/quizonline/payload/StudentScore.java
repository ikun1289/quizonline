package com.tlcn.quizonline.payload;

import lombok.Data;

@Data
public class StudentScore {

	private String studentId;
	private String studentName;
	private int numbCorrect;
	private int retry =0;
	private long time =0; //in milliseconds
}
