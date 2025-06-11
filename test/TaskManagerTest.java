
import manager.TaskManager;
import org.junit.jupiter.api.BeforeEach;


 abstract class TaskManagerTest <T extends TaskManager> {
     protected T taskManager;

     @BeforeEach
     void setUp() {
     }
 }



