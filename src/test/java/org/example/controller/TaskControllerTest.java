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
        app.get("/hello", UserController::getHello);
        app.get("/status", UserController::getStatus);
        app.post("/echo", UserController::postEcho);
        app.get("/saudacao/{nome}", UserController::getSaudacao);
        app.post("/users", UserController::createUser);
        app.get("/users", UserController::getAllUsers);
        app.get("/users/{id}", UserController::getUserById);
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
    public void testCreateUser() {
        String userJson = "{\"nome\":\"Joao\",\"email\":\"joao@example.com\",\"idade\":25}";
        given()
                .contentType(ContentType.JSON)
                .body(userJson)
                .when()
                .post("/users")
                .then()
                .statusCode(201)
                .body("nome", equalTo("Joao"))
                .body("email", equalTo("joao@example.com"))
                .body("idade", equalTo(25));
    }

    @Test
    public void testGetUserById() {
        String userJson = "{\"nome\":\"Maria\",\"email\":\"maria@example.com\",\"idade\":30}";
        String id = given()
                .contentType(ContentType.JSON)
                .body(userJson)
                .when()
                .post("/users")
                .then()
                .extract()
                .path("id");

        given()
                .when()
                .get("/users/" + id)
                .then()
                .statusCode(200)
                .body("nome", equalTo("Maria"))
                .body("email", equalTo("maria@example.com"))
                .body("idade", equalTo(30));
    }

    @Test
    public void testGetAllUsers() {
        String userJson = "{\"nome\":\"Ana\",\"email\":\"ana@example.com\",\"idade\":22}";
        given()
                .contentType(ContentType.JSON)
                .body(userJson)
                .when()
                .post("/users");

        given()
                .when()
                .get("/users")
                .then()
                .statusCode(200)
                .body("size()", equalTo(1))
                .body("[0].nome", equalTo("Ana"));
    }
}