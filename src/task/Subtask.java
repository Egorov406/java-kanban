package task;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

    public class Subtask extends Task {

        private Integer epicId; // присваиваем подзадаче номер id эпика, которой она принадлежит


        public Subtask(String nameTask, String descriptionTask, Duration duration, LocalDateTime startTime) {
            super(nameTask, descriptionTask, duration, startTime);
        }

        public Subtask(String nameTask, String descriptionTask) {
            super(nameTask, descriptionTask);
        }

        public Integer getEpicId() {
            return epicId;
        }

        public void setEpicId(Integer epicId) {
            this.epicId = epicId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            if (!super.equals(o)) return false;
            Subtask subtask = (Subtask) o;
            return Objects.equals(epicId, subtask.epicId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(super.hashCode(), epicId);
        }

        @Override
        public String toString() {
            return "task.Subtask{" +
                    "nameSubtask=" + getNameTask() +
                    ", descriptionSubtask=" + getDescriptionTask() +
                    ", id=" + getId() +
                    ", status=" + getStatus() +
                    ", epicId=" + epicId +
                    '}';
        }

        public String toStringCsv() {
            String durationStr = getDuration() != null ? String.valueOf(getDuration().toMinutes()) : "";
            String startTimeStr = getStartTime() != null ? getStartTime().toString() : "";
            return String.join(",", String.valueOf(getId()), "SUBTASK", getNameTask(), getStatus().toString(), getDescriptionTask(), String.valueOf(epicId), durationStr, startTimeStr);
        }

    }
