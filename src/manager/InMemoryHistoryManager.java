package manager;

import task.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    private final static int historyLimit = 10;
    private List <Task> taskViewing = new ArrayList<>();

    @Override
    public void addHistory(Task task) {
        if (task != null) {
            taskViewing.add(task);
            if (taskViewing.size() > historyLimit) {
                taskViewing.remove(0);
            }
        } else {
            System.out.println("Такой задачи нет.");
        }
    }



    @Override
    public List<Task> getHistory() {
        return new ArrayList<>(taskViewing);
    }

    @Override
    public String toString() {
        return "InMemoryHistoryManager{" +
                "taskViewing=" + taskViewing +
                '}';
    }
}
