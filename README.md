# Maconha

A search engine with frontend for my NAS.

* [project documentation](https://ci.weltraumschaf.de/job/maconha/site/)
* [distribution to download](https://ci.weltraumschaf.de/job/maconha/lastSuccessfulBuild/artifact/target/maconha-dsitribution-1.0.0-SNAPSHOT.tar)

## Prerequisite

You need:

* Java 8 JRE installed
* SQL database such as MySQL installed
* either the command line tool `sha256` or `shasum

## Installation

Run the command:

```
$> curl -fsSL https://raw.githubusercontent.com/Weltraumschaf/maconha/master/tools/install.sh | /bin/sh
```

By default this script install the files into the path prefix `/usr/local`. If you want another location export
the variable `MACONHA_PREFIX`.

### Inital Database Setup

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

- csh: setenv JAVA_HOME /usr/local/openjdk8-jre
- bash: export JAVA_HOME=/usr/local/openjdk8-jre
    
### Modify Serialized Context Size
 
After the first start of the application and before the first scan job is started, it is necessary to increase the size
for serialized context data of the batch jobs (at the moment this does not happen automatically):

```
ALTER TABLE BATCH_JOB_EXECUTION_CONTEXT MODIFY SERIALIZED_CONTEXT MEDIUMTEXT;
ALTER TABLE BATCH_STEP_EXECUTION_CONTEXT MODIFY SERIALIZED_CONTEXT MEDIUMTEXT;
```

## Links

- Spring
    - [Spring Boot Reference Guide](http://docs.spring.io/spring-boot/docs/1.5.3.RELEASE/reference/htmlsingle/)
    - [Spring & Hibernate](http://websystique.com/springmvc/spring-4-mvc-and-hibernate4-integration-example-using-annotations/)
    - [Spring Security 4: JDBC Authentication and Authorization in MySQL](https://dzone.com/articles/spring-security-4-authenticate-and-authorize-users)
    - [Spring Boot Samples](https://github.com/spring-projects/spring-boot/tree/master/spring-boot-samples)
    - [Spring Boot Digest Authentication](http://stackoverflow.com/questions/33918432/digest-auth-in-spring-security-with-rest-and-javaconfig)
    - [Spring Batch](http://projects.spring.io/spring-batch/)
    - [Better application events in Spring Framework 4.2](https://spring.io/blog/2015/02/11/better-application-events-in-spring-framework-4-2)
    - [Task Execution and Scheduling](http://docs.spring.io/spring/docs/current/spring-framework-reference/html/scheduling.html#scheduling-annotation-support-scheduled)
- Vaadin
    - <http://vaadin.github.io/spring-tutorial/>
    - <https://vaadin.com/docs/-/part/framework/tutorial.html>
    - [Vaadin & Spring](https://vaadin.com/spring)
- DB Testing
    - [DBUnit Rules](https://rpestano.wordpress.com/2016/06/20/ruling-database-testing-with-dbunit-rules/)
- CLI Tools
    [fdupes](https://github.com/adrianlopezroche/fdupes) to find duplicates
    
## Dirhashing

- /blackhole/Filme
    - P4
        - sha1:     3254.415u 408.382s 1:57:41.61 51.8%     25+167k 7921287+2510io 37pf+1w
        - sha256:   9891.475u 523.210s 59:40.43 290.8%      25+167k 7945364+2510io 57pf+3w
        - md5:      2697.052u 422.663s 54:07.91 96.0%       25+167k 7935452+2510io 4pf+0w
        - rmd160:   5763.476u 475.094s 55:36.99 186.9%      25+167k 7936315+2510io 15pf+1w
    - P8
        - sha1:     3401.796u 413.182s 1:31:19.30 69.6%     25+167k 7923041+2510io 66pf+4w
        - sha256:   7408.905u 440.381s 1:50:22.36 118.5%    25+167k 7935156+2510io 44pf+1w
        - md5:      2699.126u 421.089s 53:15.74 97.6%       25+167k 7903950+2510io 0pf+0w
        - rmd160:   5669.320u 460.686s 56:46.12 179.9%      25+167k 6987817+2510io 2pf+0w
    - P16
        - sha1:     3402.671u 409.748s 1:41:01.88 62.8%     25+167k 7920329+2510io 27pf+1w
        - sha256:   8066.541u 459.003s 1:30:25.13 157.1%    25+167k 7930652+2510io 42pf+1w
        - md5:      2699.401u 417.286s 54:41.24 94.9%       25+167k 7354174+2510io 0pf+0w
        - rmd160:   5668.755u 456.420s 58:10.65 175.4%      25+167k 6337065+2510io 0pf+0w