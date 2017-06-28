// tell Jenkins how to build projects from this repository
node {
	
	def mvnHome = tool 'M3'
	def mvnParams = '--batch-mode --update-snapshots -fae -Dmaven.repo.local=xpect-local-maven-repository -DtestOnly=false'

	stage 'Compile Xtext-2.9.2'
	checkout scm
	sh "${mvnHome}/bin/mvn -P!tests -Dtarget-platform=eclipse_4_5_0-xtext_2_9_2 ${mvnParams} clean install"
	archive 'build/**/*.*'
	
	wrap([$class:'Xvnc', useXauthority: true]) {
		
		stage 'Test Xtext-2.9.2'
		try {
			sh "${mvnHome}/bin/mvn -P!plugins -P!xtext-examples -Dtarget-platform=eclipse_4_5_0-xtext_2_9_2 ${mvnParams} clean integration-test"
		} finally {
			step([$class: 'JUnitResultArchiver', testResults: '**/target/surfire-reports-standalone/*.xml'])
			step([$class: 'JUnitResultArchiver', testResults: '**/target/surfire-reports-plugin/*.xml'])
		}
			
		stage 'Test Xtext-Nightly'
		try {
			sh "${mvnHome}/bin/mvn -P!plugins -P!xtext-examples -Dtarget-platform=eclipse_4_5_0-xtext_nightly ${mvnParams} clean integration-test."
		} finally {
			step([$class: 'JUnitResultArchiver', testResults: '**/target/surfire-reports-standalone/*.xml'])
			step([$class: 'JUnitResultArchiver', testResults: '**/target/surfire-reports-plugin/*.xml'])
		}
		
	}
}  