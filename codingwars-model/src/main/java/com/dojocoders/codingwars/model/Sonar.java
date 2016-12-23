package com.dojocoders.codingwars.model;

public interface Sonar {

    boolean isVaisseauEnnemi(Secteur secteur);

    boolean isChampMeteorites(Secteur secteur);

    /** @return le message intercepté sur le secteur, ou <code>""</code> s'il y en a de message */
    String intercepterMessage(Secteur secteur);
}
