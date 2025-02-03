package manager;

import status.Status;
import task.Epic;
import task.Subtask;
import task.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryTaskManager implements TaskManager {

    private final Map<Integer, Task> tasks = new HashMap<>();
    private final Map<Integer, Epic> epics = new HashMap<>();
    private final Map<Integer, Subtask> subTasks = new HashMap<>();

    HistoryManager historyManager = Managers.getDefaultHistory();

    private Integer nextId = 1; // задали счетчик ID


    // ----------------Выводим историю просмотров------------------------------

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    // ----------------Реализация методов по выводу всех задача----------------

    @Override
    public ArrayList<Task> printAllTask() { // выводим список задач (работает)
        ArrayList<Task> taskList = new ArrayList<>();
        for (Integer id : tasks.keySet()) {
            Task newTask = tasks.get(id);
            taskList.add(newTask);
        }
        return taskList;
    }

    @Override
    public ArrayList<Epic> printAllEpic() {
        ArrayList<Epic> epicList = new ArrayList<>();
        for (Integer id : epics.keySet()) {
            Epic newEpic = epics.get(id);
            epicList.add(newEpic);
        }
        return epicList;
    }

    @Override
    public ArrayList<Subtask> printAllSubtask() {
        ArrayList<Subtask> subtaskList = new ArrayList<>();
        for (Integer id : subTasks.keySet()) {
            Subtask newSubtask = subTasks.get(id);
            subtaskList.add(newSubtask);
        }
        return subtaskList;
    }

    //-----------------------Реализация методов по получению задачи по ID-----------------------

    @Override
    public Task printTaskById(Integer idTask) {// получаем задачи по ID (работает)
        historyManager.addHistory(tasks.get(idTask));
        if (tasks.containsKey(idTask)) {
            return tasks.get(idTask);
        }
        return null;
    }

    @Override
    public Subtask printSubtaskById(Integer idTask) { // получаем подзадачу по ID (работает)
        historyManager.addHistory(subTasks.get(idTask));
        if (subTasks.containsKey(idTask)) {
            return subTasks.get(idTask);
        }
        return null;
    }

    @Override
    public Epic printEpicById(Integer idTask) {// получаем эпик по ID (работает)
        historyManager.addHistory(epics.get(idTask));
        if (epics.containsKey(idTask)) {
            return epics.get(idTask);
        }
        return null;
    }

    //------------------Реализация методов по удалению всех задач--------------------

    @Override
    public void deleteAllTask() { // удаляем задачи (работает)
        tasks.clear();
    }

    @Override
    public void deleteAllEpic() { // удаляем все эпики (работает)
        subTasks.clear();
        epics.clear();
    }

    @Override
    public void deleteAllSubtask() { // удаляем все подзадачи с обновлением статуса эпика (работает)
        subTasks.clear();
        for (Epic epic : epics.values()) {
            epic.subTaskId.clear();
            updateStatusEpic(epic);
        }
    }

    //----------------------Реализация методов по обновлению задач---------------------------

    @Override
    public void updateTask(Task task) {  // обновляем задачу
        tasks.put(task.getId(), task);
    }

    @Override
    public void updateSubtask(Subtask subtask) { // обновляем подзадачу
        subTasks.put(subtask.getId(), subtask);
        Epic epic = epics.get(subtask.getEpicId());
        updateStatusEpic(epic);
    }


    private void updateStatusEpic(Epic epic) { // обновляем статус эпика
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

    @Override
    public void addTask(Task task) {  // добавляем задачу
        task.setId(nextId++); // присваиваем номер ID и увеличиваем значение на 1
        tasks.put(task.getId(), task); // добавляем ключ и значение в хеш мап
    }

    @Override
    public void addEpic(Epic epic) { // добавляем эпик
        epic.setId(nextId++);
        epics.put(epic.getId(), epic);
    }

    @Override
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

    @Override
    public void deleteTaskById(Integer idTask) { //удаляем задачу
        tasks.remove(idTask);
        historyManager.remove(idTask);
    }

    @Override
    public void deleteEpicById(Integer idTask) { // удаляем эпик (работает)
        Epic epic = epics.get(idTask);
        if (epic != null) {
            for (Integer subTaskId : epic.subTaskId) {
                subTasks.remove(subTaskId);
                historyManager.remove(subTaskId);
            }
            epics.remove(idTask);
            historyManager.remove(idTask);
        } else {
            System.out.println("Такого эпика нет");
        }
    }

    @Override
    public void deleteSubtaskById(Integer idTask) {// удаляем подзадачу (работает)
        Subtask subtask = subTasks.get(idTask);

        if (subtask != null) {
            Epic epic = epics.get(subtask.getEpicId());
            epic.subTaskId.remove(subtask.getId());
            updateStatusEpic(epic);
            subTasks.remove(idTask);
            historyManager.remove(idTask);
        } else {
            System.out.println("Такой подзадачи нет");
        }
    }

    //------------------------------Выводим список подзадача определенного эпика------------------

    @Override
    public ArrayList<Subtask> printAllSubtaskEpic(Integer idTask) {
        ArrayList<Subtask> subtasksListEpic = new ArrayList<>();
        Epic epic = epics.get(idTask);
        for (int i = 0; i < epic.subTaskId.size(); i++) {
            subtasksListEpic.add(subTasks.get(epic.subTaskId.get(i)));
        }
        return subtasksListEpic;
    }
}

