# Eclipse Xpect

Xpect&trade; is a unit- and integration-testing framework that stores test data in any kind of text files and is based on JUnit. 
The core focus of Xpect is on testing Xtext languages and supporting the process of designing Xtext languages.

## Documentation

Visit [xpect-tests.org](http://www.xpect-tests.org) for details.

#### Nightly builds

Use Jenkins https://ci.eclipse.org/xpect/job/Xpect/job/master/lastSuccessfulBuild/artifact/org.eclipse.xpect.releng/p2-repository/target/repository/


# Contributing

See [CONTRIBUTING.md](CONTRIBUTING.md) for details.

## Compile and Build Xpect by Yourself

Prerequisite: Java 11 or newer; Eclipse (2023-03 or newer recommended); Xtext 2.31.0.

 1. Clone https://github.com/eclipse/Xpect (this repository)
 2. Import all projects into your Eclipse workspace.
 3. Set target platform to /org.eclipse.xpect.releng/target-platforms/eclipse_2023_03-xtext_2_31_0/org.eclipse.xpect.target.eclipse_2023_03-xtext_2_31_0.target (Preferences -> Plug-in Development -> Target Platform)
 4. Run /org.eclipse.xpect/src/org/eclipse/xpect/GenerateXpect.mwe2, /org.xtext.example.arithmetics/src/org/eclipse/xpect/example/arithmetics/GenerateXpect.mwe2, /org.xtext.example.domainmodel/src/org/xtext/example/domainmodel/GenerateDomainmodel.mwe2. Now your projects should be without errors markers. Sometimes, even after these steps, several projects still have error markers. However, this is a refresh problem in Eclipse. Simply clean build the projects with error markers will solve the issues.
 5. Run `mvn -P '!tests'  -Dtarget-platform=eclipse_2023_03-xtext_2_31_0 --batch-mode --update-snapshots -fae -Dmaven.repo.local=xpect-local-maven-repository -DtestOnly=false clean install` to build artefacts and create a p2 repository (formerly known as update site).
