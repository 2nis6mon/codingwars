package com.dojocoders.codingwars.validation;

import com.dojocoders.codingwars.*;
import com.dojocoders.codingwars.model.JournalDeBord;
import com.dojocoders.codingwars.model.Sonar;
import com.dojocoders.codingwars.validation.model.SonarForValidationTest;
import com.dojocoders.score.junit.ScoreBlockJUnit4ClassRunner;
import com.dojocoders.score.junit.annotations.InjectImpl;
import com.dojocoders.score.junit.annotations.Persist;
import com.dojocoders.score.junit.annotations.Score;
import com.dojocoders.score.junit.persistence.ScoreApiRest;
import com.google.common.collect.Sets;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.Set;

import static com.dojocoders.codingwars.validation.model.AlerteForValidationTest.alerte;
import static com.dojocoders.codingwars.validation.PiloteAutomatiqueTestUtils.*;
import static com.dojocoders.codingwars.validation.model.SecteurForValidationTest.secteur;
import static org.fest.assertions.api.Assertions.assertThat;


/**
 * - Cout du voyage : 500
 *
 * Premiere commande :
 *
 * 1 - Chemin de point A vers un point B en esquivant les météorites
 *
 *      - Si traverse météorites ou parcours invalide (deplacement en diagonal ou pas contigue
 *      Sinon
 *          - Pour arriver à destination
 *              check_parcours_arriverADestination
 *
 *          - en plus si le chemin le plus court
 *              check_parocours_arriverADestination_PlusCourt
 *
 *      - Si Alerte MESSAGE_EMIS "Voyage impossible"
 *          check_parcours_voyageImpossible
 *
 *
 * 2 - Alerte Vaisseau Enemi
 *
 *      - Si alerte n'est pas lévé avec alerte desactivé
 *         check_messageVaisseau_AlerteNonLeve_siDesactive
 *
 *      - Si l'alerte est lévé sans vérification de message
 *         check_messageVaisseau_AlerteLeve
 *
 *      - Si l'alerte est lévé avec le message correcte
 *         check_messageVAisseau_AlerteLeve_avec_message
 *
 *
 * 3 - Alerte Message Intercepté
 *
 *      - Si alerte n'est pas lévé avec alerte desactivé
 *         check_messageIntercepte_AlerteNonLeve_siDesactive
 *
 *      - Si l'alerte est lévé sans vérification de message
 *         check_messageIntercepte_AlerteLeve
 *
 *      - Si l'alerte est lévé avec le message decrypté
 *          check_messageIntercepte_AlerteLeve_avec_messageDecrypte
 *
 *
 * Deuxieme commande :
 *
 * 4 - Chemin de point A vers un point B avec bouclier
 *
 *      - Si traverse plus de deux météorites ou parcours invalide (deplacement en diagonal ou pas contigue)
 *      Sinon
 *          - Pour trouver un chemin plus optimal en traversant deux champs de météorites
 *              check_parcours_avecBouclier
 *
 *
 * 5 - Alertes messages emis
 *
 *      - Pour emettre un message par vaisseau énémi détetcté crypté
 *          check_messageEmis_pour_messageVaisseau_Crypte
 *
 *      - Pour emettre un message par message énémi intercepté crypté
 *          check_messageEmis_pour_messageIntercepte_Crypte
 *
 **/
@RunWith(ScoreBlockJUnit4ClassRunner.class)
@Persist(ScoreApiRest.class)
public class PiloteAutomatiqueScoreTest {

    @InjectImpl
    private PiloteAutomatique piloteAutomatique;

    private static final Set<String> allActivated = Sets.newHashSet("VAISSEAU", "INTERCEPTER");
    private static final Set<String> allActivatedDeuxiemePhase = Sets.newHashSet("VAISSEAU", "INTERCEPTER", "EMETTRE", "BOUCLIER");
    private static final Set<String> desactivated = Sets.newHashSet();

    /************************************/
    /************************************/
    /*****    TEST POUR LA PHASE 1  *****/
    /************************************/
    /************************************/


