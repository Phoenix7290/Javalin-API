# Javalin-API

## Overview
This project is an academic assessment developed as part of a course to explore RESTful API development using lightweight Java technologies. The goal was to build a simple, yet functional, To-Do API using **Javalin**, a minimalistic web framework, to understand core web development concepts without relying on heavier frameworks like Spring Boot. The API manages tasks with basic CRUD operations, uses an **H2 in-memory database** for persistence, and is containerized with **Docker** for easy deployment.

The application is deployed and accessible at [http://todo-api.marcosryan.online](http://todo-api.marcosryan.online).

## Features
- **RESTful Endpoints**: Create, read, and retrieve tasks via intuitive HTTP endpoints.
- **H2 Database**: Lightweight in-memory database for task persistence.
- **Javalin Framework**: A simple, dependency-light framework for handling HTTP requests.
- **Dockerized Deployment**: Containerized setup for consistent development and production environments.
- **Unit Testing**: Comprehensive tests using JUnit and REST Assured to ensure reliability.
- **UTF-8 Support**: Proper handling of internationalization with consistent encoding.

## Prerequisites
- **Java 21** or higher
- **Gradle 8.5**
- **Docker** (for containerized deployment)
- **Git** (to clone the repository)

## Project Structure
```
├── .gitignore
├── build.gradle.kts
├── compose.yml
├── Dockerfile
├── Dockerfile.h2
├── gradle/
│   └── wrapper/
│       └── gradle-wrapper.properties
├── gradlew
├── gradlew.bat
├── README.md
├── settings.gradle.kts
├── src/
│   ├── main/
│   │   ├── java/org/example/
│   │   │   ├── client/ApiClient.java
│   │   │   ├── controller/TaskController.java
│   │   │   ├── Main.java
│   │   │   ├── model/Task.java
│   │   ├── resources/META-INF/
│   │       ├── persistence.xml
│   │       ├── persistence_local.xml
│   ├── test/
│   │   ├── java/org/example/
│   │   │   ├── controller/TaskControllerTest.java
│   │   │   ├── MainTest.java
│   │   ├── resources/META-INF/
│   │       ├── persistence.xml
```

## Endpoints
The API provides the following endpoints, accessible at `http://todo-api.marcosryan.online:7000`:

| Method | Endpoint            | Description                              |
|--------|---------------------|------------------------------------------|
| GET    | `/hello`            | Returns a simple "Hello, Javalin!" message. |
| GET    | `/status`           | Returns the API status and timestamp.    |
| POST   | `/echo`             | Echoes back the provided JSON payload.   |
| GET    | `/saudacao/{nome}`  | Returns a personalized greeting.         |
| POST   | `/tarefas`          | Creates a new task.                      |
| GET    | `/tarefas`          | Retrieves all tasks.                     |
| GET    | `/tarefas/{id}`     | Retrieves a task by ID.                  |

**Example Request (Create Task)**:
```bash
curl -X POST http://todo-api.marcosryan.online:7000/tarefas \
-H "Content-Type: application/json" \
-d '{"titulo":"Nova Tarefa","descricao":"Descrição da tarefa"}'
```

**Example Response**:
```json
{
  "id": "uuid-generated",
  "titulo": "Nova Tarefa",
  "descricao": "Descrição da tarefa",
  "concluida": false,
  "dataCriacao": "2025-06-16T21:06:00Z"
}
```

## Setup and Running Locally

1. **Clone the Repository**:
   ```bash
   git clone <repository-url>
   cd Javalin-API
   ```

2. **Build the Project**:
   ```bash
   ./gradlew build
   ```

3. **Run with Docker**:
   - Ensure Docker is running.
   - Start the application and H2 database:
     ```bash
     docker-compose -f compose.yml up --build
     ```
   - The API will be available at `http://localhost:7000`.
   - The H2 database will be accessible at `tcp://localhost:9092`.

4. **Run Without Docker**:
   - Start the H2 database server manually (optional, for local persistence):
     ```bash
     java -cp h2-2.3.230.jar org.h2.tools.Server -tcp -tcpAllowOthers -ifNotExists
     ```
   - Run the application:
     ```bash
     ./gradlew run
     ```

5. **Run Tests**:
   ```bash
   ./gradlew test
   ```

## Deployment
The application is deployed using Docker and Docker Compose, with two services:
- **app**: Runs the Javalin API, built from the `Dockerfile`.
- **db**: Runs the H2 database server, built from `Dockerfile.h2`.

The deployment is live at [http://todo-api.marcosryan.online](http://todo-api.marcosryan.online).

## Technologies Used
- **Java 21**: Core programming language.
- **Javalin 6.5.0**: Lightweight web framework.
- **H2 Database 2.3.230**: In-memory database for persistence.
- **Hibernate 6.6.0**: ORM for database interactions.
- **Gradle 8.5**: Build automation tool.
- **JUnit 5.10.0 & REST Assured 5.5.0**: Testing frameworks.
- **Docker**: Containerization for deployment.
- **Jackson 2.17.2**: JSON processing.

## Learning Objectives
This project was designed to:
- Understand RESTful API development without heavy frameworks.
- Practice manual dependency management and configuration.
- Learn database integration with Hibernate and H2.
- Explore containerization and deployment with Docker.
- Implement unit and integration testing for APIs.

## Notes
- The H2 database is in-memory by default for local testing (`persistence_local.xml`) but uses a TCP connection for Dockerized deployment (`persistence.xml`).
- Ensure UTF-8 encoding is used throughout to handle internationalization correctly.
- The `.gitignore` excludes `repomix-output.xml` to prevent committing generated files.

## Contributing
This is an academic project, but feedback is welcome! Feel free to open issues or suggest improvements via the repository.

## Acknowledgments
- Thanks to my professor @bprates29 for guidance and feedback.
- Inspired by the need to learn lightweight Java web development.
- Built with open-source tools and libraries.