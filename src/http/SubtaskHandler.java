package http;

import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import manager.TaskManager;
import task.Subtask;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class SubtaskHandler extends BaseHttpHandler implements HttpHandler {
    private final TaskManager taskManager;

    public SubtaskHandler(TaskManager taskManager) {
        super(HttpTaskServer.getGson());
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();
        String[] pathParts = path.split("/");

        try {
            switch (method) {
                case "GET":
                    if (pathParts.length == 2) {
                        // GET /subtasks — получить все подзадачи
                        List<Subtask> subtasks = taskManager.printAllSubtask();
                        sendText(exchange, gson.toJson(subtasks), 200);
                    } else if (pathParts.length == 3) {
                        // GET /subtasks/{id} — получить подзадачу по ID
                        int id = Integer.parseInt(pathParts[2]);
                        Subtask subtask = taskManager.printSubtaskById(id);
                        if (subtask == null) {
                            sendNotFound(exchange);
                        } else {
                            sendText(exchange, gson.toJson(subtask), 200);
                        }
                    }
                    break;

                case "POST":
                    // POST /subtasks — создать или обновить подзадачу
                    InputStream inputStream = exchange.getRequestBody();
                    String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
                    Subtask subtask = gson.fromJson(body, Subtask.class);
                    try {
                        if (subtask.getId() == 0) {
                            taskManager.addSubtask(subtask);
                        } else {
                            taskManager.updateSubtask(subtask);
                        }
                        sendText(exchange, "{\"status\": \"success\"}", 201);
                    } catch (IllegalStateException e) {
                        sendHasInteractions(exchange);
                    }
                    break;

                case "DELETE":
                    if (pathParts.length == 3) {
                        // DELETE /subtasks/{id} — удалить подзадачу по ID
                        int id = Integer.parseInt(pathParts[2]);
                        taskManager.deleteSubtaskById(id);
                        sendText(exchange, "{\"status\": \"success\"}", 201);
                    } else if (pathParts.length == 2) {
                        // DELETE /subtasks — удалить все подзадачи
                        taskManager.deleteAllSubtask();
                        sendText(exchange, "{\"status\": \"success\"}", 201);
                    }
                    break;

                default:
                    sendServerError(exchange, "Метод не поддерживается");
            }
        } catch (JsonSyntaxException e) {
            sendServerError(exchange, "Неверный формат JSON");
        } catch (NumberFormatException e) {
            sendNotFound(exchange);
        } catch (RuntimeException e) {
            sendServerError(exchange, "Ошибка при сохранении данных: " + e.getMessage());
        } catch (Exception e) {
            sendServerError(exchange, "Внутренняя ошибка сервера");
        }
    }
}
