package org.example.client;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class ApiClient {
    private static final String BASE_URL = "http://localhost:7000";
    private static final ObjectMapper mapper = new ObjectMapper();

    public static void main(String[] args) throws Exception {
        createTask();
        getAllTasks();
        getTaskById("algum-id");
        getStatus();
    }

    private static void createTask() throws Exception {
        URL url = new URL(BASE_URL + "/tarefas");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);

        String jsonInput = "{\"titulo\":\"Minha Tarefa\",\"descricao\":\"Descrição opcional\"}";
        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = jsonInput.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        System.out.println("POST /tarefas Response Code: " + conn.getResponseCode());
        try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
            System.out.println("Response: " + br.readLine());
        }
        conn.disconnect();
    }

    private static void getAllTasks() throws Exception {
        URL url = new URL(BASE_URL + "/tarefas");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        System.out.println("GET /tarefas Response Code: " + conn.getResponseCode());
        try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
            System.out.println("Response: " + br.readLine());
        }
        conn.disconnect();
    }

    private static void getTaskById(String id) throws Exception {
        URL url = new URL(BASE_URL + "/tarefas/" + id);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        System.out.println("GET /tarefas/" + id + " Response Code: " + conn.getResponseCode());
        try (BufferedReader br = new BufferedReader(new InputStreamReader(
                conn.getResponseCode() >= 400 ? conn.getErrorStream() : conn.getInputStream(), StandardCharsets.UTF_8))) {
            System.out.println("Response: " + br.readLine());
        }
        conn.disconnect();
    }

    private static void getStatus() throws Exception {
        URL url = new URL(BASE_URL + "/status");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        System.out.println("GET /status Response Code: " + conn.getResponseCode());
        try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
            System.out.println("Response: " + br.readLine());
        }
        conn.disconnect();
    }
}