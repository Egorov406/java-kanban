package manager;
import status.Status;
import task.*;
import task.Task;
import java.io.*;
import java.time.Duration;
import java.time.LocalDateTime;
import static task.TaskType.*;


public class FileBackedTaskManager extends InMemoryTaskManager {

    private final File file;

    public FileBackedTaskManager(File file) {
        this.file = file;
    }

    @Override
    public void addTask(Task task) {  // добавляем задачу
        super.addTask(task);
        save();
    }

    @Override
    public void addEpic(Epic epic) {
        super.addEpic(epic);
        save();
    }

    @Override
    public void addSubtask(Subtask subtask) {
        super.addSubtask(subtask);
        Epic epic = printEpicById(subtask.getEpicId());
        if (epic != null) {
            epic.updateEpicTimes(printAllSubtaskEpic(subtask.getEpicId()));
        }
        save();
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager manager = new FileBackedTaskManager(file);
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            boolean firstLine = true;
            while ((line = reader.readLine()) != null) {
                // Пропускаем заголовок
                if (firstLine) {
                    firstLine = false;
                    continue;
                }
                // Пропускаем пустые строки
                if (line.trim().isEmpty()) {
                    continue;
                }
                // Парсим и добавляем задачу
                Task task = manager.fromString(line);
                if (task instanceof Epic) {
                    manager.addEpic((Epic) task);
                } else if (task instanceof Subtask) {
                    manager.addSubtask((Subtask) task);
                } else if (task instanceof Task) {
                    manager.addTask(task);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при чтении данных из файла", e);
        }

        for (Epic epic : manager.printAllEpic()) {
            epic.updateEpicTimes(manager.printAllSubtaskEpic(epic.getId()));
        }
        return manager;
    }



        public void save() {

            try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
                bw.write("id,type,name,status,description,epicId,duration,startTime");
                bw.newLine();
                for (Task task : printAllTask()) {
                    bw.write(task.toStringCsv());
                    bw.newLine();
                }
                for (Epic epic : printAllEpic()) {
                    bw.write(epic.toStringCsv());
                    bw.newLine();
                }
                for (Subtask subTask : printAllSubtask()) {
                    bw.write(subTask.toStringCsv());
                    bw.newLine();
                }
            } catch (IOException e) {
                throw new RuntimeException("Ошибка при сохранении данных в файл", e);
            }

        }

        public Task fromString(String value) {
            String[] str = value.split(",");
            if (str.length < 5) {
                throw new IllegalArgumentException("Недостаточно данных для создания задачи: " + value);
            }

            int id = Integer.parseInt(str[0]);
            TaskType type = valueOf(str[1]);
            String name = str[2];
            Status status = Status.valueOf(str[3]);
            String description = str[4];


                switch (type) {
                    case TASK -> {
                        Duration duration = str.length > 6 && !str[6].isEmpty() ? Duration.ofMinutes(Long.parseLong(str[6])) : null;
                        LocalDateTime startTime = str.length > 7 && !str[7].isEmpty() ? LocalDateTime.parse(str[7]) : null;
                        Task task = new Task(name, description, duration, startTime);
                        task.setId(id);
                        task.setStatus(status);
                        return task;
                    }
                    case EPIC -> {
                        Duration duration = str.length > 6 && !str[6].isEmpty() ? Duration.ofMinutes(Long.parseLong(str[6])) : null;
                        LocalDateTime startTime = str.length > 7 && !str[7].isEmpty() ? LocalDateTime.parse(str[7]) : null;
                        Epic epic = new Epic(name, description, duration, startTime);
                        epic.setId(id);
                        epic.setStatus(status);
                        return epic;
                    }
                    case SUBTASK -> {
                        int epicId = (str.length > 5 && !str[5].isEmpty()) ? Integer.parseInt(str[5]) : 0;
                        Duration duration = str.length > 6 && !str[6].isEmpty() ? Duration.ofMinutes(Long.parseLong(str[6])) : null;
                        LocalDateTime startTime = str.length > 7 && !str[7].isEmpty() ? LocalDateTime.parse(str[7]) : null;
                        Subtask subtask = new Subtask(name, description, duration, startTime);
                        subtask.setId(id);
                        subtask.setStatus(status);
                        subtask.setEpicId(epicId);
                        return subtask;
                    }
                    default -> throw new IllegalArgumentException("Неизвестный тип задачи: " + type);
                }
            }
        }





