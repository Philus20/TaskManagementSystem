
import models.SoftwareProject;
import models.HardwareProject;
import services.ProjectService;
import utils.ConsoleMenu;

public class Main {
    public static void main(String[] args) {
      ProjectService service = new ProjectService();
      ConsoleMenu.setProjectService(service);
//
        SoftwareProject sp1 = new SoftwareProject(1, "AI System", "Build an AI model", "Software", 5, "Java",1500);
        HardwareProject hp1 = new HardwareProject(2, "IoT Device", "Create a smart sensor", "Hardware", 3, "Microcontroller",1500);

        service.addProject(sp1);
        service.addProject(hp1);

//        // Display all projects
//        ConsoleMenu.displayProjects(service.getAllProjects());
//
//        // Display only Software projects
//        ConsoleMenu.displayProjects(service.getAllProjects(), "Software");

        ConsoleMenu.projectCatalog();

        // Display detailed info for one project

    }
}
