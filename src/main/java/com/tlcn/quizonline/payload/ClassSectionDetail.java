package com.tlcn.quizonline.payload;

import java.util.List;

import com.tlcn.quizonline.models.Test;

import lombok.Data;

@Data
public class ClassSectionDetail {
	
	private String id;
	private String name;
	private List<Test> tests;

}
