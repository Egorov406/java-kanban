
import manager.HistoryManager;
import manager.InMemoryHistoryManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.Task;


import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {
    private HistoryManager historyManager;

    @BeforeEach
    void setUp() {
        historyManager = new InMemoryHistoryManager();
    }

    @Test
    void emptyHistory() {
        assertTrue(historyManager.getHistory().isEmpty(), "История должна быть пустой");
    }

    @Test
    void addAndRemoveFromHistory() {
        Task task1 = new Task("Задача1", "Описание1");
        task1.setId(1);
        Task task2 = new Task("Задача2", "Описание2");
        task2.setId(2);

        historyManager.addHistory(task1);
        historyManager.addHistory(task2);
        assertEquals(2, historyManager.getHistory().size(), "История должна содержать две задачи");

        historyManager.addHistory(task1); // Дублирование
        assertEquals(2, historyManager.getHistory().size(), "Дублирующиеся задачи не должны добавляться");

        historyManager.remove(task1.getId()); // Удаление из начала
        assertEquals(1, historyManager.getHistory().size(), "Задача должна быть удалена");
        assertEquals(task2, historyManager.getHistory().get(0), "Оставшаяся задача должна быть Задача2");

        historyManager.addHistory(task1);
        historyManager.remove(task1.getId()); // Удаление из конца
        assertEquals(1, historyManager.getHistory().size(), "Задача должна быть удалена");
    }
}
