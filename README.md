# A fresh IntelliJ plugin

[![Build Status](https://travis-ci.org/jansorg/intellij-plugin-base.svg?branch=master)](https://travis-ci.org/jansorg/intellij-plugin-base)

This is a template for a new IntelliJ plugin build by Gradle.
The file `intellij-plugin-base.iml` is the module file you should import in IntelliJ.

The build configuration uses Gradle with enabled Jacoco test coverage reports.

# Directory structure

- `src/main/java`: Java sources
- `src/main/resources`: Java resource files
- `src/test/java`: Java test case sources
- `src/test/resources`: Java test case resource files

# Continous integration builds

`.travis.yml` configures a [Travic-ci.org](https://travis-ci.org/) build.


# Documentation
- Introduction to plugin development: https://www.plugin-dev.com/intellij/introduction/
- IntelliJ DevGuide: http://www.jetbrains.org/intellij/sdk/docs/welcome.html
- Gradle build plugin: http://www.jetbrains.org/intellij/sdk/docs/tutorials/build_system.html
