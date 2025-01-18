package manager;

import task.Epic;
import task.Subtask;
import task.Task;

import java.util.ArrayList;
import java.util.List;

public interface TaskManager {

    List<Task> getHistory();

    ArrayList<Task> printAllTask();

    ArrayList<Epic> printAllEpic();

    ArrayList<Subtask> printAllSubtask();

    Task printTaskById(Integer idTask);

    Subtask printSubtaskById(Integer idTask);

    Epic printEpicById(Integer idTask);

    void deleteAllTask();

    void deleteAllEpic();

    void deleteAllSubtask();

    void updateTask(Task task);

    void updateSubtask(Subtask subtask);

    void updateStatusEpic(Epic epic);

    void addTask(Task task);

    void addEpic(Epic epic);

    void addSubtask(Subtask subtask);

    void deleteTaskById(Integer idTask);

    void deleteEpicById(Integer idTask);

    void deleteSubtaskById(Integer idTask);

    ArrayList<Subtask> printAllSubtaskEpic(Integer idTask);
}