package http;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import manager.TaskManager;

import java.io.IOException;

public class PrioritizedHandler extends BaseHttpHandler implements HttpHandler {
    private final TaskManager taskManager;

    public PrioritizedHandler(TaskManager taskManager) {
        super(HttpTaskServer.getGson());
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        try {
            if ("GET".equals(method)) {
                sendText(exchange, gson.toJson(taskManager.getPrioritizedTasks()), 200);
            } else {
                sendServerError(exchange, "Метод не поддерживается");
            }
        } catch (Exception e) {
            sendServerError(exchange, "Внутренняя ошибка сервера");
        }
    }
}

