#!/bin/bash
if [ ! -d "$(pwd)/node" ]
then
  mvn frontend:install-node-and-npm -f ../../../pom.xml
fi