Feature: Travels

	Scenario: Travel with meteorites
		Given a group of meteorites at sector '1-0'
		When I calculate the travel from '0-0' to '1-1'
		Then the travel steps are '0-0, 0-1, 1-1'
		And there is 0 alert

	Scenario: Travel with intercepted message
		Given the activated functionnality 'INTERCEPTER'
		And a message 'EPIVXI QSR GETMXEMRI' at sector '1-0'
		When I calculate the travel from '0-0' to '2-0'
		Then there is 1 alert
		And an alert of type 'MESSAGE_INTERCEPTE' with content 'ALERTE MON CAPITAINE' is raised at sector '1-0'
		And the travel steps are '0-0, 1-0, 2-0'

	Scenario: Travel with detected ship
		Given the activated functionnality 'VAISSEAU'
		And a ship at sector '0-2'
		And a ship at sector '1-3'
		When I calculate the travel from '0-0' to '0-5'
		Then there is 4 alerts
		And an alert of type 'VAISSEAU' with content '1' is raised at sector '0-1'
		And an alert of type 'VAISSEAU' with content '2' is raised at sector '0-2'
		And an alert of type 'VAISSEAU' with content '2' is raised at sector '0-3'
		And an alert of type 'VAISSEAU' with content '1' is raised at sector '0-4'
		And the travel steps are '0-0, 0-1, 0-2, 0-3, 0-4, 0-5'
