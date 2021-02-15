#!/usr/bin/env bash

# Builds and tests with all major versions we support

for ij in "2020.3.2"; do
    echo "Building with $ij"
    ./gradlew -Pintellij="$ij" clean build
    [[ "$?" != "0" ]] && echo "#################  Build with IntelliJ $ij failed" && exit 1
done

./gradlew clean build
[[ "$?" != "0" ]] && echo "#################  Build with IntelliJ 2016.1 failed" && exit 1