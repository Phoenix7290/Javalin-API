package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.Javalin;
import io.javalin.json.JavalinJackson;
import org.example.controller.TaskController;

public class Main {
    public static void main(String[] args) {
        // Configure Jackson to ensure UTF-8 encoding
	Javalin app = Javalin.create().start(7000);


        // Ensure UTF-8 in Content-Type header
        app.before(ctx -> ctx.header("Content-Type", "application/json; charset=UTF-8"));

        app.get("/hello", TaskController::getHello);
        app.get("/status", TaskController::getStatus);
        app.post("/echo", TaskController::postEcho);
        app.get("/saudacao/{nome}", TaskController::getSaudacao);
        app.post("/tarefas", TaskController::createTask);
        app.get("/tarefas", TaskController::getAllTasks);
        app.get("/tarefas/{id}", TaskController::getTaskById);

        app.exception(Exception.class, (e, ctx) -> {
            ctx.status(500).json(new ErrorResponse("Erro interno: " + e.getMessage()));
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
