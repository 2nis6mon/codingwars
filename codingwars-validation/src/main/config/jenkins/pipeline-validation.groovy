def gitServerUrl = 'http://dvdsi320w.creteil.francetelecom.fr:8888'
def restApiUrl = 'http://dvdsi320w.creteil.francetelecom.fr:7000'
def sprintPenality = 500

node('battlecode && validate') {
	try {
		stage("Clone $team repository") {
			httpRequest httpMode: 'PATCH', url: "$restApiUrl/api/metrics/metrics-$team/build/building", validResponseCodes: '200', consoleLogResponseBody: true
			git url: "$gitServerUrl/$team/codingwars.git", credentialsId: 'gitlab-battlecode-admin'
		}
		stage('Build implementation') {
			timeout(2) {
				maven 'clean cobertura:cobertura', ['maven.test.failure.ignore': true, 'cobertura.report.format': 'xml']
			}
			maven 'verify', ['maven.test.skip': true, 'jar.finalName': "codingwars-$team"]
		}
		def bonusMalus
		stage('Publish implementation') {
			def metrics = loadCoverageMetrics() + loadTestsMetrics()
			bonusMalus = computeBonusMalus(metrics, sprintPenality)
			httpRequest httpMode: 'POST', url: "$restApiUrl/api/metrics", requestBody: serializeTeamMetrics(team, metrics + bonusMalus, 'validating'),
					contentType: 'APPLICATION_JSON', validResponseCodes: '200', consoleLogResponseBody: true
			maven 'install:install-file', [file: "target/codingwars-${team}.jar",
					groupId: 'com.dojocoders', artifactId: "codingwars-$team",
					version: '1.0-SNAPSHOT', pomFile: 'pom.xml']
		}
		stage('Clone validation repository') {
			deleteDir()
			git url: "$gitServerUrl/battlecode-admin/codingwars-validation.git", credentialsId: 'gitlab-battlecode-admin'
		}
		stage('Validation implementation') {
			timeout(2) {
				maven 'clean test', bonusMalus + ['maven.test.failure.ignore': true,
						team: team, 'score.rest-api.url': restApiUrl]
			}
			def metrics = loadTestsMetrics()
			def validationStatus = "${metrics.numberOfTests - metrics.numberOfFailures - metrics.numberOfSkippedTests}/${metrics.numberOfTests}"
			httpRequest httpMode: 'POST', url: "$restApiUrl/api/metrics", requestBody: serializeTeamMetrics(team, [validation: validationStatus], 'pass'),
					contentType: 'APPLICATION_JSON', validResponseCodes: '200', consoleLogResponseBody: true
		}
	} catch (e) {
		httpRequest httpMode: 'POST', url: "$restApiUrl/api/scores/add/$team/-$sprintPenality", validResponseCodes: '200', consoleLogResponseBody: true
		httpRequest httpMode: 'PATCH', url: "$restApiUrl/api/metrics/metrics-$team/build/fail", validResponseCodes: '200', consoleLogResponseBody: true
		throw e
	}
}

def Closure maven(task, properties) {
	withEnv(["PATH+MAVEN=${tool 'Maven'}/bin"]) {
		def mavenCommandLine = "mvn -B $task ${joinProperties(properties)}"
		if(isUnix()) {
			sh mavenCommandLine
		} else {
			bat mavenCommandLine
		}
	}
}

@NonCPS
def joinProperties(properties) {
    properties.collect{ key, value -> "\"-D$key=$value\"" }.join(' ')
}

@NonCPS
def serializeTeamMetrics(team, metrics, buildStatus) {
    groovy.json.JsonOutput.toJson([team: "metrics-$team", metrics: metrics + [build: buildStatus]])
}

def loadCoverageMetrics() {
	if(fileExists('target/site/cobertura/coverage.xml')) {
		def coverageContent = readFile('target/site/cobertura/coverage.xml')
		return loadCoverage(coverageContent)
	} else {
		return [lineCoverage: 0.0, branchCoverage: 0.0, complexity: 0.0]
	}
}

def loadTestsMetrics() {
	def testFiles = findFiles(glob: 'target/surefire-reports/*.xml')
	def testResultContent = [:]
	for(int i = 0 ; i < testFiles.size() ; ++i) {
		def file = testFiles[i]
		testResultContent.put(file.name, readFile("target/surefire-reports/$file.name"))
	}
	return loadTestsResult(testResultContent)
}

@NonCPS
def loadCoverage(coverageContent) {
	def results = [:]
	def parser = new XmlSlurper(false, true, true)
	parser.setFeature('http://apache.org/xml/features/nonvalidating/load-external-dtd', false)
	parser.setFeature('http://xml.org/sax/features/namespaces', false)

	def coverage = parser.parseText(coverageContent)
	results += [lineCoverage: Double.parseDouble(coverage.attributes().'line-rate')]
	results += [branchCoverage: Double.parseDouble(coverage.attributes().'branch-rate')]
	results += [complexity: Double.parseDouble(coverage.attributes().complexity)]
	return results
}

@NonCPS
def loadTestsResult(testResultContent) {
	def results = [:]
	def parser = new XmlSlurper()
	def numberOfTests = 0
	def numberOfFailures = 0
	def numberOfSkippedTests = 0
	testResultContent.each { filename, content ->
		def testResult = parser.parseText(content)
		numberOfTests += Integer.parseInt(testResult.attributes().tests)
		numberOfFailures += Integer.parseInt(testResult.attributes().failures)
		numberOfFailures += Integer.parseInt(testResult.attributes().errors)
		numberOfSkippedTests += Integer.parseInt(testResult.attributes().skipped)
	}
	results += [numberOfTests: numberOfTests]
	results += [numberOfFailures: numberOfFailures]
	results += [numberOfSkippedTests: numberOfSkippedTests]
	return results
}

@NonCPS
def computeBonusMalus(metrics, sprintPenality) {
	def percentOfSuccessTests = (metrics.numberOfTests == 0) ? 0 : (double) (metrics.numberOfTests - metrics.numberOfFailures - metrics.numberOfSkippedTests) / (double) metrics.numberOfTests
	def penalities = sprintPenality
	// ratio entre 0.5 et 1.5, défini par la couverture du code pondéré du pourcentage de succès des tests
	def ratio = 0.5 + (percentOfSuccessTests * metrics.branchCoverage)
	// On quadruple la mise si le ratio est positif. Ratio final compris entre 0.5 et 3
	if(ratio > 1) {
		ratio += (ratio - 1)*3
	}
	return [bonusMalus: -penalities, scoreCoefficient: ratio]
}
