package console;

import java.util.Arrays;
import java.util.List;

import core.exceptions.UnsupportedActionException;

public enum UpdateActions {
	NAME(1, "Nom"),
	INTRODUCED(2, "Introduced"),
	DISCONTINUED(3, "Discontinued"),
	COMPANY(4, "Company");

	private int myCode;
	private String myMessage;

	UpdateActions(final int code, final String message) {
		this.myCode = code;
		this.myMessage = message;
	}

	public int getCode() {
		return myCode;
	}

	public String getMessage() {
		return myMessage;
	}

	public static List<UpdateActions> asList() {
		return Arrays.asList(values());
	}

	public static UpdateActions fromCode(final int code)
			throws UnsupportedActionException {
		switch (code) {
		case 1: return NAME;
		case 2: return INTRODUCED;
		case 3: return DISCONTINUED;
		case 4: return COMPANY;
		default : throw new UnsupportedActionException();
		}
	}
}
