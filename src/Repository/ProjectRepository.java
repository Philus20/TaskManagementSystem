package Repository;

import interfaces.IRepository;
import models.Project;
import utils.exceptions.*;

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
        if (project == null) throw new EmptyProjectException("Project cannot be null");
        ensureCapacity(index);
        if (projects[index] != null)
            throw new ProjectAlreadyExistException("Project already exists at index " + index);

        projects[index] = project;
        System.out.println(project.getType());
    }

    @Override
    public Project getById(int index) {
        if (index < 0 ) throw new IndexIsLessThanZero("Index cannot be less than zero");
        if ( index >= projects.length) throw new IndexGreatherThanArrayLengthException("The Index is higher than the array Length ");
        if(projects[index]==null)
            throw new EmptyProjectException("No Project Found for this Id");
        return projects[index];
    }

    @Override
    public Project[] getAll() {

        boolean found = false;

        for (Project p : projects) {
            if (p != null) {
                found = true;
                break;
            }
        }

        if (!found) {
            throw new EmptyProjectException();
        }

        return Arrays.copyOf(projects, projects.length);
    }



    @Override
    public void update(int index, Project project) {
        if (index < 0) throw new IllegalArgumentException("Invalid index");
        ensureCapacity(index);
        Project temp = getById(index);
        projects[index] = project;
    }

    @Override
    public void removeById(int index) {
        if (index < 0 || index >= projects.length) throw new IndexIsLessThanZero("Invalid index");
        Project temp = getById(index);

        projects[index] = null;
    }

    /** Query helpers */
    public Project[] findByType(String type) {
        if (type == null) throw new EntityAttributeException(type);
        Project[] projects = getAll();
        return Arrays.stream(projects)
                .filter(p -> p != null && type.equalsIgnoreCase(p.getType()))
                .toArray(Project[]::new);
    }

    public Project[] findByBudgetRange(double min, double max) {
        Project[] projects = getAll();
        return Arrays.stream(projects)
                .filter(p -> p != null && p.getBudget() >= min && p.getBudget() <= max)
                .toArray(Project[]::new);
    }
}
