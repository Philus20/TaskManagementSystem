package Controllers;

import interfaces.INavigation;
import interfaces.IReporting;
import utils.Printer;
import utils.ValidationUtils;

/**
 * ReportController following Single Responsibility Principle (SRP)
 * - Only responsible for report-related user interactions
 */
public class ReportController {
    private final IReporting reportService;
    private final Printer out;
    private final INavigation navigation;
    private final ValidationUtils validationUtils;

    public ReportController(IReporting reportService, Printer out, INavigation navigation,
                            ValidationUtils validationUtils) {
        this.reportService = reportService;
        this.out = out;
        this.navigation = navigation;
        this.validationUtils = validationUtils;
    }

    /**
     * Generate and display project status report
     */
    public void generateProjectStatusReport() {
        out.printTitle("PROJECT STATUS REPORT");
        reportService.generateReport();
        out.printMessage("");
        validationUtils.readNonEmptyText("Press Enter to return to main menu: ");
        navigation.showMainMenu();
    }

}
