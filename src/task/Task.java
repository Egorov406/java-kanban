package task;
import status.Status;
import java.util.Objects;

public class Task {

    private Integer id;
    private String nameTask;  // наименование задачи
    private String descriptionTask; // описание задачи;
    private Status status; // идентификатор задачи

    public Task(String nameTask, String descriptionTask) {
        this.nameTask = nameTask;
        this.descriptionTask = descriptionTask;
        this.status = Status.NEW;
    }

    public Integer getId() {
        return id;
    }

    public int setId(Integer idTask) {
        this.id = idTask;
        return 0;
    }

    public String getDescriptionTask() {
        return descriptionTask;
    }


    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getNameTask() {
        return nameTask;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(nameTask, descriptionTask, id, status);
    }

    @Override
    public String toString() {
        return "task.Task{" +
                "nameTask='" + nameTask + '\'' +
                ", descriptionTask='" + descriptionTask + '\'' +
                ", id=" + id +
                ", status=" + status +
                '}';
    }

}

