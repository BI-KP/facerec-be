# FaceRec Backend

## Specifications
- **Language**: Java
- **JDK**: Oracle GraalVM JDK 21
- **Framework**: Spring Boot 3.4.2
- **Database**: MySQL 8.0.40
- **Build Tool**: Maven 3.9.9

## Description
This is a Spring Boot project built with Maven for the authentication of Face Recognition service. It provides functionality for JWT authentication and connects to a MySQL database.

## Features
- JWT token generation using the `JWTService` class
- Secure authentication based on a secret key stored in an environment variable
- Database connectivity using MySQL with HikariCP connection pooling

## Configuration
- **.env**  
  Contains a base64-encoded secret key used for signing JWT tokens.


- **src/main/resources/application.properties**  
  Contains database configuration and other application settings.  
  Make sure to adjust the URL, username, and password according to your environment.

## Build and Run
1. Clone the repository.
2. Navigate to the project directory.
3. Build the project using Maven:
   ```bash
   mvn clean package
   ```
4. Run the application:
    ```bash
    java -jar target/facerec-backend-0.0.1-SNAPSHOT.jar
    ```
   or
    ```bash
    mvn spring-boot:run
    ```
## OpenAPI
https://app.swaggerhub.com/apis/kpbi/face-recognition_api/1.0.0
