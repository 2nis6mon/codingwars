package com.dojocoders.codingwars.cucumber;

import static org.fest.assertions.api.Assertions.assertThat;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.dojocoders.codingwars.PiloteAutomatique;
import com.dojocoders.codingwars.model.Alerte;
import com.dojocoders.codingwars.model.JournalDeBord;
import com.dojocoders.codingwars.model.Secteur;
import com.dojocoders.codingwars.model.Sonar;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

/**
 * See src/test/resources for cucumber scenarios
 */
public class PiloteAutomatiqueCucumberDefinitions {

	private PiloteAutomatique autopilot = null; //new MyPiloteAutomatiqueImplementation();

	private Set<String> functionnalities = new HashSet<String>();
	private Set<Secteur> meteorites = new HashSet<Secteur>();
	private Set<Secteur> ships = new HashSet<Secteur>();
	private Map<Secteur, String> messages = new HashMap<Secteur, String>();

	private List<Secteur> travelSteps;
	private List<Alerte> alerts;
	private Map<Secteur, String> shipAlerts = new HashMap<Secteur, String>();
	private Map<Secteur, String> messagesAlerts = new HashMap<Secteur, String>();

	@Given("the activated functionnality '(.*)'")
	public void activate_fonctionnality(String functionnality) {
		this.functionnalities.add(functionnality);
	}

	@Given("a group of meteorites at sector '(.*)'")
	public void meteorites_at_sector(String sector) {
		Secteur parsedSector = buildIndexableSector(sector);
		this.meteorites.add(parsedSector);
	}

	@Given("a ship at sector '(.*)'")
	public void ship_at_sector(String sector) {
		Secteur parsedSector = buildIndexableSector(sector);
		this.ships.add(parsedSector);
	}

	@Given("a message '(.*)' at sector '(.*)'")
	public void message_at_sector(String message, String sector) {
		Secteur parsedSector = buildIndexableSector(sector);
		this.messages.put(parsedSector, message);
	}

	@When("I calculate the travel from '(.*)' to '(.*)'")
	public void calculate_the_travel(String from, String to) {
		Secteur fromSector = buildIndexableSector(from);
		Secteur toSector = buildIndexableSector(to);
		Sonar sonar = buildSonar();
		JournalDeBord recordbook = this.autopilot.voyage(fromSector, toSector, sonar, this.functionnalities);
		this.travelSteps = recordbook.getParcours();
		this.alerts = recordbook.getAlertes();
		indexAlerts();
	}

	@Then("the travel steps are '(.*)'")
	public void check_travel_steps(List<String> expectedSteps) {
		int stepNumber = 0;
		for (String step : expectedSteps) {
			Secteur parsedStep = buildIndexableSector(step);
			assertEqual(parsedStep, this.travelSteps.get(stepNumber), stepNumber);
			++stepNumber;
		}
		assertThat(this.travelSteps.size()).isEqualTo(expectedSteps.size());
	}

	@Then("there is (\\d+) alerts?")
	public void check_number_of_alerts(int expectedAlertsNumber) {
		assertThat(this.alerts.size()).isEqualTo(expectedAlertsNumber);
	}

	@Then("an alert of type '(.*)' with content '(.*)' is raised at sector '(.*)'")
	public void check_alert(String type, String content, String sector) {
		Secteur parsedSector = buildIndexableSector(sector);
		Map<Secteur, String> indexedAlerts = getIndexedAlertsOfType(type);
		String actualMessage = indexedAlerts.get(parsedSector);
		assertThat(actualMessage).isEqualTo(content);
	}

	private void assertEqual(Secteur expected, Secteur actual, int index) {
		assertThat(actual.getX()).as("Index " + index).isEqualTo(expected.getX());
		assertThat(actual.getY()).as("Index " + index).isEqualTo(expected.getY());
	}

	private void indexAlerts() {
		for (Alerte alert : this.alerts) {
			Secteur indexableSector = buildIndexableSector(alert.getSecteur().getX(), alert.getSecteur().getY());
			Map<Secteur, String> indexedAlerts = getIndexedAlertsOfType(alert.getType());
			indexedAlerts.put(indexableSector, alert.getMessage());
		}
	}

	private Map<Secteur, String> getIndexedAlertsOfType(String type) {
		if ("VAISSEAU".equals(type)) {
			return this.shipAlerts;
		}
		if ("MESSAGE_INTERCEPTE".equals(type)) {
			return this.messagesAlerts;
		}
		throw new IllegalArgumentException("Unknown alert of type " + type);
	}

	private Secteur buildIndexableSector(String sector) {
		String[] axis = sector.split("-");
		final int x = Integer.parseInt(axis[0]);
		final int y = Integer.parseInt(axis[1]);
		return buildIndexableSector(x, y);
	}

	private Secteur buildIndexableSector(final int x, final int y) {
		return new Secteur() {
			public int getX() {
				return x;
			}

			public int getY() {
				return y;
			}

			@Override
			public int hashCode() {
				final int prime = 31;
				int result = 1;
				result = prime * result + x;
				result = prime * result + y;
				return result;
			}

			@Override
			public boolean equals(Object obj) {
				if (!(obj instanceof Secteur)) {
					return false;
				}

				Secteur other = (Secteur) obj;
				return (x == other.getX() && y == other.getY());
			}
		};
	}

	private Sonar buildSonar() {
		return new Sonar() {
			public boolean isVaisseauEnnemi(Secteur location) {
				Secteur indexableSector = buildIndexableSector(location.getX(), location.getY());
				return ships.contains(indexableSector);
			}

			public boolean isChampMeteorites(Secteur location) {
				Secteur indexableSector = buildIndexableSector(location.getX(), location.getY());
				return meteorites.contains(indexableSector);
			}

			public String intercepterMessage(Secteur location) {
				Secteur indexableSector = buildIndexableSector(location.getX(), location.getY());
				String message = messages.get(indexableSector);
				return (message != null) ? message : "";
			}
		};
	}
}
