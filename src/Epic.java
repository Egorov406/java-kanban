import java.util.ArrayList;
import java.util.Objects;

    public class Epic extends Task {

        ArrayList<Integer> subTaskId = new ArrayList<>(); // создаем список id подзадач

        public Epic(String nameTask, String descriptionTask) {
            super(nameTask, descriptionTask);


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
            return "Epic{" +
                    "nameEpic=" + getNameTask() +
                    ", descriptionEpic=" + getDescriptionTask() +
                    ", idTask=" + getId() +
                    ", status=" + getStatus() +
                    ", subTaskId=" + subTaskId +
                    '}';
        }
    }

