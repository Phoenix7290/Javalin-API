package org.example.controller;

import io.javalin.Javalin;
import io.restassured.RestAssured;
import io.restassured.config.EncoderConfig;
import io.restassured.config.RestAssuredConfig;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class TaskControllerTest {

    private static Javalin app;

    @BeforeAll
    public static void setup() {
        // Configure RestAssured to use UTF-8
        RestAssured.config = RestAssuredConfig.config()
                .encoderConfig(EncoderConfig.encoderConfig()
                        .defaultContentCharset("UTF-8")
                        .defaultQueryParameterCharset("UTF-8"));

        app = Javalin.create().start(7001);
        app.get("/hello", TaskController::getHello);
        app.get("/status", TaskController::getStatus);
        app.post("/echo", TaskController::postEcho);
        app.get("/saudacao/{nome}", TaskController::getSaudacao);
        app.post("/tarefas", TaskController::createTask);
        app.get("/tarefas", TaskController::getAllTasks);
        app.get("/tarefas/{id}", TaskController::getTaskById);
        RestAssured.baseURI = "http://localhost:7001";
    }

    @BeforeEach
    public void clearDatabase() {
        EntityManager em = TaskController.getEntityManagerFactory().createEntityManager();
        try {
            em.getTransaction().begin();
            em.createQuery("DELETE FROM Task").executeUpdate();
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    @Test
    public void testHelloEndpoint() {
        given()
                .when()
                .get("/hello")
                .then()
                .statusCode(200)
                .body(equalTo("Hello, Javalin!"));
    }

    @Test
    public void testStatusEndpoint() {
        given()
                .when()
                .get("/status")
                .then()
                .statusCode(200)
                .body("status", equalTo("ok"));
    }

    @Test
    public void testEchoEndpoint() {
        given()
                .contentType("application/json; charset=UTF-8")
                .body("{\"mensagem\": \"teste\"}")
                .when()
                .post("/echo")
                .then()
                .statusCode(200)
                .body("mensagem", equalTo("teste"));
    }

    @Test
    public void testSaudacaoEndpoint() {
        given()
                .when()
                .get("/saudacao/Joao")
                .then()
                .statusCode(200)
                .body("mensagem", equalTo("Olá, Joao!"));
    }

    @Test
    public void testCreateTask() {
        given()
                .contentType("application/json; charset=UTF-8")
                .body("{\"titulo\": \"Nova Tarefa\", \"descricao\": \"Descrição da tarefa\"}")
                .when()
                .post("/tarefas")
                .then()
                .statusCode(201)
                .body("titulo", equalTo("Nova Tarefa"))
                .body("descricao", equalTo("Descrição da tarefa"));
    }

    @Test
    public void testGetTaskById() {
        String id = given()
                .contentType("application/json; charset=UTF-8")
                .body("{\"titulo\": \"Tarefa Teste\", \"descricao\": \"Teste\"}")
                .when()
                .post("/tarefas")
                .then()
                .statusCode(201)
                .extract().path("id");

        given()
                .when()
                .get("/tarefas/" + id)
                .then()
                .statusCode(200)
                .body("titulo", equalTo("Tarefa Teste"));
    }

    @Test
    public void testGetAllTasks() {
        given()
                .contentType("application/json; charset=UTF-8")
                .body("{\"titulo\": \"Tarefa 1\", \"descricao\": \"Descrição 1\"}")
                .when()
                .post("/tarefas")
                .then()
                .statusCode(201);

        given()
                .when()
                .get("/tarefas")
                .then()
                .statusCode(200)
                .body("[0].titulo", equalTo("Tarefa 1"));
    }

    @Test
    public void testCreateTaskWithMissingTitle() {
        given()
                .contentType("application/json; charset=UTF-8")
                .body("{\"descricao\": \"Tarefa sem título\"}")
                .when()
                .post("/tarefas")
                .then()
                .statusCode(400)
                .body("error", equalTo("O título é obrigatório"));
    }
}