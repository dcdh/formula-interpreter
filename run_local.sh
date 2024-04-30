#!/usr/bin/env sh
docker-compose -f docker-compose-local-run.yaml up && docker-compose -f docker-compose-local-run.yaml rm --force
