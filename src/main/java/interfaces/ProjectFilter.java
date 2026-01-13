package interfaces;

import models.Project;
import java.util.function.Predicate;

/**
 * Functional interfaces for project filtering and validation
 * Following functional programming principles for Week 3 enhancements
 */
@FunctionalInterface
public interface ProjectFilter extends Predicate<Project> {
    /**
     * Test if a project meets the filter criteria
     * @param project the project to test
     * @return true if project matches criteria, false otherwise
     */
    boolean test(Project project);
    
    /**
     * Static factory methods for common filters
     */
    static ProjectFilter byType(String type) {
        return project -> project != null && type.equalsIgnoreCase(project.getType());
    }
    
    static ProjectFilter byBudgetRange(double min, double max) {
        return project -> project != null && 
                      project.getBudget() >= min && 
                      project.getBudget() <= max;
    }
    
    static ProjectFilter byTeamSize(int minSize, int maxSize) {
        return project -> project != null && 
                      project.getTeamSize() >= minSize && 
                      project.getTeamSize() <= maxSize;
    }
    
    static ProjectFilter softwareProjects() {
        return byType("Software");
    }
    
    static ProjectFilter hardwareProjects() {
        return byType("Hardware");
    }
    
    static ProjectFilter budgetGreaterThan(double amount) {
        return project -> project != null && project.getBudget() > amount;
    }
    
    static ProjectFilter budgetLessThan(double amount) {
        return project -> project != null && project.getBudget() < amount;
    }
    
    /**
     * Combine multiple filters with AND logic
     */
    default ProjectFilter and(ProjectFilter other) {
        return project -> this.test(project) && other.test(project);
    }
    
    /**
     * Combine multiple filters with OR logic
     */
    default ProjectFilter or(ProjectFilter other) {
        return project -> this.test(project) || other.test(project);
    }
    
    /**
     * Negate the filter
     */
    default ProjectFilter negate() {
        return project -> !this.test(project);
    }
}
