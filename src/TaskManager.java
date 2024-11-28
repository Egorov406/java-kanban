import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {

    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();
    private final HashMap<Integer, Subtask> subTasks = new HashMap<>();

    private Integer nextId = 1; // задали счетчик ID

    // ----------------Реализация методов по выводу всех задача----------------

    public ArrayList<Task> printAllTask() { // выводим список задач (работает)
        ArrayList<Task> taskList = new ArrayList<>();
        for (Integer id : tasks.keySet()) {
            Task newTask = tasks.get(id);
            taskList.add(newTask);
        }
        return taskList;
    }

    public ArrayList<Epic> printAllEpic() {
        ArrayList<Epic> epicList = new ArrayList<>();
        for (Integer id : epics.keySet()) {
            Epic newEpic = epics.get(id);
            epicList.add(newEpic);
        }
        return epicList;
    }

    public ArrayList<Subtask> printAllSubtask() {
        ArrayList<Subtask> subtaskList = new ArrayList<>();
        for (Integer id : subTasks.keySet()) {
            Subtask newSubtask = subTasks.get(id);
            subtaskList.add(newSubtask);
        }
        return subtaskList;
    }

    //-----------------------Реализация методов по получению задачи по ID-----------------------

    public Task printTaskById(Integer idTask) { // получаем задачи по ID (работает)
        if (tasks.containsKey(idTask)) {
            return tasks.get(idTask);
        }
        return null;
    }

    public Subtask printSubtaskById(Integer idTask) { // получаем подзадачу по ID (работает)
        if (subTasks.containsKey(idTask)) {
            return subTasks.get(idTask);
        }
        return null;
    }

    public Epic printEpicById(Integer idTask) { // получаем эпик по ID (работает)
        if (epics.containsKey(idTask)) {
            return epics.get(idTask);
        }
        return null;
    }

    //------------------Реализация методов по удалению всех задач--------------------

    public void deleteAllTask() { // удаляем задачи (работает)
        tasks.clear();
    }

    public void deleteAllEpic() { // удаляем все эпики (работает)
        subTasks.clear();
        epics.clear();
    }

    public void deleteAllSubtask() { // удаляем все подзадачи с обновлением статуса эпика (работает)
        subTasks.clear();
        for (Epic epic : epics.values()) {
            epic.subTaskId.clear();
            updateStatusEpic(epic);
        }
    }

    //----------------------Реализация методов по обновлению задач---------------------------

    public void updateTask(Task task) {  // обновляем задачу
        tasks.put(task.getId(), task);
    }

    public void updateSubtask(Subtask subtask) { // обновляем подзадачу
        subTasks.put(subtask.getId(), subtask);
        Epic epic = epics.get(subtask.getEpicId());
        updateStatusEpic(epic);
    }


    public void updateStatusEpic(Epic epic) { // обновляем статус эпика
        if (epics.containsKey(epic.getId())) {
            if (epic.subTaskId.isEmpty()) { // если список подзадач пустой;
                epic.setStatus(Status.NEW); // статус эпика NEW
            } else {
                int subtaskDone = 0; // задаем счетчик выполненных подзадач
                int subtaskNew = 0;
                ArrayList<Subtask> subTaskIdArrayList = new ArrayList<>();
                for (int i = 0; i < epic.subTaskId.size(); i++) {
                    subTaskIdArrayList.add(subTasks.get(epic.subTaskId.get(i)));
                }
                for (Subtask subtask : subTaskIdArrayList) {
                    if (subtask.getStatus() == Status.DONE) {
                        subtaskDone++;
                    }
                    if (subtask.getStatus() == Status.NEW) {
                        subtaskNew++;
                    }
                    if (subtask.getStatus() == Status.IN_PROGRESS) {
                        epic.setStatus(Status.IN_PROGRESS);
                        return;
                    }
                }
                if (subtaskDone == epic.subTaskId.size()) {
                    epic.setStatus(Status.DONE);
                } else if (subtaskNew == epic.subTaskId.size()) {
                    epic.setStatus(Status.NEW);
                } else {
                    epic.setStatus(Status.IN_PROGRESS);
                }
            }
        } else {
            System.out.println("Данный эпик не найден");
        }
    }

    // ----------------------Реализация методов по добавлению задач-------------------

    public void addTask(Task task) {  // добавляем задачу
        task.setId(nextId++); // присваиваем номер ID и увеличиваем значение на 1
        tasks.put(task.getId(), task); // добавляем ключ и значение в хеш мап
    }

    public void addEpic(Epic epic) { // добавляем эпик
        epic.setId(nextId++);
        epics.put(epic.getId(), epic);
    }

    public void addSubtask(Subtask subtask) { // добавляем подзадачу

        Epic epic = epics.get(subtask.getEpicId()); // получили эпик по ID

        if (epic != null) {
            subtask.setId(nextId++); // присваиваем номер ID подзадаче
            subTasks.put(subtask.getId(), subtask);// добавляем подзадачу в мапу
            epic.subTaskId.add(subtask.getId()); // добавляем подзадачу в эпик
        } else {
            System.out.println("Такого эпика нет!");
        }
    }

    // ---------------------------Реализация методов по удалению задачи по ID-------------------------

    public void deleteTaskById(Integer idTask) { //удаляем задачу
        tasks.remove(idTask);
    }

    public void deleteEpicById(Integer idTask) { // удаляем эпик (работает)
        Epic epic = epics.get(idTask);
        if (epic != null) {
            for (Integer subTaskId : epic.subTaskId) {
                subTasks.remove(subTaskId);
            }
            epics.remove(idTask);
        } else {
            System.out.println("Такого эпика нет");
        }
    }

    public void deleteSubtaskById(Integer idTask) {// удаляем подзадачу (работает)
        Subtask subtask = subTasks.get(idTask);

        if (subtask != null) {
            Epic epic = epics.get(subtask.getEpicId());
            epic.subTaskId.remove(subtask.getId());
            updateStatusEpic(epic);
            subTasks.remove(idTask);
        } else {
            System.out.println("Такой подзадачи нет");
        }
    }

    //------------------------------Выводим список подзадача определенного эпика------------------

    public ArrayList<Subtask> printAllSubtaskEpic(Integer idTask) {
        ArrayList<Subtask> subtasksListEpic = new ArrayList<>();
        Epic epic = epics.get(idTask);
        for (int i = 0; i < epic.subTaskId.size(); i++) {
            subtasksListEpic.add(subTasks.get(epic.subTaskId.get(i)));
        }
        return subtasksListEpic;
    }
}
