## About
A (Servlet 3.0) web application that provides an interface to do some basic querying of an LDAP server. The major technologies used are:

- Spring Security (version 3.2.0)
- Spring MVC (version 4.0.0)
- Spring LDAP (version  1.3.2)

Efforts have been made to use only the Java/Annotation configuration so there are no Spring XML files. There is also no web.xml file.

## Prerequisites
- Download and install [Java 7](http://www.oracle.com/technetwork/java/javaee/downloads/index.html).
- Download the latest [Maven](http://maven.apache.org/download.cgi "Maven's download page").
- Download and install the latest [ApacheDS](http://directory.apache.org/apacheds/).

## Get Started

- ```ldap-connection.properties``` should be modified to have the correct values for your LDAP server.
- From this folder type ```mvn install```. This will (also) build the war file that you can deploy to an application server.
- Alternatively, you can build and run the application with Tomcat 7 by typing ```mvn tomcat7:run``` without having to install Tomcat. (Same for Jetty by running ```mvn jetty:run```)
- Access the application on [http://localhost:8080/ldap-ui/](http://localhost:8080/ldap-ui/).
- You can log in with user/password or admin/password.

## TODO
#### Generic Functional
- [x] Log in and Logout capabilities.
- [ ] Menu bar for easy navigation to different parts of the application.
- [ ] The ability to view (or even change) LDAP connection details.

#### LDAP Functional
- [x] Check that a user can bind, given a username and password
- [ ] status of user
- [ ] verify password of user
- [ ] list roles of a user
- [ ] list attributes of a user node
- [ ] list organisations
- [ ] list sub-organisations within an organisation
- [ ] list attributes of an organisation node
- [ ] list users and their roles in an organisation
- [ ] list users in an organisation and its suborganisations
- [ ] Reset user status and password

## References/Useful links

- Novell LDAP [examples and documentation](http://www.novell.com/documentation/developer/samplecode/jldap_sample/ "Novell LDAP documentation").
- [Spring Security tutorial](http://spring.io/blog/2013/07/03/spring-security-java-config-preview-web-security/ "Spring Security tutorial") and also Designing and Implementing a Web Application with Spring [part 6](http://spring.io/guides/tutorials/web/6/ "Spring tutorial part 6").
- Documentation for [Spring LDAP](http://docs.spring.io/spring-ldap/docs/current/reference/html/introduction.html).