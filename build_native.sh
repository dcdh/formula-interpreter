#!/usr/bin/env sh
mvn clean install verify -P native &&
  docker build -f src/main/docker/Dockerfile.native-micro -t damdamdeo/formula-interpreter