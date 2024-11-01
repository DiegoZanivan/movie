# SpringBoot application

### Tools & versions
* Java 17 (used OpenJDK to run)
* Embedded H2 database
* Gradle 8.3

### Running the application
When in the root folder, you can run the following commands:
- gradlew clean build
- gradle test
- gradle bootRun

To respectively cleaning and building, running tests, and running the application

### Endpoints
You can hit any of these endpoints and check if is everything working properly:
* http://localhost:8080/v1/movies - get all
* http://localhost:8080/v1/movies/winners - get all winners
* http://localhost:8080/v1/movies/award-interval - get max and min interval between awards