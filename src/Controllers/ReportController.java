package Controllers;

import interfaces.IReporting;
import utils.Printer;

/**
 * ReportController following Single Responsibility Principle (SRP)
 * - Only responsible for report-related user interactions
 */
public class ReportController {
    private final IReporting reportService;
    private final Printer out;

    public ReportController(IReporting reportService, Printer out) {
        this.reportService = reportService;
        this.out = out;
    }

    /**
     * Generate and display project status report
     */
    public void generateProjectStatusReport() {
        out.printTitle("PROJECT STATUS REPORT");
        reportService.generateReport();
    }
}

