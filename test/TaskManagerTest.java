import manager.TaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import status.Status;
import task.Epic;
import task.Subtask;
import task.Task;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public abstract class TaskManagerTest <T extends TaskManager> {
    protected T taskManager;

    @BeforeEach
    abstract void setUp();

    @Test
    void addTaskAndGetById() {
        Task task = new Task("Task", "Desc", Duration.ofMinutes(30), LocalDateTime.now());
        taskManager.addTask(task);
        Task retrieved = taskManager.printTaskById(task.getId());
        assertEquals(task, retrieved, "Задача должна быть найдена по ID");
    }

    @Test
    void addEpicAndCalculateStatus() {
        Epic epic = new Epic("Epic", "Desc");
        taskManager.addEpic(epic);

        Subtask sub1 = new Subtask("Sub1", "Desc1", Duration.ofMinutes(30), LocalDateTime.now());
        sub1.setEpicId(epic.getId());
        Subtask sub2 = new Subtask("Sub2", "Desc2", Duration.ofMinutes(30), LocalDateTime.now().plusHours(1));
        sub2.setEpicId(epic.getId());
        taskManager.addSubtask(sub1);
        taskManager.addSubtask(sub2);

        assertEquals(Status.NEW, epic.getStatus(), "Статус эпика с подзадачами NEW должен быть NEW");

        sub1.setStatus(Status.DONE);
        taskManager.updateSubtask(sub1);
        assertEquals(Status.IN_PROGRESS, epic.getStatus(), "Статус эпика с NEW и DONE должен быть IN_PROGRESS");

        sub2.setStatus(Status.DONE);
        taskManager.updateSubtask(sub2);
        assertEquals(Status.DONE, epic.getStatus(), "Статус эпика с подзадачами DONE должен быть DONE");

        sub1.setStatus(Status.IN_PROGRESS);
        taskManager.updateSubtask(sub1);
        assertEquals(Status.IN_PROGRESS, epic.getStatus(), "Статус эпика с IN_PROGRESS должен быть IN_PROGRESS");
    }

    @Test
    void checkTaskOverlap() {
        Task task1 = new Task("Task1", "Desc", Duration.ofMinutes(60), LocalDateTime.now());
        taskManager.addTask(task1);

        Task task2 = new Task("Task2", "Desc", Duration.ofMinutes(30), LocalDateTime.now().plusMinutes(30));
        assertThrows(IllegalStateException.class, () -> taskManager.addTask(task2), "Должно быть исключение при пересечении");

        Task task3 = new Task("Task3", "Desc", Duration.ofMinutes(30), LocalDateTime.now().plusHours(2));
        assertDoesNotThrow(() -> taskManager.addTask(task3), "Задача без пересечения должна добавляться");
    }
}

