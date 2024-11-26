import java.util.Objects;

    public class Subtask extends Task {

        Integer epicId; // присваиваем подзадаче номер id эпика, которой она принадлежит


        public Subtask(String nameTask, String descriptionTask) {
            super(nameTask, descriptionTask);

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
            return "Subtask{" +
                    "nameSubtask=" + getNameTask() +
                    ", descriptionSubtask=" + getDescriptionTask() +
                    ", idTask=" + getId() +
                    ", status=" + getStatus() +
                    ", epicId=" + epicId +
                    '}';
        }
    }
