package Repository;

import interfaces.IRepository;
import models.Project;

import java.util.Arrays;

public class ProjectRepository implements IRepository<Project> {

    private Project[] projects;

    public ProjectRepository(int initialCapacity) {
        if (initialCapacity <= 0) initialCapacity = 10;
        this.projects = new Project[initialCapacity];
    }

    private void ensureCapacity(int index) {
        if (index < projects.length) return;

        int newCapacity = Math.max(projects.length * 2, 1);
        while (newCapacity <= index) newCapacity *= 2;
        projects = Arrays.copyOf(projects, newCapacity);
    }

    @Override
    public void add(Project project, int index) {
        if (project == null) throw new IllegalArgumentException("Project cannot be null");
        ensureCapacity(index);
        if (projects[index] != null)
            throw new IllegalStateException("Project already exists at index " + index);

        projects[index] = project;
    }

    @Override
    public Project getById(int index) {
        if (index < 0 || index >= projects.length) return null;
        return projects[index];
    }

    @Override
    public Project[] getAll() {
        return Arrays.copyOf(projects, projects.length); // safe copy
    }

    @Override
    public void update(int index, Project project) {
        if (index < 0) throw new IllegalArgumentException("Invalid index");
        ensureCapacity(index);
        projects[index] = project;
    }

    @Override
    public void removeById(int index) {
        if (index < 0 || index >= projects.length) return;
        projects[index] = null;
    }

    /** Query helpers */
    public Project[] findByType(String type) {
        if (type == null) return new Project[0];
        return Arrays.stream(projects)
                .filter(p -> p != null && type.equalsIgnoreCase(p.getType()))
                .toArray(Project[]::new);
    }

    public Project[] findByBudgetRange(double min, double max) {
        return Arrays.stream(projects)
                .filter(p -> p != null && p.getBudget() >= min && p.getBudget() <= max)
                .toArray(Project[]::new);
    }
}
