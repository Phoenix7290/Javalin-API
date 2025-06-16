package org.example.controller;

import io.javalin.Javalin;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class TaskControllerTest {
    private static Javalin app;

    @BeforeAll
    public static void setup() {
        app = Javalin.create().start(7000);
        app.get("/hello", TaskController::getHello);
        app.get("/status", TaskController::getStatus);
        app.post("/echo", TaskController::postEcho);
        app.get("/saudacao/{nome}", TaskController::getSaudacao);
        app.post("/tarefas", TaskController::createTask);
        app.get("/tarefas", TaskController::getAllTasks);
        app.get("/tarefas/{id}", TaskController::getTaskById);
        RestAssured.baseURI = "http://localhost:7000";
    }

    @AfterAll
    public static void tearDown() {
        app.stop();
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
        String echoJson = "{\"mensagem\":\"Teste de eco\"}";
        given()
                .contentType(ContentType.JSON)
                .body(echoJson)
                .when()
                .post("/echo")
                .then()
                .statusCode(200)
                .body("mensagem", equalTo("Teste de eco"));
    }

    @Test
    public void testSaudacaoEndpoint() {
        given()
                .when()
                .get("/saudacao/Marcos")
                .then()
                .statusCode(200)
                .body("mensagem", equalTo("Olá, Marcos!"));
    }

    @Test
    public void testCreateTask() {
        String taskJson = "{\"titulo\":\"Estudar Java\",\"descricao\":\"Revisar Javalin\"}";
        given()
                .contentType(ContentType.JSON)
                .body(taskJson)
                .when()
                .post("/tarefas")
                .then()
                .statusCode(201)
                .body("titulo", equalTo("Estudar Java"))
                .body("descricao", equalTo("Revisar Javalin"))
                .body("concluida", equalTo(false));
    }

    @Test
    public void testGetTaskById() {
        String taskJson = "{\"titulo\":\"Tarefa Teste\",\"descricao\":\"Testar endpoint\"}";
        String id = given()
                .contentType(ContentType.JSON)
                .body(taskJson)
                .when()
                .post("/tarefas")
                .then()
                .extract()
                .path("id");

        given()
                .when()
                .get("/tarefas/" + id)
                .then()
                .statusCode(200)
                .body("titulo", equalTo("Tarefa Teste"))
                .body("descricao", equalTo("Testar endpoint"))
                .body("concluida", equalTo(false));
    }

    @Test
    public void testGetAllTasks() {
        String taskJson = "{\"titulo\":\"Tarefa Lista\",\"descricao\":\"Verificar listagem\"}";
        given()
                .contentType(ContentType.JSON)
                .body(taskJson)
                .when()
                .post("/tarefas");

        given()
                .when()
                .get("/tarefas")
                .then()
                .statusCode(200)
                .body("size()", equalTo(1))
                .body("[0].titulo", equalTo("Tarefa Lista"));
    }

    @Test
    public void testCreateTaskWithMissingTitle() {
        String taskJson = "{\"descricao\":\"Tarefa sem título\"}";
        given()
                .contentType(ContentType.JSON)
                .body(taskJson)
                .when()
                .post("/tarefas")
                .then()
                .statusCode(400)
                .body("error", equalTo("Título é obrigatório"));
    }
}