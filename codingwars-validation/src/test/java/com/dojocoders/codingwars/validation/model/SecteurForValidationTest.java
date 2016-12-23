package com.dojocoders.codingwars.validation.model;

import com.dojocoders.codingwars.model.Secteur;

public class SecteurForValidationTest implements Secteur {

	private final int x;

	private final int y;

	private SecteurForValidationTest(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public static Secteur secteur(int x, int y) {
		return new SecteurForValidationTest(x, y);
	}

	@Override
	public int getX() {
		return x;
	}

	@Override
	public int getY() {
		return y;
	}

}
