package com.tlcn.quizonline.models;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class Activity {
	private List<String> recentClassId = new ArrayList<String>();
}
