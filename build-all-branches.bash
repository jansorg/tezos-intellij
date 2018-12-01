#!/usr/bin/env bash

# Builds and tests with all major versions we support

for ij in "2018.2.3" "2018.1.3" "2017.1.3"; do
    ./gradlew -Pintellij="$ij" clean build
    [[ "$?" != "0" ]] && echo "#################  Build with IntelliJ $ij failed" && exit 1
done

./gradlew clean build
[[ "$?" != "0" ]] && echo "#################  Build with IntelliJ 2016.1 failed" && exit 1