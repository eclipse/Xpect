# Releng

### Buildserver

https://ci.eclipse.org/xpect/job/Xpect/

## Publishing

### P2
p2 repository should be published under https://download.eclipse.org/xpect/updates/?d
nightly/releases

### Maven
Maven SNAPSHOT artifacts are automatically deployed during a regular build

Maven release artifacts are automatically deployed during a release build

## Releasing

- create branch named 'release_x.y.z'
- update maven versions
- push the branch
- Jenkins will trigger a build and publish to maven staging
- review and publish staging repo from sonatype web UI

### Update maven version

To select an other target platform use e.g. `-Dtarget-platform=eclipse_2023_03-xtext_2_31_0`

```mvn org.eclipse.tycho:tycho-versions-plugin:set-version -Dtarget-platform=eclipse_2023_03-xtext_2_31_0 -P'!xtext-examples' -DnewVersion="0.2.0.v20190619" -Dartifacts="org.eclipse.xpect.parent,org.eclipse.xpect.test.parent"```

Manually change the property value of `target-platform-version` to  0.2.0.v20190619 in:
```
org.eclipse.xpect.releng/maven-plugin-parent/pom.xml
org.eclipse.xtext.example.arithmetics.xpect.tests/pom.xml
org.eclipse.xtext.example.domainmodel.xpect.tests/pom.xml
```

### Sonatype Web UI

https://oss.sonatype.org/index.html
