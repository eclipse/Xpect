/*******************************************************************************
 * Copyright (c) 2012-2017 TypeFox GmbH and itemis AG.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   Moritz Eysholdt - Initial contribution and API
 *******************************************************************************/

timestamps() {
    properties([
        pipelineTriggers([cron('H 2 * * *')])
    ])
    node('centos-8') {
        def javaHome = tool 'temurin-jdk11-latest'
        def java17Home = tool 'temurin-jdk17-latest'
        env.JAVA_HOME = "${javaHome}"
        def mvnHome = tool 'apache-maven-3.8.6'
        def mvnParams = '--batch-mode --update-snapshots -fae -Dmaven.repo.local=xpect-local-maven-repository -DtestOnly=false'
        timeout(time: 1, unit: 'HOURS') {
        try {
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
                       ${javaHome}/bin/java -version
                   """
                echo("===================================")
            }

            wrap([$class: 'Xvnc', useXauthority: true]) {
                stage('compile with Eclipse 2023-03 and Xtext 2.31.0') {
                    sh "${mvnHome}/bin/mvn -P!tests -Declipsesign=true -Dtarget-platform=eclipse_2023_03-xtext_2_31_0 ${mvnParams} clean install"
                    archiveArtifacts artifacts: 'org.eclipse.xpect.releng/p2-repository/target/repository/**/*.*,org.eclipse.xpect.releng/p2-repository/target/org.eclipse.xpect.repository-*.zip'
                }
            }

            wrap([$class: 'Xvnc', useXauthority: true]) {
                stage('test with Eclipse 2023-09 and Xtext nighly') {
                    try{
                        sh "JAVA_HOME=${java17Home} ${mvnHome}/bin/mvn -P!xtext-examples -Dtycho-version=2.7.5 -Dtarget-platform=eclipse_2023_09-xtext_nightly ${mvnParams} clean integration-test"
                    }finally {
                        junit '**/TEST-*.xml'
                    }
                }
            }

            if(env.BRANCH_NAME?.toLowerCase() == 'master') {
                stage('deploy') {
                    withCredentials([file(credentialsId: 'secret-subkeys.asc', variable: 'KEYRING')]) {
                        sh '''
                        rm -r xpect-local-maven-repository
                        gpg --batch --import "${KEYRING}"
                        for fpr in $(gpg --list-keys --with-colons  | awk -F: '/fpr:/ {print $10}' | sort -u);
                        do
                            echo -e "5\ny\n" | gpg --batch --command-fd 0 --expert --edit-key $fpr trust;
                        done
                        '''
                        sh "${mvnHome}/bin/mvn -P!tests -P maven-publish -Dtarget-platform=eclipse_2023_03-xtext_2_31_0 ${mvnParams} clean deploy"
                    }
                    
                }
            } else if(env.BRANCH_NAME?.toLowerCase()?.startsWith('release_')) {
                stage('deploy') {
                    withCredentials([file(credentialsId: 'secret-subkeys.asc', variable: 'KEYRING')]) {
                        sh '''
                        rm -r xpect-local-maven-repository
                        gpg --batch --import "${KEYRING}"
                        for fpr in $(gpg --list-keys --with-colons  | awk -F: '/fpr:/ {print $10}' | sort -u);
                        do
                            echo -e "5\ny\n" | gpg --batch --command-fd 0 --expert --edit-key $fpr trust;
                        done
                        '''
                        sh "${mvnHome}/bin/mvn -P!tests -P!xtext-examples -P maven-publish -Dtarget-platform=eclipse_2023_03-xtext_2_31_0 ${mvnParams} clean deploy"
                    }
                    
                }
            }
        } finally {
            def curResult = currentBuild.currentResult
            def lastResult = 'NEW'
            if (currentBuild.previousBuild != null) {
                lastResult = currentBuild.previousBuild.result
            }

            if (curResult != 'SUCCESS' || lastResult != 'SUCCESS') {
                def color = ''
                switch (curResult) {
                    case 'SUCCESS':
                        color = '#00FF00'
                        break
                    case 'UNSTABLE':
                        color = '#FFFF00'
                        break
                    case 'FAILURE':
                        color = '#FF0000'
                        break
                    default: // e.g. ABORTED
                        color = '#666666'
                }

                matrixSendMessage https: true,
                    hostname: 'matrix.eclipse.org',
                    accessTokenCredentialsId: "matrix-token",
                    roomId: '!aFWRHMCLJDZBzuNIRD:matrix.eclipse.org',
                    body: "${lastResult} => ${curResult} ${env.BUILD_URL} | ${env.JOB_NAME}#${env.BUILD_NUMBER}",
                    formattedBody: "<div><font color='${color}'>${lastResult} => ${curResult}</font> | <a href='${env.BUILD_URL}' target='_blank'>${env.JOB_NAME}#${env.BUILD_NUMBER}</a></div>"
            }
        }
        }
    }
}
