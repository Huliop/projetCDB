package com.excilys.cdb.ui;

import java.util.Arrays;
import java.util.List;

public enum UpdateActions {
	NAME(1, "Nom"),
	INTRODUCED(2, "Introduced"),
	DISCONTINUED(3, "Discontinued"),
	COMPANY(4, "Company");
	
	private int code;
	private String message;
	
	private UpdateActions(int code, String message) {
		this.code = code;
		this.message = message;
	}

	public int getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}
	
	public static List<UpdateActions> asList() {
		return Arrays.asList(values());
	}
	
	public static UpdateActions fromCode(int code) throws UnsupportedActionException {
		switch (code) {
		case 1: return NAME;
		case 2: return INTRODUCED;
		case 3: return DISCONTINUED;
		case 4: return COMPANY;
		default : throw new UnsupportedActionException();
		}
	}
}
