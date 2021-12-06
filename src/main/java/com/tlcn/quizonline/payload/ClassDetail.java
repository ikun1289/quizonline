package com.tlcn.quizonline.payload;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class ClassDetail {

	private String name;
	private List<ClassSectionDetail> sections = new ArrayList<>();
}
