import manager.Managers;
import manager.TaskManager;
import status.Status;
import task.Epic;
import task.Subtask;
import task.Task;


public class Main {

    public static void main(String[] args) {


        TaskManager taskManager = Managers.getDefault();


        Task task1 = new Task("Задач № 1", "Сел");
        taskManager.addTask(task1);

        Task task2 = new Task("Задача № 2", "Встал");
        taskManager.addTask(task2);

        Epic epic1 = new Epic("Эпик № 1", "Вспомнил");
        taskManager.addEpic(epic1);

        Subtask subtask1 = new Subtask("Подзадача № 1", "Напомнил");
        subtask1.setEpicId(epic1.getId());
        taskManager.addSubtask(subtask1);

        Subtask subtask2 = new Subtask("Подзадача № 2", "Запомнил");
        subtask2.setEpicId(epic1.getId());
        taskManager.addSubtask(subtask2);

        Epic epic2 = new Epic("Эпик № 2", "Забыл");
        taskManager.addEpic(epic2);

        Subtask subtask3 = new Subtask("Подзадача № 3", "Уплыл");
        subtask3.setEpicId(epic2.getId());
        taskManager.addSubtask(subtask3);

        taskManager.deleteAllEpic();

        System.out.println("Список задач: " + taskManager.printAllTask());
        System.out.println("Список эпиков" + taskManager.printAllEpic());
        System.out.println("Список подзадач: " + taskManager.printAllSubtask());
        System.out.println(" ");
        Task task = taskManager.printTaskById(2); // обновляем задачу
        task.setStatus(Status.IN_PROGRESS);
        taskManager.updateTask(task);
        System.out.println("Обновили задачу: " + taskManager.printTaskById(2));
        System.out.println(" ");
        Subtask subtask = taskManager.printSubtaskById(4);
        subtask.setStatus(Status.DONE);
        taskManager.updateSubtask(subtask);
        System.out.println("Обновили статус подзадачи: " + taskManager.printSubtaskById(4));
        System.out.println(" ");
        System.out.println("Уточнили статус эпика № 1 после обновления статуса его подзадачи" + taskManager.printEpicById(3));
        System.out.println(" ");
        taskManager.deleteSubtaskById(5);
        taskManager.deleteEpicById(6);
        System.out.println("Обновленный список подзадач: " + taskManager.printAllSubtask());
        System.out.println("Обновленный список эпиков: " + taskManager.printAllEpic());

        taskManager.printTaskById(1);
        taskManager.printTaskById(1);

        System.out.println(taskManager.getHistory());


    }
}