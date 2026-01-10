package Repository;

import interfaces.IRepository;
import models.Project;
import models.User;
import utils.exceptions.*;

import java.util.*;

public class ProjectRepository implements IRepository<Project> {

    private Map<String,Project> projects ;

    public ProjectRepository() {
        this.projects = new HashMap<>();
    }



    @Override
    public void add(Project project, String key) {
        if (this.projects.isEmpty()) throw new EmptyProjectException("Project cannot be null");

        if (projects.containsKey(key))
            throw new ProjectAlreadyExistException("Project already exists for this Name " + key);

        projects.put(key, project);
//        System.out.println(project.getType());
    }

    @Override
    public Project getById(String key) {
        if (key == null || key.isBlank()) {
            throw new IllegalArgumentException("Project ID cannot be null or empty");
        }

        Project project = projects.get(key);
        if (project == null) {
            throw new EmptyProjectException("No Project Found for this Id");
        }

        return project;
    }



    @Override
    public List<Project> getAll() {
        if (projects == null || projects.isEmpty()) {
            throw new EmptyProjectException("No Projects Found");
        }
        // Return a snapshot so callers can't mutate the backing map via the view
        return new ArrayList<>(projects.values());
    }






    @Override
    public void update(String index, Project project) {
        Project temp = getById(index);
        projects.put(project.getId(),project) ;
    }

    @Override
    public void removeById(String Id) {
        Project temp = getById(Id);

        projects.remove(temp.getId()) ;
    }

    /** Query helpers */
    public List<Project> findByType(String type) {
        if (type == null) throw new EntityAttributeException(type);
      List<Project> projects = getAll();
        return projects.stream()
                .filter(p -> p != null && type.equalsIgnoreCase(p.getType()))
                .toList();
    }

    public List<Project> findByBudgetRange(double min, double max) {
      List<Project> projects = getAll();
        return projects.stream()
                .filter(p -> p != null && p.getBudget() >= min && p.getBudget() <= max)
                .toList();
    }
}
