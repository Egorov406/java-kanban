import manager.HistoryManager;
import manager.InMemoryHistoryManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.Epic;
import task.Subtask;
import task.Task;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {

    static HistoryManager historyManager;

    @BeforeEach
    void beforeEach() {
        historyManager = new InMemoryHistoryManager();
    }

    @Test
    void addHistory () {
    Task task1 = new Task("Задача 1", "Описание 1");
    Epic epic1 = new Epic("Эпик 1", "Описание 1");
    Subtask subtask1 = new Subtask("Подзадача 1", "Описание 1");
    subtask1.setEpicId(epic1.getId());
    historyManager.addHistory(task1);
    historyManager.addHistory(epic1);
    historyManager.addHistory(subtask1);
    List<Task> list = historyManager.getHistory();
    assertEquals(task1, list.get(0), "Должна быть задача");
    assertEquals(epic1, list.get(1), "Должен быть эпик");
    assertEquals(subtask1, list.get(2), "Должна быть подзадача");
}

@Test
void getHistory() {
    Task task1 = new Task("Задача 1", "Описание 1");
    Epic epic1 = new Epic("Эпик 1", "Описание 1");
    Subtask subtask1 = new Subtask("Подзадача 1", "Описание 1");
    subtask1.setEpicId(epic1.getId());
    historyManager.addHistory(task1);
    historyManager.addHistory(epic1);
    historyManager.addHistory(subtask1);
    historyManager.addHistory(task1);
    historyManager.addHistory(subtask1);
    historyManager.addHistory(epic1);
    List<Task> list = historyManager.getHistory();
    assertEquals(6, list.size(), "Список должен состоять из 3-х задач");
}

@Test
void removeFirstElementHistory () {
    Task task1 = new Task("Задача 1", "Описание 1");
    Epic epic1 = new Epic("Эпик 1", "Описание 1");
    Subtask subtask1 = new Subtask("Подзадача 1", "Описание 1");
    subtask1.setEpicId(epic1.getId());
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