import manager.HistoryManager;
import manager.InMemoryTaskManager;
import manager.Managers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import task.Epic;
import manager.TaskManager;
import task.Subtask;
import task.Task;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {

    static TaskManager taskManager;

    @BeforeAll
    static void beforeEach() {
        taskManager = new InMemoryTaskManager();
    }


    @Test
    void addTask() {
        Task task1 = new Task("Задача 1", "Описание 1");
        taskManager.addTask(task1);
        Task task2 = new Task("Задача 2", "Описание 2");
        task2.setId(task1.getId());
        assertNotNull(task1, "Задача не создана"); // проверяем на создание
        assertEquals(task1, task2, "Задачи по ID должны быть равны"); // проверяем на равенство ID
        assertEquals(task1, taskManager.printTaskById(1), "Неверный ID задачи"); // проверяем задачу по ID
    }


    @Test
    void addSubtaskWithNotEpic() {
        Subtask subtask = new Subtask("Подзадача 1", "Описание 1");
        taskManager.addSubtask(subtask);
        assertNotNull(subtask, "Без эпика создание подзадачи невозможно");
    }

    @Test
    void addEpic() {
        Epic epic1 = new Epic("Эпик 1", "Описание 1");
        taskManager.addEpic(epic1);
        Epic epic2 = new Epic("Эпик 2", "Описание 2");
        epic2.setId(epic1.getId());
        assertNotNull(epic1, "Задача не была создана"); // проверяем на создание
        assertEquals(epic1, epic2, "Эпики по ID должны быть равны"); // проверяем на равенство по ID
        assertEquals(epic1, taskManager.printEpicById(1), "Неверный ID эпика"); // проверяем задачу по ID
    }

    @Test
    void addSubtaskWithEpic() {
        Epic epic1 = new Epic("Эпик 1", "Описание 1");
        taskManager.addEpic(epic1);
        Subtask subtask1 = new Subtask("Подзадача 1", "Описание 1");
        Subtask subtask2 = new Subtask("Позадача 2", "Описание 2");
        subtask1.setEpicId(epic1.getId());
        subtask2.setEpicId(epic1.getId());
        taskManager.addSubtask(subtask1);
        subtask2.setId(subtask1.getId());
        assertNotNull(subtask1, "Подзадача не была создана"); // проверяем на создание
        assertEquals(subtask1, subtask2, "Подзадачи по ID должны быть равны"); // проверяем на равенство по ID
        assertEquals(subtask1, taskManager.printSubtaskById(2));
    }

    @Test
    void printAllTask() {
        Task task1 = new Task("Задача 1", "Описание 1");
        Task task2 = new Task("Задача 2", "Описание 2");
        Task task3 = new Task("Задача 3", "Описание 3");
        taskManager.addTask(task1);
        taskManager.addTask(task2);
        taskManager.addTask(task3);
        List<Task> tasks = taskManager.printAllTask();
        assertEquals(3, tasks.size(), "Количество добавленных задача не равно размеру списка");
    }

    @Test
    void printAllEpic() {
        Epic epic1 = new Epic("Эпик 1", "Описание 1");
        Epic epic2 = new Epic("Эпик  2", "Описание 2");
        Epic epic3 = new Epic("Зпик  3", "Описание 3");
        taskManager.addEpic(epic1);
        taskManager.addEpic(epic2);
        taskManager.addEpic(epic3);
        List<Epic> epics = taskManager.printAllEpic();
        assertEquals(3, epics.size(), "Количество добавленных эпиков не равно размеру списка");
    }

    @Test
    void printAllSubtask() {
        Epic epic1 = new Epic("Эпик 1", "Описание 1");
        taskManager.addEpic(epic1);
        Subtask subtask1 = new Subtask("Подзадача 1", "Описание 1");
        Subtask subtask2 = new Subtask("Подзадача 2", "Описание 3");
        Subtask subtask3 = new Subtask("Подзадача 2", "Описание 3");
        subtask1.setEpicId(epic1.getId());
        subtask2.setEpicId(epic1.getId());
        subtask3.setEpicId(epic1.getId());
        taskManager.addSubtask(subtask1);
        taskManager.addSubtask(subtask2);
        taskManager.addSubtask(subtask3);
        List<Subtask> subtasks = taskManager.printAllSubtask();
        assertEquals(3, subtasks.size(), "Количество добавленных подзадач не равно размеру списка");
    }

    @Test
    void deleteAllTask() {
        Task task1 = new Task("Задача 1", "Описание 1");
        Task task2 = new Task("Задача 2", "Описание 2");
        Task task3 = new Task("Задача 3", "Описание 3");
        taskManager.addTask(task1);
        taskManager.addTask(task2);
        taskManager.addTask(task3);
        taskManager.deleteAllTask();
        assertEquals(0, taskManager.printAllTask().size(), "Список задач не был очищен");
    }

    @Test
    void deleteAllEpic() {
        Epic epic1 = new Epic("Эпик 1", "Описание 1");
        Epic epic2 = new Epic("Эпик  2", "Описание 2");
        Epic epic3 = new Epic("Зпик  3", "Описание 3");
        taskManager.addEpic(epic1);
        taskManager.addEpic(epic2);
        taskManager.addEpic(epic3);
        taskManager.deleteAllEpic();
        assertEquals(0, taskManager.printAllEpic().size(), "Список задач не был очищен");
    }

    @Test
    void deleteAllSubtask() {
        Epic epic1 = new Epic("Эпик 1", "Описание 1");
        taskManager.addEpic(epic1);
        Subtask subtask1 = new Subtask("Подзадача 1", "Описание 1");
        Subtask subtask2 = new Subtask("Подзадача 2", "Описание 3");
        Subtask subtask3 = new Subtask("Подзадача 2", "Описание 3");
        subtask1.setEpicId(epic1.getId());
        subtask2.setEpicId(epic1.getId());
        subtask3.setEpicId(epic1.getId());
        taskManager.addSubtask(subtask1);
        taskManager.addSubtask(subtask2);
        taskManager.addSubtask(subtask3);
        taskManager.deleteAllSubtask();
        assertEquals(0, taskManager.printAllSubtask().size(), "Список задач не был очищен");

    }

    @Test
    void deleteTaskById() {
        Task task1 = new Task("Задача 1", "Описание 1");
        taskManager.addTask(task1);
        taskManager.deleteTaskById(1);
        assertNull(taskManager.printTaskById(1), "Задача должна быть удалена");
    }

    @Test
    void deleteEpicById() {
        Epic epic1 = new Epic("Эпик 1", "Описание 1");
        taskManager.addEpic(epic1);
        taskManager.deleteEpicById(1);
        assertNull(taskManager.printEpicById(1), "Эпик должен быть удален");
    }

    @Test
    void deleteSubtaskById() {
        Epic epic1 = new Epic("Эпик 1", "Описание 1");
        taskManager.addEpic(epic1);
        Subtask subtask1 = new Subtask("Подзадача 1", "Описание 1");
        subtask1.setEpicId(epic1.getId());
        taskManager.addSubtask(subtask1);
        taskManager.deleteSubtaskById(2);
        assertNull(taskManager.printSubtaskById(2), "Подзадача должна быть удалена");

    }

    @Test
    void getHistory() {
        Task task1 = new Task("Задача 1", "Описание 1");
        taskManager.addTask(task1);
        Epic epic1 = new Epic("Эпик 1", "Описание 1");
        taskManager.addEpic(epic1);
        Subtask subtask1 = new Subtask("Подзадача 1", "Описание 1");
        subtask1.setEpicId(epic1.getId());
        taskManager.addSubtask(subtask1);
        taskManager.printTaskById(1);
        taskManager.printEpicById(2);
        taskManager.printSubtaskById(3);
        List<Task> list = taskManager.getHistory();
        assertEquals(3, list.size(), "Список должен состоять из 3- задач");
        assertEquals(task1, list.get(0), "Должна быть задача");
        assertEquals(epic1, list.get(1), "Должен быть эпик");
        assertEquals(subtask1, list.get(2), "Должна быть подзадача");
    }
}
