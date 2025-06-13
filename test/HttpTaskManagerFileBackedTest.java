import com.google.gson.Gson;
import http.HttpTaskServer;
import manager.FileBackedTaskManager;
import manager.TaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import task.Task;

import java.net.URI;
import java.io.File;
import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static task.TaskType.TASK;

public class HttpTaskManagerFileBackedTest {
    private TaskManager manager;
    private HttpTaskServer taskServer;
    private final Gson gson = HttpTaskServer.getGson();
    private final HttpClient client = HttpClient.newHttpClient();
    private final File file = new File("test_tasks.csv");

    @BeforeEach
    public void setUp() throws IOException {
        file.delete();
        file.createNewFile();
        manager = FileBackedTaskManager.loadFromFile(file);
        taskServer = new HttpTaskServer(file);
        manager.deleteAllTask();
        manager.deleteAllSubtask();
        manager.deleteAllEpic();
        taskServer.start();
    }

    @AfterEach
    public void shutDown() {
        taskServer.stop();
        file.delete();
    }

    @Test
    public void testAddTaskAndSaveToFile() throws IOException, InterruptedException {
        Task task = new Task("Задача", "Описание", Duration.ofMinutes(30), LocalDateTime.now());
        manager.addTask(task);
        String taskJson = gson.toJson(task);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks"))
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());

        // Проверяем, что задача сохранена в файл
        TaskManager newManager = FileBackedTaskManager.loadFromFile(file);
        List<Task> tasksFromManager = newManager.printAllTask();
        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
        assertEquals("Задача", tasksFromManager.get(0).getNameTask(), "Некорректное имя задачи");
    }
}

