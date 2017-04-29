# Maconha

A search engine with frontend for my NAS.

* [project documentation](https://ci.weltraumschaf.de/job/maconha/site/)
* [distribution to download](https://ci.weltraumschaf.de/job/maconha/lastSuccessfulBuild/artifact/target/maconha-dsitribution-1.0.0-SNAPSHOT.tar.bz2)

## Prerequisite

You need:

* Java 8 JRE installed
* SQL database such as MySQL installed

## Installation

Unzip the downloaded distribution file and adapt the database configuration in `etc/config.properties.sample`.
Then you can start the application with the command:

    $> ./bin/maconha --spring.config.location=etc/config.properties.sample

## Links

- Spring
    - [Spring & Angular Tutorial](http://websystique.com/springmvc/spring-mvc-4-angularjs-example/)
    - [Spring & Hibernate](http://websystique.com/springmvc/spring-4-mvc-and-hibernate4-integration-example-using-annotations/)
    - [Spring Security 4: JDBC Authentication and Authorization in MySQL](https://dzone.com/articles/spring-security-4-authenticate-and-authorize-users)
    - [Spring Boot Samples](https://github.com/spring-projects/spring-boot/tree/master/spring-boot-samples)
    - [Spring Boot Digest Authentication](http://stackoverflow.com/questions/33918432/digest-auth-in-spring-security-with-rest-and-javaconfig)
    - [Batch](http://projects.spring.io/spring-batch/)
- Vaadin
    - <http://vaadin.github.io/spring-tutorial/>
    - <https://vaadin.com/docs/-/part/framework/tutorial.html>
    - [Vaadin & Spring](https://vaadin.com/spring)
- DB Testing
    - [DBUnit Rules](https://rpestano.wordpress.com/2016/06/20/ruling-database-testing-with-dbunit-rules/)