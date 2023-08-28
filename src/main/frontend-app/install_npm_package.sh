#!/bin/bash
PATH="$(pwd)/node:$PATH"
npm install "$1" --save --force
