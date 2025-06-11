import manager.FileBackedTaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.Epic;
import task.Subtask;
import task.Task;
import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

public class FileBackedTaskManagerTest extends TaskManagerTest<FileBackedTaskManager> {
    private File file;

    @Override
    @BeforeEach
    void setUp() {
        try {
            file = File.createTempFile("tasks", ".csv");
            taskManager = new FileBackedTaskManager(file);
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при создании временного файла", e);
        }
    }

    @Test
    void saveAndLoadFromFile() {
        Task task = new Task("Task", "Desc", Duration.ofMinutes(30), LocalDateTime.now());
        Epic epic = new Epic("Epic", "Desc");
        Subtask subtask = new Subtask("Subtask", "Desc", Duration.ofMinutes(30), LocalDateTime.now().plusHours(1));
        //subtask.setEpicId(epic.getId());

        taskManager.addTask(task);
        taskManager.addEpic(epic);
        subtask.setEpicId(epic.getId());
        taskManager.addSubtask(subtask);

        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(file);
        assertEquals(taskManager.printTaskById(task.getId()).toStringCsv(), loadedManager.printTaskById(task.getId()).toStringCsv(), "Задача должна быть загружена корректно");
        assertEquals(taskManager.printEpicById(epic.getId()).toStringCsv(), loadedManager.printEpicById(epic.getId()).toStringCsv(), "Эпик должен быть загружен корректно");
        assertEquals(taskManager.printSubtaskById(subtask.getId()).toStringCsv(), loadedManager.printSubtaskById(subtask.getId()).toStringCsv(), "Подзадача должна быть загружена корректно");
    }

    @Test
    void loadFromFileWithException() {
        File invalidFile = new File("nonexistent.csv");
        assertThrows(RuntimeException.class, () -> FileBackedTaskManager.loadFromFile(invalidFile), "Должно быть исключение при чтении несуществующего файла");
    }
}

