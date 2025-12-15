package Controllers;


import interfaces.INavigation;
import interfaces.ITaskService;
import interfaces.IUserService;
import models.HardwareProject;
import models.Project;
import models.SoftwareProject;
import models.User;
import services.GenerateProjectId;
import services.ProjectService;
import services.ProjectUserAssignmentOperations;
import utils.Printer;
import utils.ValidationUtils;

public class ProjectController {

    private final ProjectService projectService;
    private final ValidationUtils in;
    private final Printer out;
    private final GenerateProjectId idGenerator;
    private final IUserService userService;
    private final ITaskService taskservice;
    ProjectUserAssignmentOperations projectUserAssignmentOperations;
    private final INavigation navigation;

    public ProjectController(ProjectService projectService, ValidationUtils in, Printer out,
            GenerateProjectId idGenerator, IUserService userService, ITaskService taskservice,
            ProjectUserAssignmentOperations projectAssignment, INavigation navigation) {
        this.userService = userService;
        this.projectService = projectService;
        this.in = in;
        this.out = out;
        this.taskservice = taskservice;
        this.idGenerator = idGenerator;
        this.projectUserAssignmentOperations = projectAssignment;
        this.navigation = navigation;
    }

    public void createProjectInteractive() {
        out.printTitle("Create Project");
        int type = in.readIntInRange("1=Software %n 2=Hardware %n 3=Cancel: ", 1, 3);
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
            projectService.addProject(sp);
            out.printMessage("Software project created.");
        } else {
            String hw = in.readNonEmptyText("Hardware type: ");
            HardwareProject hp = new HardwareProject(name, desc, "Hardware", team, hw, budget);
            hp.setId(idGenerator.generate());
            projectService.addProject(hp);
            out.printMessage("Hardware project created.");
        }
    }

    public void projectCatalog() {
        boolean continueMenu = true;
        while (continueMenu) {
            out.printTitle("PROJECT CATALOG ");
            out.printMessage("1. View All Projects");
            out.printMessage("2. Software Projects Only");
            out.printMessage("3. Hardware Projects Only");
            out.printMessage("4. Search by Budget Range");
            out.printMessage("5. Assign User to Project");
            out.printMessage("6. Go to Main Menu");

            int input = in.readIntInRange("Enter filter choice __", 1, 6);

            switch (input) {
                case 1:
                    try {
                        if (printAllProjects()) {
                            continueMenu = false;
                            navigation.showMainMenu();
                            return;
                        }
                    } catch (Exception e) {
                        out.printMessage(e.getMessage());
                    }
                    break;
                case 2:
                    try {
                        out.printProjectsTable(projectService.filterByType("Software"));
                        if (enterProjectId()) {
                            continueMenu = false;
                            navigation.showMainMenu();
                            return;
                        }
                    } catch (Exception e) {
                        out.printMessage(e.getMessage());
                    }
                    break;
                case 3:

                    try {
                        // Filter by type - would need to add method to ProjectService
                        out.printProjectsTable(projectService.filterByType("Hardware"));
                        if (enterProjectId()) {
                            continueMenu = false;
                            navigation.showMainMenu();
                            return;
                        }
                    } catch (Exception ex) {
                        out.printMessage(ex.getMessage());
                    }
                    break;
                case 4:
                    out.printTitle("Filter by minimum and maximum budget ");
                    double min = in.readNonNegativeDouble("Enter the minimum budget __");
                    double max = in.readNonNegativeDouble("Enter the Maximum budget __");
                    try {
                        out.printProjectsTable(projectService.findByBudgetRange(min, max));
                        if (enterProjectId()) {
                            continueMenu = false;
                            navigation.showMainMenu();
                            return;
                        }
                    } catch (Exception ex) {
                        out.printMessage(ex.getMessage());
                    }
                    break;
                case 5:
                    // Assign user - would need separate method
                    setProjectUserAssignmentOperations();
                    break;
                case 6:
                    continueMenu = false;
                    return; // Return to main menu (which will be shown by MenuRouter)
            }
        }
    }

    public boolean printAllProjects() {
        try {
            out.printProjectsTable(projectService.getAllProjects());
            return enterProjectId();
        } catch (Exception e) {
            out.printMessage(e.getMessage());
            return false;
        }
    }

    /**
     * Enter project ID to view details
     * 
     * @return true if user wants to go to main menu, false to stay in catalog
     */
    private boolean enterProjectId() {
        String projectId = in.readExistingProjectId("Enter project id to view details (or 0 to return to catalog): ");
        if (projectId.equals("0")) {
            return false; // Return to catalog menu
        } else {
            out.displayProjectDetails(projectId, projectService, taskservice, userService,
                    projectUserAssignmentOperations);
            out.printMessage("");
            String choice = in.readNonEmptyText("Press Enter to return to catalog (or 'm' for main menu): ");
            return "m".equalsIgnoreCase(choice.trim()); // Return true if user wants main menu
        }
    }

    public void setProjectUserAssignmentOperations() {
        out.printTitle("ASSIGN USER TO PROJECT");
        String projectId = in.readExistingProjectId("Enter Project ID (or 0 to cancel): ");
        if ("0".equals(projectId)) {
            out.printMessage("Cancelled.");
            return; // Return to catalog menu
        } else {
            User[] users = userService.getAllUsers();
            if (users == null || users.length == 0) {
                out.printMessage("No users available. Please create a user first.");
                return; // Return to catalog menu
            } else {
                out.printUsersTable(users);
                String userId = in.readExistingUserId("Enter User ID to assign (or 0 to cancel): ");
                if ("0".equals(userId)) {
                    out.printMessage("Cancelled.");
                    return; // Return to catalog menu
                } else {
                    boolean success = projectUserAssignmentOperations.assignUser(projectId, userId);
                    if (success) {
                        User user = userService.getUserById(userId);
                        Project project = projectService.getProjectById(projectId);
                        out.printMessage(String.format("User %s (%s) successfully assigned to project %s (%s)",
                                user.getName(), user.getId(), project.getName(), project.getId()));
                    } else {
                        out.printMessage("Failed to assign user to project.");
                    }
                    out.printMessage("");
                }
            }
        }
    }
}
