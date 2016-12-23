package com.dojocoders.codingwars.validation.model;

import static com.dojocoders.codingwars.validation.PiloteAutomatiqueTestUtils.isSame;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.dojocoders.codingwars.model.Secteur;
import com.dojocoders.codingwars.model.Sonar;
import com.dojocoders.codingwars.validation.PiloteAutomatiqueTestUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class SonarForValidationTest implements Sonar {

	private List<Secteur> vaisseauEnnemiList = Lists.newArrayList();

	private List<Secteur> meteoriteList = Lists.newArrayList();

	private Map<Secteur, String> messages = Maps.newHashMap();

	private SonarForValidationTest() {
	}

	public static SonarForValidationTest sonar() {
		return new SonarForValidationTest();
	}

	public SonarForValidationTest avecVaisseauEnnemi(Secteur secteur) {
		vaisseauEnnemiList.add(secteur);
		return this;
	}

	public SonarForValidationTest avecMeteorite(Secteur secteur) {
		meteoriteList.add(secteur);
		return this;
	}

	public SonarForValidationTest avecMessage(Secteur secteur, String message) {
		messages.put(secteur, message);
		return this;
	}

	@Override
	public boolean isVaisseauEnnemi(Secteur secteur) {
		PiloteAutomatiqueTestUtils.checkValidSecteur(secteur);
		return !vaisseauEnnemiList.stream() //
				.filter(vaisseauEnemi -> isSame(vaisseauEnemi, secteur)) //
				.collect(Collectors.toList()).isEmpty();
	}

	@Override
	public boolean isChampMeteorites(Secteur secteur) {
		PiloteAutomatiqueTestUtils.checkValidSecteur(secteur);
		return !meteoriteList.stream() // 
				.filter(meteorite -> isSame(meteorite, secteur)) //
				.collect(Collectors.toList()).isEmpty();
	}

	@Override
	public String intercepterMessage(Secteur secteur) {
		PiloteAutomatiqueTestUtils.checkValidSecteur(secteur);
		for (Map.Entry<Secteur, String> messageEntry : messages.entrySet()) {
			if (isSame(messageEntry.getKey(), secteur)) {
				return messageEntry.getValue();
			}
		}
		return "";
	}
}
