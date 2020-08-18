# JavaPerfMon - How to build
Simple Java Performancy Monuitor
To run the application, you require a local JDK 11.

    .\gradle.bat wrapper
    .\gradlew.bat bootRun -D org.gradle.java.home=D:\Java\jdk-11.0.1

I've been using "test" and "prod" profiles, these can be on the command above set with 

     --args='--spring.profiles.active=prod'

## What is it?
Currently serves as the backend for a "Service poller" which can be seen running [on Github Pages](https://hemmels.github.io/reactapp) N.B. Doesn't show anything until I implement the database reads in Javascript.
The "test" profile will setup an embedded h2 database with test data. This will actually run against live sites since all we are doing is pinging them.
More information on the whole project can be found on the (admittedly poorly named) [react frontend project](https://github.com/Hemmels/reactapp)

## Features
* Showcases a gradle build and folder structure of 2 projects, 1 for the application logic, and one for Jooq generated classes.
* SpringBoot application and Junit 5 tests.
* Embedded H2 database for testing
* test and prod Spring prodiles for H2/Mysql reads on application run

## How to run?
Simply navigate to the JavaPerfMonApp subproject dir and execute something like:

    ..\gradlew.bat bootRun -D org.gradle.java.home=D:\Java\jdk-11.0.1 --args='--spring.profiles.active=test'
    
This will create a running back end to which you can run the [React front end](https://github.com/Hemmels/reactapp) against.
