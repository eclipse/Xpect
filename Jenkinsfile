// tell Jenkins how to build projects from this repository
node {
	try {
		def mvnHome = tool 'M3'
		def mvnParams = '--batch-mode --update-snapshots -fae -Dmaven.repo.local=xpect-local-maven-repository -DtestOnly=false clean install'

		stage 'Compile - Xtext 2.9.2'
		checkout scm
		sh "${mvnHome}/bin/mvn -P!tests -Dtarget-platform=eclipse_4_5_0-xtext_2_9_2 ${mvnParams}"
		archive 'build/**/*.*'
		
		stage 'Test'
		wrap([$class:'Xvnc', useXauthority: true]) {
		
			stage 'Test - Xtext 2.9.2'
			sh "${mvnHome}/bin/mvn -P!plugins -Dtarget-platform=eclipse_4_5_0-xtext_2_9_2 ${mvnParams}"
			
			stage 'Test - Xtext Nightly'
			sh "${mvnHome}/bin/mvn -P!plugins -Dtarget-platform=eclipse_4_4_0-xtext_nightly ${mvnParams}"
		}
				
		// slackSend "Build Succeeded - ${env.JOB_NAME} ${env.BUILD_NUMBER} (<${env.BUILD_URL}|Open>)"
		
	} catch (e) {
		// slackSend color: 'danger', message: "Build Failed - ${env.JOB_NAME} ${env.BUILD_NUMBER} (<${env.BUILD_URL}|Open>)"
		throw e
	} finally {
		step([$class: 'JUnitResultArchiver', testResults: '**/target/surfire-reports-standalone/*.xml'])
		step([$class: 'JUnitResultArchiver', testResults: '**/target/surfire-reports-plugin/*.xml'])
	}
}  