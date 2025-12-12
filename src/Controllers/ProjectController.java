package Controllers;

import models.HardwareProject;
import models.SoftwareProject;
import services.GenerateProjectId;
import services.ProjectService;
import utils.Printer;
import utils.ValidationUtils;

public class ProjectController {

    private final ProjectService projectService;
    private final ValidationUtils in;
    private final Printer out;
    private final GenerateProjectId idGenerator;

    public ProjectController(ProjectService projectService, ValidationUtils in, Printer out,
            GenerateProjectId idGenerator) {
        this.projectService = projectService;
        this.in = in;
        this.out = out;
        this.idGenerator = idGenerator;
    }

    public void createProjectInteractive() {
        out.printTitle("Create Project");
        int type = in.readIntInRange("1=Software 2=Hardware 3=Cancel: ", 1, 3);
        if (type == 3)
            return;

        String name = in.readNonEmptyText("Name: ");
        String desc = in.readNonEmptyText("Desc: ");
        int team = in.readIntInRange("Team size: ", 1, 100);
        double budget = in.readNonNegativeDouble("Budget: ");

        if (type == 1) {
            String lang = in.readNonEmptyText("Language: ");
            SoftwareProject sp = new SoftwareProject(name, desc, "Software", team, lang, budget);
            sp.setId(idGenerator.generate());
            projectService.addProject(sp, idGenerator.elementIndex(sp.getId()));
            out.printMessage("Software project created.");
        } else {
            String hw = in.readNonEmptyText("Hardware type: ");
            HardwareProject hp = new HardwareProject(name, desc, "Hardware", team, hw, budget);
            hp.setId(idGenerator.generate());
            projectService.addProject(hp, idGenerator.elementIndex(hp.getId()));
            out.printMessage("Hardware project created.");
        }
    }

    public void projectCatalog() {
        out.printTitle("PROJECT CATALOG ");
        out.printMessage("1. View All Projects");
        out.printMessage("2. Software Projects Only");
        out.printMessage("3. Hardware Projects Only");
        out.printMessage("4. Search by Budget Range");
        out.printMessage("5. Assign User to Project");

        int input = in.readIntInRange("Enter filter choice __", 1, 5);

        switch (input) {
            case 1:
                out.printProjectsTable(projectService.getAllProjects());
                break;
            case 2:
                // Filter by type - would need to add method to ProjectService
                out.printProjectsTable(projectService.getAllProjects());
                break;
            case 3:
                // Filter by type - would need to add method to ProjectService
                out.printProjectsTable(projectService.getAllProjects());
                break;
            case 4:
                out.printTitle("Filter by minimum and maximum budget ");
                double min = in.readNonNegativeDouble("Enter the minimum budget __");
                double max = in.readNonNegativeDouble("Enter the Maximum budget __");
                out.printMessage(String.format("Budget range entered: %.2f - %.2f (filter not implemented)", min, max));
                break;
            case 5:
                // Assign user - would need separate method
                break;
        }
    }
}
