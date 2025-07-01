package http;

import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import manager.TaskManager;
import task.Task;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

    public class TaskHandler extends BaseHttpHandler implements HttpHandler {
        private final TaskManager taskManager;

        public TaskHandler(TaskManager taskManager) {
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
                            // GET /tasks — получить все задачи
                            List<Task> tasks = taskManager.printAllTask();
                            sendText(exchange, gson.toJson(tasks), 200);
                        } else if (pathParts.length == 3) {
                            // GET /tasks/{id} — получить задачу по ID
                            int id = Integer.parseInt(pathParts[2]);
                            Task task = taskManager.printTaskById(id);
                            if (task == null) {
                                sendNotFound(exchange);
                            } else {
                                sendText(exchange, gson.toJson(task), 200);
                            }
                        }
                        break;

                    case "POST":
                        // POST /tasks — создать или обновить задачу
                        InputStream inputStream = exchange.getRequestBody();
                        String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
                        Task task = gson.fromJson(body, Task.class);
                        try {
                            if (task.getId() == 0) {
                                taskManager.addTask(task);
                            } else {
                                taskManager.updateTask(task);
                            }
                            sendText(exchange, "{\"status\": \"success\"}", 201);
                        } catch (IllegalStateException e) {
                            sendHasInteractions(exchange);
                        }
                        break;

                    case "DELETE":
                        if (pathParts.length == 3) {
                            // DELETE /tasks/{id} — удалить задачу по ID
                            int id = Integer.parseInt(pathParts[2]);
                            taskManager.deleteTaskById(id);
                            sendText(exchange, "{\"status\": \"success\"}", 201);
                        } else if (pathParts.length == 2) {
                            // DELETE /tasks — удалить все задачи
                            taskManager.deleteAllTask();
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
