
import models.SoftwareProject;
import models.Task;
import models.HardwareProject;
import services.ProjectService;
import services.ReportService;
import services.TaskService;
import services.UserService;
import utils.ConsoleMenu;

public class Main {
    public static void main(String[] args) {
        ProjectService projectService = new ProjectService(20);
        TaskService taskService = new TaskService();
        UserService userService = new UserService();

        ConsoleMenu.setProjectService(projectService);

        ConsoleMenu.setTaskService(taskService);


        ConsoleMenu.setUserService(userService);
        //
        SoftwareProject sp1 = new SoftwareProject( "AI System", "Build an AI model", "Software", 5, "Java",
                1500);
        HardwareProject hp1 = new HardwareProject( "IoT Device", "Create a smart sensor", "Hardware", 3,
                "Microcontroller", 1500);
Task t1 = new Task("Task 1",  "Completed", "P0000");
Task t2 = new Task("Task 2", "Pending", "P0001");

        projectService.addProject(sp1);
        projectService.addProject(hp1);

        taskService.addTask(t1);
        taskService.addTask(t2);

        // Start with initial login menu
        ConsoleMenu.initialLoginMenu();
       

        // Display detailed info for one project



    }
}
