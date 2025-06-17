package org.example.controller;

import io.javalin.http.Context;
import org.example.model.Task;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.Arrays;

public class TaskController {
    static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("h2-pu");

    public static EntityManagerFactory getEntityManagerFactory() {
        return emf;
    }

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

    public static void getSaudacao(Context ctx) {
        String nome = ctx.pathParam("nome");
        ctx.json(new SaudacaoResponse("Olá, " + nome + "!"));
    }

    public static void createTask(Context ctx) {
        EntityManager em = emf.createEntityManager();
        try {
            Task task = ctx.bodyAsClass(Task.class);
            if (task.getTitulo() == null || task.getTitulo().isEmpty()) {
                ctx.status(400).json(new ErrorResponse("O título é obrigatório"));
                return;
            }
            task.setId(UUID.randomUUID().toString());
            task.setDataCriacao(Instant.now().toString());
            em.getTransaction().begin();
            em.persist(task);
            em.getTransaction().commit();
            ctx.status(201).json(task);
        } catch (Exception e) {
            ctx.status(400).json(new ErrorResponse("Dados inválidos"));
        } finally {
            em.close();
        }
    }

    public static void getAllTasks(Context ctx) {
        EntityManager em = emf.createEntityManager();
        try {
            List<Task> tasks = em.createQuery("SELECT t FROM Task t", Task.class).getResultList();
            ctx.json(tasks);
        } finally {
            em.close();
        }
    }

    public static void getTaskById(Context ctx) {
        EntityManager em = emf.createEntityManager();
        try {
            String id = ctx.pathParam("id");
            Task task = em.find(Task.class, id);
            if (task != null) {
                ctx.json(task);
            } else {
                ctx.status(404).json(new ErrorResponse("Tarefa não encontrada"));
            }
        } finally {
            em.close();
        }
    }

    public static void getApiHelp(Context ctx) {
        List<EndpointInfo> endpoints = Arrays.asList(
                new EndpointInfo("GET", "/", "Retorna uma lista de todos os endpoints disponíveis na API.", null),
                new EndpointInfo("GET", "/hello", "Retorna uma mensagem simples: 'Hello, Javalin'.", null),
                new EndpointInfo("GET", "/status", "Retorna um status da API e data/hora.", null),
                new EndpointInfo("POST", "/echo", "Retorna de volta o JSON fornecido.", "{\"mensagem\": \"teste\"}"),
                new EndpointInfo("GET", "/saudacao/{nome}", "Retorna uma saudação personalizada para o nome fornecido.", null),
                new EndpointInfo("POST", "/tarefas", "Cria uma nova tarefa.", "{\"titulo\": \"Nova Tarefa\", \"descricao\": \"Descrição\"}"),
                new EndpointInfo("GET", "/tarefas", "Retorna todas as tarefas.", null),
                new EndpointInfo("GET", "/tarefas/{id}", "Retorna uma tarefa usando o ID correspondente.", null)
        );
        ctx.json(new ApiHelpResponse("Todos os endpoints disponíveis.", endpoints));
    }

    // Classes auxiliares
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

    private static class EndpointInfo {
        private String method;
        private String path;
        private String description;
        private String exampleRequest;

        public EndpointInfo(String method, String path, String description, String exampleRequest) {
            this.method = method;
            this.path = path;
            this.description = description;
            this.exampleRequest = exampleRequest;
        }

        public String getMethod() {
            return method;
        }

        public String getPath() {
            return path;
        }

        public String getDescription() {
            return description;
        }

        public String getExampleRequest() {
            return exampleRequest;
        }
    }

    private static class ApiHelpResponse {
        private String message;
        private List<EndpointInfo> endpoints;

        public ApiHelpResponse(String message, List<EndpointInfo> endpoints) {
            this.message = message;
            this.endpoints = endpoints;
        }

        public String getMessage() {
            return message;
        }

        public List<EndpointInfo> getEndpoints() {
            return endpoints;
        }
    }
}