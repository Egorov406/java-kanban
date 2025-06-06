
import manager.InMemoryTaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import status.Status;
import task.Epic;
import task.Subtask;
import task.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {


    @BeforeEach
    void setUp() {
        taskManager = new InMemoryTaskManager();  // инициализация конкретного менеджера
    }

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

    @Test
    void getPrioritizedTasks() {
        Task task1 = new Task("Task 1", "Desc", Duration.ofMinutes(30), LocalDateTime.now().plusMinutes(1000));
        Task task3 = new Task("Task 3", "Desc", Duration.ofMinutes(30), LocalDateTime.now().plusMinutes(2000));
        Task task4 = new Task("Task 4", "Desc", Duration.ofMinutes(30), LocalDateTime.now().plusMinutes(3000));

        taskManager.addTask(task1);
        taskManager.addTask(task3);
        taskManager.addTask(task4);

        List<Task> prioritizedTasks = taskManager.getPrioritizedTasks();

        assertNotNull(prioritizedTasks, "Список приоритетных задач не должен быть null");
        assertTrue(prioritizedTasks.size() >= 3, "В списке приоритетных задач должно быть минимум 3 задачи");

        assertEquals(task1.getId(), prioritizedTasks.get(0).getId(), "Задача 1 не приоритизирована");
        assertEquals(task3.getId(), prioritizedTasks.get(1).getId(), "Задача 3 не приоритизирована");
        assertEquals(task4.getId(), prioritizedTasks.get(2).getId(), "Задача 4 не приоритизирована");
    }


    @Test
    void updateEpicTimes() {
        Epic epic = new Epic("Эпик 1", "Описание 1");

        LocalDateTime now = LocalDateTime.now();

        Subtask subtask1 = new Subtask("Подзадача 1", "Описание 1", Duration.ofMinutes(30), now.plusMinutes(10));
        Subtask subtask2 = new Subtask("Подзадача 2", "Описание 2", Duration.ofMinutes(20), now);
        Subtask subtask3 = new Subtask("Подзадача 3", "Описание 3", Duration.ofMinutes(40), now.plusMinutes(15));

        List<Subtask> subtasks = List.of(subtask1, subtask2, subtask3);

        taskManager.updateEpicTimes(epic, subtasks);

        Duration expectedDuration = Duration.ofMinutes(30 + 20 + 40); // 90 мин
        LocalDateTime expectedStart = now;
        LocalDateTime expectedEnd = subtask3.getEndTime();

        assertEquals(expectedDuration, epic.getDuration(), "Длительность эпика рассчитана неправильно");
        assertEquals(expectedStart, epic.getStartTime(), "Время начала эпика рассчитано неправильно");
        assertEquals(expectedEnd, epic.getEndTime(), "Время окончания эпика рассчитано неправильно");
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
