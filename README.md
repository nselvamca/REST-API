# Spring Boot- REST-API

<h2>About</h2>


This Spring Boot- REST-API will get all Products from a input JSON

<h2>Tools</h2>
- Spring Boot 2.1.2 - Web
<br>
- Java 1.8
<br>
- Maven 
<br>
- API with Embeded Tomcat Server
<br>
- Editor - STS
<br>
-Postman
<br>
<h2>Build, Deploy And Run</h2>
- Use Maven Build or Export as WAR from STS (build file is rest-api-test-0.0.1-SNAPSHOT.war)
<br>
- Run the WAR file as java -jar rest-api-test-0.0.1-SNAPSHOT.war

<h2>Unit Test</h2>
- JUNIT
<br>
- Mockito (MockMVC)
<br>
<h>API URI</h>
<br>
<b>URI for all Products : </b>
<br>
/v1/products/allproducts
<br>
<b>URI for optional Param : ( labelType=ShowWasNow / labelType=ShowWasThenNow / labelType=ShowPercDscount </b>
<br>
/v1/products/allproducts?labelType=ShowWasNow
