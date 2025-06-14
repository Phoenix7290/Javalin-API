package org.example;

import io.javalin.Javalin;
import org.example.controller.UserController;

public class Main {
    public static void main(String[] args) {
        Javalin app = Javalin.create().start(7000);

        app.get("/hello", UserController::getHello);
        app.get("/status", UserController::getStatus);
        app.post("/echo", UserController::postEcho);
        app.get("/saudacao/{nome}", UserController::getSaudacao);
        app.post("/users", UserController::createUser);
        app.get("/users", UserController::getAllUsers);
        app.get("/users/{id}", UserController::getUserById);

        app.exception(Exception.class, (e, ctx) -> {
            ctx.status(500).json(new ErrorResponse("Internal server error: " + e.getMessage()));
        });
    }

    private static class ErrorResponse {
        private String error;

        public ErrorResponse(String error) {
            this.error = error;
        }

        public String getError() {
            return error;
        }
    }
}