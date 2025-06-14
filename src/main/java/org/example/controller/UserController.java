package org.example.controller;

import io.javalin.http.Context;
import org.example.model.User;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UserController {
    private static final List<User> users = new ArrayList<>();

    public static void getHello(Context ctx) {
        ctx.status(200).result("Hello, Javalin!");
    }

    public static void getStatus(Context ctx) {
        ctx.json(new StatusResponse("ok", Instant.now().toString()));
    }

    public static void postEcho(Context ctx) {
        try {
            EchoRequest request = ctx.bodyAsClass(EchoRequest.class);
            ctx.json(request);
        } catch (Exception e) {
            ctx.status(400).json(new ErrorResponse("Invalid JSON format"));
        }
    }

    // Endpoint /saudacao/{nome}
    public static void getSaudacao(Context ctx) {
        String nome = ctx.pathParam("nome");
        ctx.json(new SaudacaoResponse("Olá, " + nome + "!"));
    }

    public static void createUser(Context ctx) {
        try {
            User user = ctx.bodyAsClass(User.class);
            user.setId(UUID.randomUUID().toString());
            users.add(user);
            ctx.status(201).json(user);
        } catch (Exception e) {
            ctx.status(400).json(new ErrorResponse("Invalid user data"));
        }
    }

    public static void getAllUsers(Context ctx) {
        ctx.json(users);
    }

    public static void getUserById(Context ctx) {
        String id = ctx.pathParam("id");
        User user = users.stream()
                .filter(u -> u.getId().equals(id))
                .findFirst()
                .orElse(null);
        if (user != null) {
            ctx.json(user);
        } else {
            ctx.status(404).json(new ErrorResponse("User not found"));
        }
    }

    private static class StatusResponse {
        private String status;
        private String timestamp;

        public StatusResponse(String status, String timestamp) {
            this.status = status;
            this.timestamp = timestamp;
        }

        public String getStatus() {
            return status;
        }

        public String getTimestamp() {
            return timestamp;
        }
    }

    private static class EchoRequest {
        private String mensagem;

        public String getMensagem() {
            return mensagem;
        }

        public void setMensagem(String mensagem) {
            this.mensagem = mensagem;
        }
    }

    private static class SaudacaoResponse {
        private String mensagem;

        public SaudacaoResponse(String mensagem) {
            this.mensagem = mensagem;
        }

        public String getMensagem() {
            return mensagem;
        }
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