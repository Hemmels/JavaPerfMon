# JavaPerfMon - How to build
Simple Java Performancy Monuitor
To run the application, you require a local JDK 11.

    .\gradle.bat wrapper
    .\gradlew.bat bootRun -D org.gradle.java.home=D:\Java\jdk-11.0.1

I've been using "test" and "prod" profiles, these can be on the command above set with 

     --args='--spring.profiles.active=prod'

## What is it?
Currently serves as the backend for a "Service poller" which can be seen running [on Github Pages](https://hemmels.github.io/reactapp)
The "test" profile will setup an embedded h2 database with test data. This will actually run against live sites since all we are doing is pinging them.
