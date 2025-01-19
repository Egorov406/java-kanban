package manager;
import task.Epic;
import task.Subtask;
import task.Task;
import java.util.List;

public interface TaskManager {

    List<Task> getHistory();

    List<Task> printAllTask();

    List<Epic> printAllEpic();

    List<Subtask> printAllSubtask();

    Task printTaskById(Integer idTask);

    Subtask printSubtaskById(Integer idTask);

    Epic printEpicById(Integer idTask);

    void deleteAllTask();

    void deleteAllEpic();

    void deleteAllSubtask();

    void updateTask(Task task);

    void updateSubtask(Subtask subtask);

    //void updateStatusEpic(Epic epic);

    void addTask(Task task);

    void addEpic(Epic epic);

    void addSubtask(Subtask subtask);

    void deleteTaskById(Integer idTask);

    void deleteEpicById(Integer idTask);

    void deleteSubtaskById(Integer idTask);

    List<Subtask> printAllSubtaskEpic(Integer idTask);
}