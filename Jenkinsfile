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
            stage('log configuration') {
                sh """\
                               echo "===== checking tools versions ====="
                               pwd
                               ls -ls
                               ${mvnHome}/bin/mvn -v
                               echo "==================================="
                          """
                //clone
                timeout(time: 30, unit: 'MINUTES') {
                    gitCheckout("xpect_force_clone", scm.userRemoteConfigs, scm.branches[0].name)
                }
            }

            stage('compile with Eclipse Luna and Xtext 2.9.2') {
                sh "${mvnHome}/bin/mvn -P!tests -Dtarget-platform=eclipse_4_4_2-xtext_2_9_2 ${mvnParams} clean install"
                archive 'org.eclipse.xpect.releng/p2-repository/target/repository/**/*.*'
            }

            wrap([$class: 'Xvnc', useXauthority: true]) {

                stage('test with Eclipse Luna and Xtext 2.9.2') {
                    sh "${mvnHome}/bin/mvn -P!plugins -P!xtext-examples -Dtarget-platform=eclipse_4_4_2-xtext_2_9_2 ${mvnParams} clean integration-test"
                    junit '**/TEST-*.xml'
                }

                stage('test with Eclipse Mars and Xtext nighly') {
                    sh "${mvnHome}/bin/mvn -P!plugins -P!xtext-examples -Dtarget-platform=eclipse_4_5_0-xtext_nightly ${mvnParams} clean integration-test"
                    junit '**/TEST-*.xml'
                }
            }
        }
    }
}


//======

/**
 * Helper function that performs git checkout.
 * Note that for the remote user can pass either inherited remote config,
 * or assuming you have git url like {@code git@github.com:Profile/repo.git}, then you can in place create config
 * by passing {@code [[url: git@github.com:Profile/repo.git]]}
 *
 * @param checkoutDir string with directory name where to checkout, relative to the workspace
 * @param gitRemote scm object configuring remote ({@code scm.userRemoteConfigs})
 * @param branch branch name to checkout (it is String but cannot infer from scm object)
 */
@NonCPS
def gitCheckout(String checkoutDir, Object gitRemote, Object branch) {
    checkout poll: false, scm: [ $class                             : 'GitSCM'
                                 , branches                         : [[name: branch]]
                                 , doGenerateSubmoduleConfigurations: false
                                 , extensions                       : [ [$class: 'RelativeTargetDirectory', relativeTargetDir: checkoutDir],
                                                                        [$class: 'CloneOption', depth: 0, noTags: false, shallow: true]]
                                 , userRemoteConfigs                : gitRemote]
}