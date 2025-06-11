package task;

import status.Status;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class Task {


    private Integer id;
    private String nameTask;  // наименование задачи
    private String descriptionTask; // описание задачи;
    private Status status;// идентификатор задачи
    private Duration duration; // продолжительность задачи
    private LocalDateTime startTime; // время старта задачи


    public Task(String nameTask, String descriptionTask) {
        this.nameTask = nameTask;
        this.descriptionTask = descriptionTask;
        this.status = Status.NEW;
    }

    public Task(String nameTask, String descriptionTask, Duration duration, LocalDateTime startTime) {
        this.nameTask = nameTask;
        this.descriptionTask = descriptionTask;
        this.status = Status.NEW;
        this.duration = duration;
        this.startTime = startTime;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        if (startTime == null || duration == null) {
            return null;
        }
        return startTime.plus(duration);
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

    public boolean isOverlapping(Task other) {
        if (this.getStartTime() == null || this.getDuration() == null ||
                other.getStartTime() == null || other.getDuration() == null) {
            return false;
        }
        LocalDateTime thisStart = this.getStartTime();
        LocalDateTime thisEnd = this.getEndTime();
        LocalDateTime otherStart = other.getStartTime();
        LocalDateTime otherEnd = other.getEndTime();
        return !thisEnd.isBefore(otherStart) && !otherEnd.isBefore(thisStart);
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

    public String toStringCsv() {
        String durationStr = duration != null ? String.valueOf(duration.toMinutes()) : "";
        String startTimeStr = startTime != null ? startTime.toString() : "";
        return String.join(",", String.valueOf(id), "TASK", nameTask, status.toString(), descriptionTask, "", durationStr, startTimeStr);
    }
}

