package com.excilys.cdb.ui;

import java.util.Arrays;
import java.util.List;

public enum Actions {
	LIST_ALL_COMPUTERS(1, "Lister tous les ordinateurs"),
	LIST_ALL_COMPANIES(2, "Lister toutes les entreprises"),
	ADD_COMPUTER(3, "Ajouter un ordinateur"),
	UPDATE_COMPUTER(4, "Modifier un ordinateur"),
	DELETE_COMPUTER(5, "Supprimer un ordinateur"),
	GET_COMPUTER_BY_ID(6, "Récupérer un ordinateur par son ID"),
	LIST_ALL_COMPUTERS_PAGE(8, "Lister tous les ordinateurs paginés"),
	EXIT(7, "Quitter");

	private int myCode;
	private String myMessage;

	Actions(final int code, final String message) {
		this.myCode = code;
		this.myMessage = message;
	}

	public int getCode() {
		return myCode;
	}

	public String getMessage() {
		return myMessage;
	}

	public static List<Actions> asList() {
		return Arrays.asList(values());
	}

	public static Actions fromCode(final int code)
			throws UnsupportedActionException {
		switch (code) {
		case 1: return LIST_ALL_COMPUTERS;
		case 2: return LIST_ALL_COMPANIES;
		case 3: return ADD_COMPUTER;
		case 4: return UPDATE_COMPUTER;
		case 5: return DELETE_COMPUTER;
		case 6: return GET_COMPUTER_BY_ID;
		case 7: return EXIT;
		case 8: return LIST_ALL_COMPUTERS_PAGE;
		default : throw new UnsupportedActionException();
		}
	}
}
