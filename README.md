Simple project for education
============================

- Start application

   Without Maven Wrapper ```mvn spring-boot:run -Dspring.profiles.active=dev```
   
   Using Maven Wrapper ```./mvnw spring-boot:run -Dspring.profiles.active=dev```
   
- Stop application
```
curl -X POST localhost:8080/shutdown
```

- API design
```
http://localhost:8080/swagger-ui.html
```

- Run tests

   Unit tests: ```mvn test -P dev```
   
   Integration tests: ```mvn verify -P it```

