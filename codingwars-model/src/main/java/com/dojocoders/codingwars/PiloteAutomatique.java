package com.dojocoders.codingwars;

import java.util.Set;

import com.dojocoders.codingwars.model.JournalDeBord;
import com.dojocoders.codingwars.model.Secteur;
import com.dojocoders.codingwars.model.Sonar;

public interface PiloteAutomatique {

	JournalDeBord voyage(Secteur depuis, Secteur vers, Sonar sonar, Set<String> fonctionnalitesActives);

}