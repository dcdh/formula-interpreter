#!/usr/bin/env sh
docker tag damdamdeo/formula-interpreter dcdh1983/formula-interpreter
docker login -u dcdh1983 -p $1
docker push dcdh1983/formula-interpreter
docker logout