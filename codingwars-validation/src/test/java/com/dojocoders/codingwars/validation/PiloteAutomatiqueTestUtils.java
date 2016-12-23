package com.dojocoders.codingwars.validation;

import com.dojocoders.codingwars.model.Alerte;
import com.dojocoders.codingwars.model.Secteur;
import com.dojocoders.codingwars.model.Sonar;
import com.google.common.base.Objects;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class PiloteAutomatiqueTestUtils {

    public static void assertParcoursSize(List<Secteur> parcours, int size) {
        assertThat(parcours).hasSize(size);
    }


    public static void assertParcoursExactlySame(List<Secteur> parcours, List<Secteur> expected) {
        assertThat(parcours).isNotNull().hasSize(expected.size());

        for (int i = 0; i < expected.size(); i++){
            assertThat(isSame(parcours.get(i), expected.get(i))).isTrue();
        }

    }

    public static void assertParcoursContains(List<Secteur> parcours, Secteur expected, int position) {
        assertThat(parcours).isNotNull();
        assertThat(parcours.size()).isGreaterThan(position);
        assertThat(isSame(parcours.get(position), expected)).isTrue();
    }

    public static void assertParcoursWithoutMeteorites(List<Secteur> parcours, Sonar sonar) {
        assertThat(parcours).isNotNull();
        // No meteorites
        assertThat(parcours.stream()
                .filter(sonar::isChampMeteorites)
                .collect(Collectors.toList()))
                .isNotNull().isEmpty();
    }

    public static void assertParcoursValid(List<Secteur> parcours, Secteur from, Secteur to) {
        assertThat(parcours).isNotNull().isNotEmpty();

        assertThat(isSame(parcours.get(0), from)).isTrue();

        // Parcours steps valides
        Secteur origin = from;
        for (Secteur candidate : parcours.subList(1, parcours.size())) {
        	checkValidSecteur(candidate);
            if (isCandidateFrom(origin, candidate)) {
                origin = candidate;
            } else {
                break;
            }
        }
        assertThat(isSame(origin, parcours.get(parcours.size() - 1))).isTrue();
        assertThat(isSame(origin, to)).isTrue();
    }

    private static boolean isCandidateFrom(Secteur origin, Secteur candidate) {
        int deltaX = Math.abs(origin.getX() - candidate.getX());
        int deltaY = Math.abs(origin.getY() - candidate.getY());
        int absolutDelta = deltaX + deltaY;
        return absolutDelta == 1;
    }


    public static void assertAlertesContains(List<Alerte> alertes, boolean checkMessage, Alerte... alertesExpected) {
        assertThat(alertes).isNotNull();
        for (Alerte expected: alertesExpected) {
            assertThat(alertes.stream()
            		.filter(alerte -> isSame(checkMessage, alerte, expected))
                    .collect(Collectors.toList()))
                    .isNotNull().isNotEmpty().hasSize(1);
        }
    }

    public static boolean isSame(Secteur pos1, Secteur pos2) {
        return pos1 == pos2 || (pos1.getX() == pos2.getX() && pos1.getY() == pos2.getY());
    }

    public static boolean isSame(boolean checkMessage, Alerte alerte1, Alerte alerte2) {
        return alerte1 == alerte2 ||
                (isSame(alerte1.getSecteur(), alerte2.getSecteur()) &&
                        alerte1.getType() == alerte2.getType() &&
                        (!checkMessage || Objects.equal(alerte1.getMessage(), alerte2.getMessage())));
    }

	public static void checkValidSecteur(Secteur secteur) {
		if(secteur.getX() < 0 || secteur.getY() < 0) {
			throw new IndexOutOfBoundsException("Secteur avec coordonnée négative : (" + secteur.getX() + "," + secteur.getY() + ")");
		}
	}
}
