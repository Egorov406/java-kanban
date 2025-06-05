
import manager.InMemoryTaskManager;
import manager.TaskManager;
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

 abstract class TaskManagerTest <T extends TaskManager> {
     protected T taskManager;

     @BeforeEach
     void setUp() {
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

         InMemoryTaskManager manager = new InMemoryTaskManager(); // 👈 конкретный менеджер
         manager.updateEpicTimes(epic, subtasks);

         Duration expectedDuration = Duration.ofMinutes(30 + 20 + 40); // 90 мин
         LocalDateTime expectedStart = now;
         LocalDateTime expectedEnd = subtask3.getEndTime();

         assertEquals(expectedDuration, epic.getDuration(), "Длительность эпика рассчитана неправильно");
         assertEquals(expectedStart, epic.getStartTime(), "Время начала эпика рассчитано неправильно");
         assertEquals(expectedEnd, epic.getEndTime(), "Время окончания эпика рассчитано неправильно");
     }


 }



