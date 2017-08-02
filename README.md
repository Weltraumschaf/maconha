# Maconha

A search engine with frontend for my NAS based on [Spring Boot](https://projects.spring.io/spring-boot/)
and [Vaddin](https://vaadin.com/home).

* [project documentation](https://ci.weltraumschaf.de/job/maconha/site/)
* [distribution to download](https://ci.weltraumschaf.de/job/maconha/lastSuccessfulBuild/artifact/target/maconha-dsitribution-1.0.0-SNAPSHOT.tar)

## Prerequisite

You need:

* Java 8 JRE installed
* SQL database (e.g MySQL) installed
* either the command line tool `sha256` or `shasum`

## Installation

Run the command:

```
$> curl -fsSL https://raw.githubusercontent.com/Weltraumschaf/maconha/master/tools/install.sh | /bin/sh
```

By default this script install the files into the path prefix `/usr/local`. If you want another location export
the variable `MACONHA_PREFIX`.

### Initall Database Setup

```
CREATE DATABASE maconha;
CREATE USER 'maconha'@'%' IDENTIFIED BY 'new_password';
GRANT ALL ON maconha.* TO 'maconha'@'%';
FLUSH PRIVILEGES;
```

Then adapt the database configuration in `PREFIX/etc/maconha.properties.sample` with the MySQL password.
Then you can start the application with the command:

```
$> PREFIX/bin/maconha --spring.config.location=PREFIX/etc/maconha.properties --spring.profiles.active=prod
```

Maybe you need to set JAVA_HOME with the right path (below are FreeBSD paths):

- csh: `setenv JAVA_HOME /usr/local/openjdk8-jre`
- bash: `export JAVA_HOME=/usr/local/openjdk8-jre`

## Links

- Spring
    - [Spring Boot Reference Guide](http://docs.spring.io/spring-boot/docs/1.5.3.RELEASE/reference/htmlsingle/)
    - [Spring & Hibernate](http://websystique.com/springmvc/spring-4-mvc-and-hibernate4-integration-example-using-annotations/)
    - [Spring Security 4: JDBC Authentication and Authorization in MySQL](https://dzone.com/articles/spring-security-4-authenticate-and-authorize-users)
    - [Spring Boot Samples](https://github.com/spring-projects/spring-boot/tree/master/spring-boot-samples)
    - [Spring Boot Digest Authentication](http://stackoverflow.com/questions/33918432/digest-auth-in-spring-security-with-rest-and-javaconfig)
    - [Better application events in Spring Framework 4.2](https://spring.io/blog/2015/02/11/better-application-events-in-spring-framework-4-2)
    - [Task Execution and Scheduling](http://docs.spring.io/spring/docs/current/spring-framework-reference/html/scheduling.html#scheduling-annotation-support-scheduled)
    - [Dynamically add jars to SpringBoot at runtime](https://stackoverflow.com/questions/41148353/dynamically-add-jars-to-springboot-at-runtime)
- Vaadin
    - <http://vaadin.github.io/spring-tutorial/>
    - <https://vaadin.com/docs/-/part/framework/tutorial.html>
    - [Vaadin & Spring](https://vaadin.com/spring)
- DB Testing
    - [DBUnit Rules](https://rpestano.wordpress.com/2016/06/20/ruling-database-testing-with-dbunit-rules/)
