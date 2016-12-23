def gitServerUrl = 'http://dvdsi320w.creteil.francetelecom.fr:8888/battlecode-admin'
def restApiUrl = 'http://dvdsi320w.creteil.francetelecom.fr:7000'

job('Seed-battlecode-configuration') {
    scm {
        git {
            remote {
                url ("$gitServerUrl/codingwars-validation.git")
                credentials ('gitlab-battlecode-admin')
            }
            branch('master')
        }
    }
    triggers {
        cron '0 H * * *'
        scm 'H/5 * * * *'
    }
    steps {
		jobDsl {
			targets 'src/main/config/jenkins/jobs.groovy'
			removedJobAction 'DELETE'
			removedViewAction 'DELETE'
			failOnMissingPlugin true
			unstableOnDeprecation true
		}
	}
}

pipelineJob('Register-battlecode-teams') {
	concurrentBuild false
	triggers {
		upstream('Seed-battlecode-configuration', 'SUCCESS')
	}
	definition {
		cps {
			sandbox true
			script getTeams().collect{ team -> "httpRequest httpMode: 'POST', url: '$restApiUrl/api/team/$team', validResponseCodes: '200,500', consoleLogResponseBody: true" }.join('\n')

		}
	}
}

job('Build-battlecode-framework') {
    label('battlecode && build')
    scm {
        git {
            remote {
                url ("$gitServerUrl/score-framework.git")
                credentials ('gitlab-battlecode-admin')
            }
            branch('master')
        }
    }
    triggers {
        scm 'H/5 * * * *'
    }
    steps {
        shell('''\
cd score-ihm &&
rm -rf node_modules &&
npm config set proxy http://10.177.100.95:8080 &&
npm install
npm run tsc
''')
        maven {
            mavenInstallation ('Maven')
            goals ('clean deploy')
            properties ('maven.test.failure.ignore': true, performRelease: true)
        }
    }
}

job('Build-battlecode-exercise') {
    label('battlecode && build')
    scm {
        git {
            remote {
                url ("$gitServerUrl/codingwars-api.git")
                credentials ('gitlab-battlecode-admin')
            }
            branch('master')
        }
    }
    triggers {
        scm 'H/5 * * * *'
    }
    steps {
        maven {
            mavenInstallation ('Maven')
            goals ('clean deploy')
            properties (performRelease: true)
        }
    }
}

job('Build-battlecode-template') {
    label('battlecode && build')
    scm {
        git {
            remote {
                url ("$gitServerUrl/codingwars.git")
                credentials ('gitlab-battlecode-admin')
            }
            branch('master')
        }
    }
    triggers {
        scm 'H/5 * * * *'
    }
    steps {
        maven {
            mavenInstallation ('Maven')
            goals ('clean install')
            properties ('maven.test.failure.ignore': true)
        }
    }
}

job('Build-battlecode-validation') {
    label('battlecode && build')
    scm {
        git {
            remote {
                url ("$gitServerUrl/codingwars-validation.git")
                credentials ('gitlab-battlecode-admin')
            }
            branch('master')
        }
    }
    triggers {
        scm 'H/5 * * * *'
    }
    steps {
        maven {
            mavenInstallation ('Maven')
            goals ('clean verify')
            properties ('maven.test.failure.ignore': true, team: 'battlecode-admin', 'score.rest-api.url': restApiUrl)
        }
    }
}

def getTeams() {
	hudson.model.User.all.findAll{ team ->
		team.id != 'admin' && team.getProperty(hudson.security.HudsonPrivateSecurityRealm.Details) != null
	}*.id
}

pipelineJob('Run-battlecode-sprint') {
	concurrentBuild false
	authenticationToken 'battlecode-token'
	definition {
		cps {
			sandbox true
			script generateSprintScript(restApiUrl)
		}
	}
}

def generateSprintScript(restApiUrl) {
	def resetMetrics = getTeams().collect{ team -> "httpRequest httpMode: 'DELETE', url: '$restApiUrl/api/metrics/metrics-$team', validResponseCodes: '200', consoleLogResponseBody: true" }.join('\n')
	def launchTeamJobs = getTeams().collect{ team -> "build job: 'Validate-$team-battlecode-implementation', wait: false" }.join('\n')
	return resetMetrics + '\n' + launchTeamJobs + '\n'
}

getTeams().each { team ->
	pipelineJob("Validate-$team-battlecode-implementation") {
        label('battlecode && validate')
		concurrentBuild false
		parameters {
			stringParam('team', team)
		}
		definition {
			cpsScm {
				scm {
					git {
						remote {
							url ("$gitServerUrl/codingwars-validation.git")
							credentials ('gitlab-battlecode-admin')
						}
						branch('master')
					}
				}
				scriptPath ('src/main/config/jenkins/pipeline-validation.groovy')
			}
		}
	}
}
