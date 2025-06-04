package task;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

    public class Epic extends Task {

        public ArrayList<Integer> subTaskId = new ArrayList<>();// создаем список id подзадач
        private LocalDateTime endTime;

        public Epic(String nameTask, String descriptionTask, Duration duration, LocalDateTime startTime) {
            super(nameTask, descriptionTask, duration, startTime);
        }

        public Epic(String nameTask, String descriptionTask) {
            super(nameTask, descriptionTask);
        }

        public void updateEpicTimes(List<Subtask> subtasks) {
            if (subtasks.isEmpty()) {
                setDuration(null);
                setStartTime(null);
                this.endTime = null;
                return;
            }

            LocalDateTime earliestStart = null;
            LocalDateTime latestEnd = null;
            long totalMinutes = 0;

            for (Subtask subtask : subtasks) {
                if (subtask.getStartTime() != null && subtask.getDuration() != null) {
                    totalMinutes += subtask.getDuration().toMinutes();
                    if (earliestStart == null || subtask.getStartTime().isBefore(earliestStart)) {
                        earliestStart = subtask.getStartTime();
                    }
                    LocalDateTime subtaskEnd = subtask.getEndTime();
                    if (subtaskEnd != null && (latestEnd == null || subtaskEnd.isAfter(latestEnd))) {
                        latestEnd = subtaskEnd;
                    }
                }
            }

            setDuration(totalMinutes > 0 ? Duration.ofMinutes(totalMinutes) : null);
            setStartTime(earliestStart);
            this.endTime = latestEnd;
        }

        public LocalDateTime getEndTime() {
            return endTime;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            if (!super.equals(o)) return false;
            Epic epic = (Epic) o;
            return Objects.equals(subTaskId, epic.subTaskId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(super.hashCode(), subTaskId);
        }

        @Override
        public String toString() {
            return "task.Epic{" +
                    "nameEpic=" + getNameTask() +
                    ", descriptionEpic=" + getDescriptionTask() +
                    ", id=" + getId() +
                    ", status=" + getStatus() +
                    ", subTaskId=" + subTaskId +
                    '}';
        }

        public String toStringCsv() {
            String durationStr = getDuration() != null ? String.valueOf(getDuration().toMinutes()) : "";
            String startTimeStr = getStartTime() != null ? getStartTime().toString() : "";
            return String.join(",", String.valueOf(getId()), "EPIC", getNameTask(), getStatus().toString(), getDescriptionTask(), "", durationStr, startTimeStr);
        }

    }