    /************************************/
    /*****   1 CHEMIN LE PLUS COURT *****/
    /************************************/


    // PARCOURS SIMPLE SANS METEORITES

    /**
     * Test for problem :
     *
     *     A - - - B
     */
    @Test(timeout = 1000)
    @Score(50)
    public void check_parcours_arriverADestination_ligneDroite() {
        // Setup
        Sonar sonar = SonarForValidationTest.sonar();

        // Test
        JournalDeBord journalDeBord = piloteAutomatique.voyage(secteur(0, 0), secteur(4, 0), sonar, allActivated);

        // Assertions
        assertParcoursValid(journalDeBord.getParcours(), secteur(0, 0), secteur(4, 0));
        assertParcoursWithoutMeteorites(journalDeBord.getParcours(), sonar);
    }

    /**
     * Test for problem :
     *
     *     - - A - - -
     *     - - - - - -
     *     - - - - - -
     *     - - - - - -
     *     - - - - - B
     *     - - - - - -
     */
    @Test(timeout = 1000)
    @Score(50)
    public void check_parcours_arriverADestination_sansMeteorites() {
        // Setup
        Sonar sonar = SonarForValidationTest.sonar();

        // Test
        JournalDeBord journalDeBord = piloteAutomatique.voyage(secteur(2, 0), secteur(5, 4), sonar, allActivated);

        // Assertions
        assertParcoursValid(journalDeBord.getParcours(), secteur(2, 0), secteur(5, 4));
        assertParcoursWithoutMeteorites(journalDeBord.getParcours(), sonar);
    }

    /**
     * Test for problem :
     *
     *     - - A - - -
     *     - - - - - -
     *     - - - - - -
     *     - - - - - -
     *     - - - - - B
     *     - - - - - -
     */
    @Test(timeout = 1000)
    @Score(50)
    public void check_parcours_arriverADestination_sansMeteorites_plusCourt() {
        // Setup
        Sonar sonar = SonarForValidationTest.sonar();

        // Test
        JournalDeBord journalDeBord = piloteAutomatique.voyage(secteur(2, 0), secteur(5, 4), sonar, allActivated);

        // Assertions
        assertParcoursValid(journalDeBord.getParcours(), secteur(2, 0), secteur(5, 4));
        assertParcoursWithoutMeteorites(journalDeBord.getParcours(), sonar);
        assertThat(journalDeBord.getParcours()).hasSize(8);
    }


    /**
     * Test for problem :
     *
     *     - M A M - -
     *     - - - M - -
     *     - - - M - -
     *     - M M - - -
     *     - - - - - B
     *     - - - - - -
     */
    @Test(timeout = 1000)
    @Score(100)
    public void check_parcours_arriverADestination() {
        // Setup
        Sonar sonar = SonarForValidationTest.sonar()
                .avecMeteorite(secteur(1,0))
                .avecMeteorite(secteur(3,0))
                .avecMeteorite(secteur(3,1))
                .avecMeteorite(secteur(3,2))
                .avecMeteorite(secteur(2,3))
                .avecMeteorite(secteur(1,3));

        // Test
        JournalDeBord journalDeBord = piloteAutomatique.voyage(secteur(2, 0), secteur(5, 4), sonar, allActivated);

        // Assertions
        assertParcoursValid(journalDeBord.getParcours(), secteur(2, 0), secteur(5, 4));
        assertParcoursWithoutMeteorites(journalDeBord.getParcours(), sonar);
    }

