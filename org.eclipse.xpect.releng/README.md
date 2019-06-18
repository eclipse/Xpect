# Releng

### Buildserver

https://ci.eclipse.org/xpect/job/Xpect/



## Releasing

- create branch named 'release_x.y.z'
- update maven versions
- push the branch
- Jenkins will trigger a build and publish to maven staging
- review and publish staging repo from sonatype web UI

### Update maven version

To select an other target platform use e.g. `-Dtarget-platform=eclipse_4_4_2-xtext_2_9_2`

```mvn org.eclipse.tycho:tycho-versions-plugin:set-version -P'!tests' -P'!xtext-examples' -DnewVersion="0.2.0.v20190618" -Dartifacts="org.eclipse.xpect.parent"```

### Sonatype Web UI

https://oss.sonatype.org/index.html
