import manager.FileBackedTaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.Epic;
import task.Subtask;
import task.Task;
import java.io.File;
import java.io.IOException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FileBackedTaskManagerTest {

    private FileBackedTaskManager manager;
    private File testFile;

    @BeforeEach
    public void setUp() throws IOException {
        // Создаем временный файл для тестов
        testFile = File.createTempFile("task_manager", ".csv");
        manager = new FileBackedTaskManager(testFile);
    }

    @AfterEach
    public void tearDown() {
        // Удаляем временный файл после тестов
        testFile.delete();
    }


    @Test
    public void testSaveAndLoadEmptyFile() {
        // Проверяем загрузку из пустого файла
        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(testFile);
        assertTrue(loadedManager.printAllTask().isEmpty(), "Задачи должны быть пустыми");
        assertTrue(loadedManager.printAllEpic().isEmpty(), "Эпики должны быть пустыми");
        assertTrue(loadedManager.printAllSubtask().isEmpty(), "Подзадачи должны быть пустыми");
    }

    @Test
    public void testSaveMultipleTasks() {
        // Создание задач
        Task task1 = new Task("Задача 1", "Описание задачи 1");
        task1.setId(1);
        Epic epic1 = new Epic("Эпик 1", "Описание эпика 1");
        epic1.setId(2);
        Subtask subtask1 = new Subtask("Подзадача 1", "Описание подзадачи 1");
        subtask1.setId(3);
        subtask1.setEpicId(epic1.getId());

        // Добавляем задачи в менеджер
        manager.addTask(task1);
        manager.addEpic(epic1);
        manager.addSubtask(subtask1);

        // Сохраняем в файл
        manager.save();

        // Загружаем из файла
        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(testFile);

        // Проверяем, что задачи загружены корректно
        assertEquals(1, loadedManager.printAllTask().size(), "Должна быть 1 задача");
        assertEquals(1, loadedManager.printAllEpic().size(), "Должен быть 1 эпик");
        assertEquals(1, loadedManager.printAllSubtask().size(), "Должна быть 1 подзадача");
    }

}