    /**
     * Test for problem :
     *                         1 1 1 1 1 1 1
     *     0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6
     *   0 - - - - - - - - - - - - - - - - -
     *   1 - - - - M M M M - M - - - - - M -
     *   2 - - - M - - - - - - M M - - - M -
     *   3 - M M M - M M M M - M - - - - - -
     *   4 - - - M - - - A - - M - - - - M -
     *   5 - - - - M M M M M M - M M - M - -
     *   6 - - - - - M B M - - - - - - - - -
     *   7 - - - - - - - - - - - - - - - - -
     */
    @Test(timeout = 1000)
    @Score(200)
    public void check_parcours_arriverADestination_complexe_plusCourtChemin() {
        // Setup
        Sonar sonar = SonarForValidationTest.sonar()
                .avecMeteorite(secteur(1,3))
                .avecMeteorite(secteur(2,3))
                .avecMeteorite(secteur(3,2))
                .avecMeteorite(secteur(3,3))
                .avecMeteorite(secteur(3,4))
                .avecMeteorite(secteur(4,1))
                .avecMeteorite(secteur(4,5))
                .avecMeteorite(secteur(5,1))
                .avecMeteorite(secteur(5,3))
                .avecMeteorite(secteur(5,5))
                .avecMeteorite(secteur(5,6))
                .avecMeteorite(secteur(6,1))
                .avecMeteorite(secteur(6,3))
                .avecMeteorite(secteur(6,5))
                .avecMeteorite(secteur(7,1))
                .avecMeteorite(secteur(7,3))
                .avecMeteorite(secteur(7,5))
                .avecMeteorite(secteur(7,6))
                .avecMeteorite(secteur(8,3))
                .avecMeteorite(secteur(8,5))
                .avecMeteorite(secteur(9,1))
                .avecMeteorite(secteur(9,5))
                .avecMeteorite(secteur(10,2))
                .avecMeteorite(secteur(10,3))
                .avecMeteorite(secteur(10,4))
                .avecMeteorite(secteur(11,2))
                .avecMeteorite(secteur(11,5))
                .avecMeteorite(secteur(12,5))
                .avecMeteorite(secteur(14,5))
                .avecMeteorite(secteur(15,1))
                .avecMeteorite(secteur(15,2))
                .avecMeteorite(secteur(15,4));

        // Test
        JournalDeBord journalDeBord = piloteAutomatique.voyage(secteur(7, 4), secteur(6, 6), sonar, allActivated);

        // Assertions
        assertParcoursValid(journalDeBord.getParcours(), secteur(7, 4), secteur(6, 6));
        assertParcoursWithoutMeteorites(journalDeBord.getParcours(), sonar);
        assertThat(journalDeBord.getParcours()).hasSize(27);
    }

    /**
     * Test for problem :
     *
     *     A M
     *     M B
     */
    @Test(timeout = 1000)
    @Score(50)
    public void check_parcours_voyageImpossible() {
        // Setup
        Sonar sonar = SonarForValidationTest.sonar()
                .avecMeteorite(secteur(1,0))
                .avecMeteorite(secteur(0,1));

        // Test
        JournalDeBord journalDeBord = piloteAutomatique.voyage(secteur(0, 0), secteur(1, 1), sonar, allActivated);

        // Assertions
        assertThat(journalDeBord.getParcours()).hasSize(0);
        assertAlertesContains(journalDeBord.getAlertes(), true, alerte(secteur(0,0), "MESSAGE_EMIS", "Voyage impossible"));

    }

    /************************************/
    /*****  2 ALERTE VAISSEAU ENEMI *****/
    /************************************/

    @Test(timeout = 1000)
    @Score(60)
    public void check_messageVaisseau_AlerteLeve() {
        // Setup
        Sonar sonar = SonarForValidationTest.sonar().avecVaisseauEnnemi(secteur(0, 0));

        // Test
        JournalDeBord journalDeBord = piloteAutomatique.voyage(secteur(0, 0), secteur(0, 0), sonar, allActivated);

        // Assertions
        assertAlertesContains(journalDeBord.getAlertes(), false,
                alerte(secteur(0, 0), "VAISSEAU", ""));
    }

    @Test(timeout = 1000)
    @Score(40)
    public void check_messageVaisseau_AlerteNonLeve_siDesactive() {
        // Setup
        Sonar sonar = SonarForValidationTest.sonar().avecVaisseauEnnemi(secteur(0, 0));

        // Test
        JournalDeBord previousJournalDeBord = piloteAutomatique.voyage(secteur(0, 0), secteur(0, 0), sonar, allActivated);
        JournalDeBord journalDeBord = piloteAutomatique.voyage(secteur(0, 0), secteur(0, 0), sonar, desactivated);

        // Assertions
        assertAlertesContains(previousJournalDeBord.getAlertes(), false,
                alerte(secteur(0, 0), "VAISSEAU", ""));
        assertThat(journalDeBord.getAlertes()).isNotNull().isEmpty();
    }

