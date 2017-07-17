
<!--
Copyright (c) 2013-2017 TypeFox GmbH and itemis AG.
All rights reserved. This program and the accompanying materials
are made available under the terms of the Eclipse Public License v1.0
which accompanies this distribution, and is available at
http://www.eclipse.org/legal/epl-v10.html

Contributors:
  Moritz Eysholdt - Initial contribution and API
-->

# Xpect

A unit- and integration-testing framework that stores test data in any kind of text files and is based on JUnit. 
The core focus of Xpect is on testing Xtext languages and supporting the process of designing Xtext languages.


## Installation further Information

Go to http://www.xpect-tests.org (and look for an Eclipse update-site).


## Compile and Build Xpect by Yourself

**The Fast Way:** Download [OOMPH](https://wiki.eclipse.org/Eclipse_Oomph_Installer) and point it to [Xpect.setup](https://github.com/meysholdt/Xpect/raw/master/org.xpect.releng/Xpect.setup).

**The Manual Way:** Prerequisite: Java 1.6 or newer; Eclipse 3.8 or 4.2 or newer; Xtext 2.9.2.

 1. Clone https://github.com/meysholdt/Xpect (this repository)
 2. Import all projects into your Eclipse workspace.
 3. Set target platform to /org.xpect.releng/target-platforms/eclipse_4_6_3-xtext_2_9_2/org.xpect.target.eclipse_4_6_3-xtext_2_9_2.target (Preferences -> Plug-in Development -> Target Platform)
 4. Run /org.xpect/src/org/xpect/GenerateXpect.mwe2, /org.xtext.example.arithmetics/src/org/xpect/example/arithmetics/GenerateXpect.mwe2, /org.xtext.example.domainmodel/src/org/xtext/example/domainmodel/GenerateDomainmodel.mwe2. Now your projects should be without errors markers. Sometimes, even after these steps, several projects still have error markers. However, this is a refresh problem in Eclipse. Simply clean build the projects with error markers will solve the issues.
 5. Run "mvn clean install" to build artefacts and create a p2 repository (formerly known as update site).


 
