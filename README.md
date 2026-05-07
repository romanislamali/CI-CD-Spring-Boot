# Spring Boot ToDo App - CI/CD Demo

This project is a functional ToDo application built with Spring Boot, JPA, and H2 database, designed to demonstrate a modern CI/CD pipeline using GitHub Actions and Docker.

## CI/CD Pipeline Flow

The CI/CD pipeline is defined in `.github/workflows/ci.yml`. It automatically triggers on every `push` or `pull_request` to the `main` branch.

### 1. Build and Test Job (`build-and-test`)
- **Environment**: Runs on `ubuntu-latest`.
- **Checkout**: Pulls the latest code from the repository.
- **Setup Java**: Configures Java 21 (Temurin) and enables Maven caching for faster builds.
- **Maven Test**: Executes all unit and integration tests (`./mvnw test`).
- **Maven Build**: Packages the application into a JAR file, skipping tests since they were already verified.

### 2. Docker Build Job (`docker-build`)
- **Dependency**: This job only starts after `build-and-test` completes successfully.
- **Setup Buildx**: Configures Docker Buildx for advanced building capabilities.
- **Build Image**: Builds a Docker image using the `Dockerfile` in the root directory.

## Dockerization

The project uses a **multi-stage Dockerfile** to ensure a small and secure production image:
- **Build Stage**: Uses a Maven image with JDK 21 to compile the code and package the JAR.
- **Run Stage**: Uses a lightweight JRE 21 image (Alpine-based) to run the application.

## Local Development

### Prerequisites
- **Java 21** must be installed.

### Running the App
Since the project is compiled for Java 21, ensure your `JAVA_HOME` is set correctly:

```bash
export JAVA_HOME=/usr/lib/jvm/java-21-openjdk-amd64
export PATH=$JAVA_HOME/bin:$PATH
./mvnw spring-boot:run
```
The application starts on port **8099** by default (configured in `application.properties`).

## API Testing

### REST Endpoints
- `GET /api/todos`: List all tasks.
- `POST /api/todos`: Create a new task.
- `GET /api/todos/{id}`: Get a specific task.
- `PUT /api/todos/{id}`: Update a task.
- `DELETE /api/todos/{id}`: Delete a task.

### Postman
A Postman collection is included in the project for easy testing: `todo-app.postman_collection.json`. Import it into Postman to start testing the CRUD operations.

## Database
- **H2 Console**: Available at `http://localhost:8099/h2-console`.
- **JDBC URL**: Check the application logs for the dynamic URL (e.g., `jdbc:h2:mem:...`).