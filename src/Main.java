import manager.FileBackedTaskManager;
import task.Epic;
import task.Subtask;
import task.Task;
import java.io.File;


public class Main {

    public static void main(String[] args) {


        File tempFile = new File("tasks.csv");


        FileBackedTaskManager manager = new FileBackedTaskManager(tempFile);


        Task task1 = new Task("Task 1", "Description of Task 1");
        manager.addTask(task1);

        Epic epic1 = new Epic("Epic 1", "Description of Epic 1");
        manager.addEpic(epic1);

        Subtask subtask1 = new Subtask("Subtask 1", "Description of Subtask 1");
        subtask1.setEpicId(epic1.getId());
        manager.addSubtask(subtask1);

        Subtask subtask2 = new Subtask("Subtask 2", "Description of Subtask 2");
        subtask2.setEpicId(epic1.getId());
        manager.addSubtask(subtask2);


        manager.save();


        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(tempFile);


        System.out.println("Загруженные задачи:");
        for (Task task : loadedManager.printAllTask()) {
            System.out.println(task);
        }

        for (Epic epic : loadedManager.printAllEpic()) {
            System.out.println(epic);
        }

        for (Subtask subtask : loadedManager.printAllSubtask()) {
            System.out.println(subtask);
        }
    }
}