
import manager.HistoryManager;
import manager.Managers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import task.Epic;
import task.Subtask;
import task.Task;
import status.Status;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {


    HistoryManager historyManager = Managers.getDefaultHistory();

    static Task task1, task2;
    static Epic epic1, epic2;
    static Subtask subtask1, subtask2;
    static int id = 0;

    @BeforeAll
    static void createTasks() {
        task1 = new Task("Task-1", "Описание-1");
        task1.setId(id++);
        task2 = new Task("Task-2", "Описание-2");
        task2.setId(id++);
        epic1 = new Epic("Epic-1", "Описание-1");
        epic1.setId(id++);
        epic2 = new Epic("Epic-2", "Описание-2");
        epic2.setId(id++);
        subtask1 = new Subtask("SubTask-1", "Описание-1");
        subtask1.setEpicId(epic1.getId());
        subtask1.setId(id++);
        subtask2 = new Subtask("SubTask-2", "Описание-2");
        subtask2.setEpicId(epic1.getId());
        subtask2.setId(id++);
    }


    @Test
    void addDoubleHistory () {
    historyManager.addHistory(epic1);
    historyManager.addHistory(epic1);
    historyManager.addHistory(epic1);
    List<Task> list = historyManager.getHistory();
    assertEquals(1, list.size(), "В историю просмотров записались дубли просмотра задачи");


}

@Test
void addAndRemoveTask() {

    historyManager.addHistory(task1);
    historyManager.addHistory(subtask2);
    historyManager.addHistory(subtask1);
    historyManager.addHistory(task2);
    historyManager.addHistory(epic1);
    historyManager.addHistory(epic2);
    historyManager.remove(task1.getId());
    List<Task> list = historyManager.getHistory();
    assertEquals(5, list.size(), "Список должен состоять из 5-ти задач");
    assertEquals(epic2, list.getLast(), "Последние задачи не совпадают");
    assertArrayEquals(list.toArray(), historyManager.getHistory().toArray(), "Задачи не совпадают");
}

@Test
void removeFirstElementHistory () {

    historyManager.addHistory(task1);
    historyManager.addHistory(epic1);
    historyManager.addHistory(subtask1);
    List<Task> list = historyManager.getHistory();
    list.remove(0);
    assertEquals(2, list.size(), "После удаления список должен состоять из 2-х задач");
    assertEquals(epic1, list.get(0), "Должен быть эпик");
    assertEquals(subtask1, list.get(1), "Должна быть подзадача");

}


}