/*******************************************************************************
 * Copyright (c) 2012-2017 TypeFox GmbH and itemis AG.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Moritz Eysholdt - Initial contribution and API
 *******************************************************************************/

// tell Jenkins how to build projects from this repository
node {
	
	def mvnHome = tool 'M3'
	def mvnParams = '--batch-mode --update-snapshots -fae -Dmaven.repo.local=xpect-local-maven-repository -DtestOnly=false'

	stage ('compile with Xtext 2.9.2') {
		checkout scm
		sh "${mvnHome}/bin/mvn -P!tests -Dtarget-platform=eclipse_4_6_3-xtext_2_9_2 ${mvnParams} clean install"
		archive 'org.xpect.releng/p2-repository/target/repository/**/*.*'
	}
	
	wrap([$class:'Xvnc', useXauthority: true]) {
		
		stage ('test with Xtext 2.9.2') {
			sh "${mvnHome}/bin/mvn -P!plugins -P!xtext-examples -Dtarget-platform=eclipse_4_6_3-xtext_2_9_2 ${mvnParams} clean integration-test"
			junit '**/TEST-*.xml'
		}
			
		stage ('test with Xtext nighly') {
			sh "${mvnHome}/bin/mvn -P!plugins -P!xtext-examples -Dtarget-platform=eclipse_4_5_0-xtext_nightly ${mvnParams} clean integration-test"
			junit '**/TEST-*.xml'
		}
		
	}
}  