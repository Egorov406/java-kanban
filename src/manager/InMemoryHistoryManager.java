package manager;
import task.Task;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {

    private static class Node {

        Task task;
        Node prev;
        Node next;

        public Node(Task task, Node prev, Node next) {
            this.task = task;
            this.prev = prev;
            this.next = next;

        }
    }

    private final Map<Integer, Node> nodeMap = new HashMap<>();
    private Node first;
    private Node last;


    private List<Task> getTask() {
        Node node = first;
        ArrayList<Task> tasks = new ArrayList<>();
        while (node != null) {
            tasks.add(node.task);
            node = node.next;
        }
        return tasks;
    }


    @Override
    public void addHistory(Task task) {
        remove(task.getId());
        linkLast(task);
        nodeMap.put(task.getId(), last);
    }

    @Override
    public void remove(int id) {
        Node node = nodeMap.remove(id);  // Получаем узел для удаления
        if (node != null) {
            if (node.prev != null) {
                node.prev.next = node.next;  // Если есть предыдущий узел, обновляем его ссылку на следующий
            } else {
                first = node.next;  // Если удаляется первый узел, обновляем first
            }
            if (node.next != null) {
                node.next.prev = node.prev;  // Если есть следующий узел, обновляем его ссылку на предыдущий
            } else {
                last = node.prev;  // Если удаляется последний узел, обновляем last
            }
        }
    }


    @Override
    public List<Task> getHistory() {
        return getTask();
    }

    private void linkLast(Task task) {
        Node node = new Node(task, last,null);
        if (first == null) {
            first = node;
        } else {
            last.next = node;
        }
        last = node;
    }

    @Override
    public String toString() {
        return "InMemoryHistoryManager{" +
                '}';
    }
}
