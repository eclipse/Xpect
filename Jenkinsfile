// tell Jenkins how to build projects from this repository
node {
	try {
		def mvnHome = tool 'M3'

		stage 'Build'
		checkout scm
		sh "${mvnHome}/bin/mvn --batch-mode --update-snapshots -fae -Dmaven.repo.local=xpect-local-maven-repository -DtestOnly=false clean install"
		archive 'build/**/*.*'
		
		stage 'Test'
		wrap([$class:'Xvnc', useXauthority: true]) {
			sh "${mvnHome}/bin/mvn --batch-mode --update-snapshots -fae -Dmaven.repo.local=xpect-local-maven-repository -DtestOnly=true clean install"
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