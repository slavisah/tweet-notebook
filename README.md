Simple project for education
============================

###How to start application

   - Without Maven Wrapper ```mvn spring-boot:run -Dspring.profiles.active=dev```
   
   - Using Maven Wrapper ```./mvnw spring-boot:run -Dspring.profiles.active=dev```
   
###How to stop application
```
curl -X POST localhost:8080/shutdown
```

###How to run tests

   Unit tests: ```mvn test -P dev```
   
   Integration tests: ```mvn verify -P it```

###API design
```
http://localhost:8080/swagger-ui.html
```
