
import models.SoftwareProject;
import models.HardwareProject;
import services.ProjectService;
import services.ReportService;
import services.TaskService;
import services.UserService;
import utils.ConsoleMenu;

public class Main {
    public static void main(String[] args) {
        ProjectService projectService = new ProjectService();
        TaskService taskService = new TaskService();
        ReportService reportService = new ReportService(projectService, taskService);
        UserService userService = new UserService();

        ConsoleMenu.setProjectService(projectService);

        ConsoleMenu.setTaskService(taskService);

        ConsoleMenu.setReportService(reportService);

        ConsoleMenu.setUserService(userService);
        //
        SoftwareProject sp1 = new SoftwareProject("P001", "AI System", "Build an AI model", "Software", 5, "Java",
                1500);
        HardwareProject hp1 = new HardwareProject("P002", "IoT Device", "Create a smart sensor", "Hardware", 3,
                "Microcontroller", 1500);

        projectService.addProject(sp1);
        projectService.addProject(hp1);

        // // Display all projects
        // ConsoleMenu.displayProjects(service.getAllProjects());
        //
        // // Display only Software projects
        // ConsoleMenu.displayProjects(service.getAllProjects(), "Software");

        // ConsoleMenu.projectCatalog();

        ConsoleMenu.addingNewTaskMenu();
        ConsoleMenu.printProjectStatusReport();

        // Display detailed info for one project

    }
}
