# Xpect

A unit- and integration-testing framework that stores test data in any kind of text files and is based on JUnit. 
The core focus of Xpect is on testing Xtext languages and supporting the process of designing Xtext languages.


## Installation further Information

Go to http://www.xpect-tests.org (and look for an Eclipse update-site).


## Compile and Build Xpect by Yourself

**The Fast Way:** Download [OOMPH](https://wiki.eclipse.org/Eclipse_Oomph_Installer) and point it to [Xpect.setup](https://github.com/meysholdt/Xpect/raw/master/org.xpect.releng/Xpect.setup).

**The Manual Way:** Prerequisite: Java 1.6 or newer; Eclipse 3.8 or 4.2 or newer; Xtext 2.4. 

 1. Clone https://github.com/meysholdt/Xpect (this repository)
 2. Import all projects into your Eclipse workspace.
 3. Set target platform /org.xpect.releng/target-platforms/org.xpect.target.target
 4. Run /org.xpect/src/org/xpect/GenerateXpect.mwe2. Now your projects should be without errors markers. 
 5. Run "mvn clean install" to build artefacts and create a p2 repository (formerly known as update site).


 
