## About

## Prerequisites
- Download the latest [Maven](http://maven.apache.org/download.cgi "Maven's download page").

## Get Started

- From this folder type ```mvn install```. This will (also) build the war file that you can deploy to an application server.
- To build and run the application with Tomcat 7, type ```mvn tomcat7:run```. If you prefer, you can use Jetty instead of Tomcat by running ```mvn jetty:run```. Access the application on [http://localhost:8080/](http://localhost:8080/).
- You can log in with user/password or admin/password.

## TODO
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
- [Spring Security tutorial](http://spring.io/blog/2013/07/03/spring-security-java-config-preview-web-security/ "Spring Security tutorial") and also Designing and Implementing a Web Application with Spring [part 6](http://spring.io/guides/tutorials/web/6/ "Spring tutorial part 6")
