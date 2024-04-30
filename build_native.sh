#!/usr/bin/env sh
mvn clean install verify -Dnative -Dquarkus.native.container-build=true -Dquarkus.container-image.build=true
