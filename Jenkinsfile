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

timestamps() {
    node() {
        def mvnHome = tool 'apache-maven-3.0.5'
        def mvnParams = '--batch-mode --update-snapshots -fae -Dmaven.repo.local=xpect-local-maven-repository -DtestOnly=false'
        timeout(time: 1, unit: 'HOURS') {
            stage('prepare workspace') {
                step([$class: 'WsCleanup'])
                // we need to live with detached head, or we need to adjust settings:
                // https://issues.jenkins-ci.org/browse/JENKINS-42860
                checkout scm
            }
            stage('log configuration') {
                echo("===== checking tools versions =====")
                sh """\
                       git config --get remote.origin.url
                       git reset --hard
                       pwd
                       ls -la
                       ${mvnHome}/bin/mvn -v
                   """
                echo("===================================")
            }

            stage('compile with Eclipse Luna and Xtext 2.9.2') {
                sh "${mvnHome}/bin/mvn -P!tests -Dtarget-platform=eclipse_4_4_2-xtext_2_9_2 ${mvnParams} clean install"
                archive 'org.eclipse.xpect.releng/p2-repository/target/repository/**/*.*'
            }

            wrap([$class: 'Xvnc', useXauthority: true]) {

                stage('test with Eclipse Luna and Xtext 2.9.2') {
                    try{
                        sh "${mvnHome}/bin/mvn -P!plugins -P!xtext-examples -Dtarget-platform=eclipse_4_4_2-xtext_2_9_2 ${mvnParams} clean integration-test"
                    }finally{
                        junit '**/TEST-*.xml'
                    }
                }

                stage('test with Eclipse Mars and Xtext 2.14') {
                    try{
                        sh "${mvnHome}/bin/mvn -P!plugins -P!xtext-examples -Dtarget-platform=eclipse_4_5_0-xtext_2_14_0 ${mvnParams} clean integration-test"
                    }finally {
                        junit '**/TEST-*.xml'
                    }
                }

                stage('test with Eclipse 2018-12 and Xtext nighly') {
                    try{
                        sh "${mvnHome}/bin/mvn -P!plugins -P!xtext-examples -Dtarget-platform=eclipse_4_10_0-xtext_nightly ${mvnParams} clean integration-test"
                    }finally {
                        junit '**/TEST-*.xml'
                    }
                }
            }
            
            if(env.BRANCH_NAME.toLowerCase() == 'master') {
                stage('deploy') {
                    def settings = "-s /opt/public/hipp/homes/genie.xpect/.m2/settings-deploy-ossrh.xml"
                    sh "${mvnHome}/bin/mvn -P!tests -P maven-publish -Dtarget-platform=eclipse_4_4_2-xtext_2_9_2 ${settings} ${mvnParams} clean deploy"
                }
            }
        }
    }
}
