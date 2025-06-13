package http;

import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import manager.TaskManager;
import task.Epic;
import task.Subtask;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;


public class EpicHandler extends BaseHttpHandler implements HttpHandler {
    private final TaskManager taskManager;

    public EpicHandler(TaskManager taskManager) {
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
                        // GET /epics — получить все эпики
                        List<Epic> epics = taskManager.printAllEpic();
                        sendText(exchange, gson.toJson(epics), 200);
                    } else if (pathParts.length == 3) {
                        // GET /epics/{id} — получить эпик по ID
                        int id = Integer.parseInt(pathParts[2]);
                        Epic epic = taskManager.printEpicById(id);
                        if (epic == null) {
                            sendNotFound(exchange);
                        } else {
                            sendText(exchange, gson.toJson(epic), 200);
                        }
                    } else if (pathParts.length == 4 && "subtasks".equals(pathParts[3])) {
                        // GET /epics/{id}/subtasks — получить подзадачи эпика
                        int id = Integer.parseInt(pathParts[2]);
                        List<Subtask> subtasks = taskManager.printAllSubtaskEpic(id);
                        sendText(exchange, gson.toJson(subtasks), 200);
                    }
                    break;

                case "POST":
                    // POST /epics — создать или обновить эпик
                    InputStream inputStream = exchange.getRequestBody();
                    String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
                    Epic epic = gson.fromJson(body, Epic.class);
                    if (epic.getId() == 0) {
                        taskManager.addEpic(epic);
                    } else {
                        taskManager.addEpic(epic); // Эпики не обновляются, только создаются
                    }
                    sendText(exchange, "{\"status\": \"success\"}", 201);
                    break;

                case "DELETE":
                    if (pathParts.length == 3) {
                        // DELETE /epics/{id} — удалить эпик по ID
                        int id = Integer.parseInt(pathParts[2]);
                        taskManager.deleteEpicById(id);
                        sendText(exchange, "{\"status\": \"success\"}", 201);
                    } else if (pathParts.length == 2) {
                        // DELETE /epics — удалить все эпики
                        taskManager.deleteAllEpic();
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
