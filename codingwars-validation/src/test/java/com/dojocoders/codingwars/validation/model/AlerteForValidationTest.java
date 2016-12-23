package com.dojocoders.codingwars.validation.model;

import com.dojocoders.codingwars.model.Alerte;
import com.dojocoders.codingwars.model.Secteur;

public class AlerteForValidationTest implements Alerte {

	private final Secteur secteur;
	private final String type;
	private final String message;

	private AlerteForValidationTest(Secteur secteur, String type, String message) {
		this.secteur = secteur;
		this.type = type;
		this.message = message;
	}

	public static Alerte alerte(Secteur secteur, String type, String message) {
		return new AlerteForValidationTest(secteur, type, message);
	}

	@Override
	public Secteur getSecteur() {
		return secteur;
	}

	@Override
	public String getType() {
		return type;
	}

	@Override
	public String getMessage() {
		return message;
	}
}
