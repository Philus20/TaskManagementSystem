package Controllers;

import models.HardwareProject;
import models.SoftwareProject;
import services.ProjectService;
import utils.Printer;
import utils.ValidationUtils;

public class ProjectController {

    private final ProjectService projectService;
    private final ValidationUtils in;
    private final Printer out;

    public ProjectController(ProjectService projectService, ValidationUtils in, Printer out) {
        this.projectService = projectService;
        this.in = in;
        this.out = out;
    }

    public void createProjectInteractive() {
        out.printTitle("Create Project");
        int type = in.readIntInRange("1=Software 2=Hardware 3=Cancel: ", 1, 3);
        if (type == 3) return;
        String name = in.readNonEmptyText("Name: ");
        String desc = in.readNonEmptyText("Desc: ");
        int team = in.readIntInRange("Team size: ", 1, 100);
        double budget = in.readNonNegativeDouble("Budget: ");
        if (type == 1) {
            String lang = in.readNonEmptyText("Language: ");
            SoftwareProject sp = new SoftwareProject(name, desc, "Software", team, lang, budget);
            projectService.addProject(sp);
            out.printMessage("Software project created.");
        } else {
            String hw = in.readNonEmptyText("Hardware type: ");
            HardwareProject hp = new HardwareProject(name, desc, "Hardware", team, hw, budget);
            projectService.addProject(hp);
            out.printMessage("Hardware project created.");
        }
    }

}