    @Test(timeout = 1000)
    @Score(100)
    public void check_messageVaisseau_AlerteLeve_avec_message() {
        // Setup
        Sonar sonar = SonarForValidationTest.sonar()
                .avecVaisseauEnnemi(secteur(0, 0))
                .avecVaisseauEnnemi(secteur(1, 1))
                .avecVaisseauEnnemi(secteur(2, 0));

        // Test
        JournalDeBord journalDeBord = piloteAutomatique.voyage(secteur(1, 1), secteur(1, 1), sonar, allActivated);

        // Assertions
        assertAlertesContains(journalDeBord.getAlertes(), true,
                alerte(secteur(1, 1), "VAISSEAU", "3"));
    }


    /**************************************/
    /***** 3 ALERTE MESSAGE INTERCEPTE ****/
    /**************************************/

    @Test(timeout = 1000)
    @Score(60)
    public void check_messageIntercepte_AlerteLeve() {
        // Setup
        Sonar sonar = SonarForValidationTest.sonar().avecMessage(secteur(0, 0), "M Q CSYV JEXLIV");

        // Test
        JournalDeBord journalDeBord = piloteAutomatique.voyage(secteur(0, 0), secteur(0, 0), sonar, allActivated);

        // Assertions
        assertAlertesContains(journalDeBord.getAlertes(), false,
                alerte(secteur(0, 0), "MESSAGE_INTERCEPTE", ""));
    }

    @Test(timeout = 1000)
    @Score(40)
    public void check_messageIntercepte_AlerteNonLeve_siDesactive() {
        // Setup
        Sonar sonar = SonarForValidationTest.sonar().avecMessage(secteur(0, 0), "M Q CSYV JEXLIV");

        // Test
        JournalDeBord previousJournalDeBord = piloteAutomatique.voyage(secteur(0, 0), secteur(0, 0), sonar, allActivated);
        JournalDeBord journalDeBord = piloteAutomatique.voyage(secteur(0, 0), secteur(0, 0), sonar, desactivated);

        // Assertions
        assertAlertesContains(previousJournalDeBord.getAlertes(), false,
                alerte(secteur(0, 0), "MESSAGE_INTERCEPTE", ""));
        assertThat(journalDeBord.getAlertes()).isNotNull().isEmpty();
    }

    @Test(timeout = 1000)
    @Score(900)
    public void check_messageIntercepte_AlerteLeve_avec_messageDecrypte() {
        // Setup
        Sonar sonar1 = SonarForValidationTest.sonar().avecMessage(secteur(0, 0), "M Q CSYV JEXLIV");
        Sonar sonar2 = SonarForValidationTest.sonar().avecMessage(secteur(0, 0), "WSJX FSRLIYV UYSXMHMIR");
        Sonar sonar3 = SonarForValidationTest.sonar().avecMessage(secteur(0, 0), "E PSRK PSRK XMQI EKS");

        // Test
        JournalDeBord journalDeBord1 = piloteAutomatique.voyage(secteur(0, 0), secteur(0, 0), sonar1, allActivated);
        JournalDeBord journalDeBord2 = piloteAutomatique.voyage(secteur(0, 0), secteur(0, 0), sonar2, allActivated);
        JournalDeBord journalDeBord3 = piloteAutomatique.voyage(secteur(0, 0), secteur(0, 0), sonar3, allActivated);

        // Assertions
        assertAlertesContains(journalDeBord1.getAlertes(), true,
                alerte(secteur(0, 0), "MESSAGE_INTERCEPTE", "I M YOUR FATHER"));
        assertAlertesContains(journalDeBord2.getAlertes(),true,
                alerte(secteur(0, 0), "MESSAGE_INTERCEPTE", "SOFT BONHEUR QUOTIDIEN"));
        assertAlertesContains(journalDeBord3.getAlertes(),true,
                alerte(secteur(0, 0), "MESSAGE_INTERCEPTE", "A LONG LONG TIME AGO"));
    }

