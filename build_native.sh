#!/usr/bin/env sh
export DOCKER_HOST=unix:///run/user/${UID}/podman/podman.sock
export TESTCONTAINERS_RYUK_DISABLED=true
mvn clean install verify -P native &&
  docker build -f src/main/docker/Dockerfile.native-micro -t damdamdeo/formula-interpreter .
