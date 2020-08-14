# JavaPerfMon
Simple Java Performancy Monuitor
To run the application, you require a local JDK 11.

    .\gradle.bat wrapper
    .\gradlew.bat -D org.gradle.java.home=D:\Java\jdk-11.0.1 bootRun

I've been using "test" and "prod" profiles, these can be on the command above set with 

     --args='--spring.profiles.active=prod'
     
Currently serves as the backend for a "Service poller" which can be seen running [on Github Pages](https://hemmels.github.io/reactapp)