    /************************************/
    /************************************/
    /*****    TEST POUR LA PHASE 2  *****/
    /************************************/
    /************************************/


    /**************************************/
    /***** 4 PARCOURS AVEC BOUCLIER    ****/
    /**************************************/

    /**
     * Test for problem :
     *
     *     A - M M B
     *     M M M M -
     *     M M M - -
     *     M M - - -
     */
    @Test(timeout = 1000)
    @Score(200)
    public void check_parcours_avecBouclier() {
        // Setup
        Sonar sonar = SonarForValidationTest.sonar()
                .avecMeteorite(secteur(0,1))
                .avecMeteorite(secteur(0,2))
                .avecMeteorite(secteur(0,3))
                .avecMeteorite(secteur(1,1))
                .avecMeteorite(secteur(1,2))
                .avecMeteorite(secteur(1,3))
                .avecMeteorite(secteur(2,0))
                .avecMeteorite(secteur(2,1))
                .avecMeteorite(secteur(2,2))
                .avecMeteorite(secteur(3,0))
                .avecMeteorite(secteur(3,1));

        // Test
        JournalDeBord journalDeBordAvecBouclier = piloteAutomatique.voyage(secteur(0, 0), secteur(4, 0), sonar, allActivatedDeuxiemePhase);
        JournalDeBord journalDeBordSansBouclier = piloteAutomatique.voyage(secteur(0, 0), secteur(4, 0), sonar, allActivated);

        // Assertions
        assertParcoursValid(journalDeBordAvecBouclier.getParcours(), secteur(0, 0), secteur(4, 0));
        assertThat(journalDeBordAvecBouclier.getParcours()).hasSize(5);

        assertThat(journalDeBordSansBouclier.getParcours()).hasSize(0);
        assertAlertesContains(journalDeBordSansBouclier.getAlertes(), true, alerte(secteur(0,0), "MESSAGE_EMIS", "Voyage impossible"));
    }

    /**
     * Test for problem :
     *
     *     A M M M B -
     *     - M M M - -
     *     - - M M - -
     *     - M M M - -
     *     - M M M - -
     *     - - - - - -
     */
    @Test(timeout = 1000)
    @Score(300)
    public void check_parcours_avecBouclier_complexe() {
        // Setup
        Sonar sonar = SonarForValidationTest.sonar()
                .avecMeteorite(secteur(1,0))
                .avecMeteorite(secteur(2,0))
                .avecMeteorite(secteur(3,0))
                .avecMeteorite(secteur(1,1))
                .avecMeteorite(secteur(2,1))
                .avecMeteorite(secteur(3,1))
                .avecMeteorite(secteur(2,2))
                .avecMeteorite(secteur(3,2))
                .avecMeteorite(secteur(1,3))
                .avecMeteorite(secteur(2,3))
                .avecMeteorite(secteur(3,3))
                .avecMeteorite(secteur(1,4))
                .avecMeteorite(secteur(2,4))
                .avecMeteorite(secteur(3,4));

        // Test
        JournalDeBord journalDeBordAvecBouclier = piloteAutomatique.voyage(secteur(0, 0), secteur(4, 0), sonar, allActivatedDeuxiemePhase);
        JournalDeBord journalDeBordSansBouclier = piloteAutomatique.voyage(secteur(0, 0), secteur(4, 0), sonar, allActivated);

        // Assertions
        assertParcoursExactlySame(journalDeBordAvecBouclier.getParcours(),
                Arrays.asList(
                        secteur(0,0),
                        secteur(0,1),
                        secteur(0,2),
                        secteur(1,2),
                        secteur(2,2),
                        secteur(3,2),
                        secteur(4,2),
                        secteur(4,1),
                        secteur(4,0)));
        assertParcoursValid(journalDeBordSansBouclier.getParcours(), secteur(0, 0), secteur(4, 0));
        assertParcoursWithoutMeteorites(journalDeBordSansBouclier.getParcours(), sonar);
    }



