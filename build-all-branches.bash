#!/usr/bin/env bash

# Builds and tests with all major versions we support

./gradlew -Pintellij=2018.2.3 clean build
./gradlew -Pintellij=2018.1.3 clean build
./gradlew -Pintellij=2017.1.3 clean build
./gradlew clean build