    /********************************/
    /***** 5 EMISSION MESSAGES   ****/
    /********************************/

    @Test(timeout = 1000)
    @Score(60)
    public void check_messageEmis_pour_messageVaisseau_Crypte() {
        // Setup
        Sonar sonar = SonarForValidationTest.sonar()
                .avecVaisseauEnnemi(secteur(0, 0))
                .avecVaisseauEnnemi(secteur(1, 1))
                .avecVaisseauEnnemi(secteur(2, 0));

        // Test
        JournalDeBord journalDeBord = piloteAutomatique.voyage(secteur(1, 1), secteur(1, 1), sonar, allActivatedDeuxiemePhase);

        // Assertions
        assertAlertesContains(journalDeBord.getAlertes(), true,
                alerte(secteur(1, 1), "VAISSEAU", "3"),
                alerte(secteur(1, 1), "MESSAGE_EMIS", "14"));
    }

    @Test(timeout = 1000)
    @Score(40)
    public void check_messageEmis_pour_messageVaisseau_Crypte_siDesactive() {
        // Setup
        Sonar sonar = SonarForValidationTest.sonar()
                .avecVaisseauEnnemi(secteur(0, 0))
                .avecVaisseauEnnemi(secteur(1, 1))
                .avecVaisseauEnnemi(secteur(2, 0));

        // Test
        JournalDeBord journalDeBord = piloteAutomatique.voyage(secteur(1, 1), secteur(1, 1), sonar, allActivatedDeuxiemePhase);
        JournalDeBord journalDeBordSansEmettre = piloteAutomatique.voyage(secteur(1, 1), secteur(1, 1), sonar, allActivated);

        // Assertions
        assertAlertesContains(journalDeBord.getAlertes(), true,
                alerte(secteur(1, 1), "VAISSEAU", "3"),
                alerte(secteur(1, 1), "MESSAGE_EMIS", "14"));
        assertAlertesContains(journalDeBordSansEmettre.getAlertes(), true,
                alerte(secteur(1, 1), "VAISSEAU", "3"));
        assertThat(journalDeBordSansEmettre.getAlertes()).hasSize(1);
    }

    @Test(timeout = 1000)
    @Score(900)
    public void check_messageEmis_pour_messageIntercepte_Decrypte_et_CryptePourNous() {
        // Setup
        Sonar sonar = SonarForValidationTest.sonar().avecMessage(secteur(0, 0), "M Q CSYV JEXLIV");

        // Test
        JournalDeBord journalDeBord = piloteAutomatique.voyage(secteur(0, 0), secteur(0, 0), sonar, allActivatedDeuxiemePhase);

        // Assertions
        assertAlertesContains(journalDeBord.getAlertes(), true,
                alerte(secteur(0, 0), "MESSAGE_INTERCEPTE", "I M YOUR FATHER"),
                alerte(secteur(0, 0), "MESSAGE_EMIS", "11 51 33026232 500052014032"));
    }

    @Test(timeout = 1000)
    @Score(1400)
    public void check_messageEmis_pour_messageIntercepte_NonDecrypte_et_CryptePourNous() {
        // Setup
        Sonar sonar = SonarForValidationTest.sonar().avecMessage(secteur(0, 0), "M Q CSYV JEXLIV");

        // Test
        JournalDeBord journalDeBord = piloteAutomatique.voyage(secteur(0, 0), secteur(0, 0), sonar, allActivatedDeuxiemePhase);

        // Assertions
        assertAlertesContains(journalDeBord.getAlertes(), true,
                alerte(secteur(0, 0), "MESSAGE_INTERCEPTE", "M Q CSYV JEXLIV"),
                alerte(secteur(0, 0), "MESSAGE_EMIS", "51 22 20423303 214023411103"));
    }

}
