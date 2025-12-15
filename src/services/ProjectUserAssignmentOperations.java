package services;

import interfaces.IProjectService;
import models.Project;

import java.util.Arrays;

public class ProjectUserAssignmentOperations {

    private final IProjectService projectService;
    private String[][] assignments;
    private  GenerateProjectId projectIdGenerator ;

    public ProjectUserAssignmentOperations(IProjectService projectService, int initialCapacity) {
        this.projectService = projectService;
        this.assignments = new String[initialCapacity][];
        this.projectIdGenerator = new GenerateProjectId();
    }

    private void ensureCapacity(int projectIndex, int teamSize) {
        if (projectIndex >= assignments.length) {
            int newCap = Math.max(assignments.length * 2, 1);
            while (newCap <= projectIndex) newCap *= 2;
            assignments = Arrays.copyOf(assignments, newCap);
        }
        if (assignments[projectIndex] == null) {
            assignments[projectIndex] = new String[teamSize];
        }
    }

    public boolean assignUser(String projectId, String userId) {
        if (projectId == null || userId == null) return false;

        Project project = projectService.getProjectById(projectId);
        if (project == null) return false;

        int teamSize = Math.max(1, project.getTeamSize());
        ensureCapacity(projectIdGenerator.elementIndex(projectId), teamSize);

        String[] row = assignments[projectIdGenerator.elementIndex(projectId)];
        int size = 0;
        while (size < row.length && row[size] != null) size++;

        if (size >= row.length) return false; // team full
        row[size] = userId;
        return true;
    }

    public boolean removeUser(String projectId, String userId) {
        if (projectId == null || userId == null) return false;

        int projectIdIndex = projectIdGenerator.elementIndex(projectId);

        Project project = projectService.getProjectById(projectId);
        if (project == null || projectIdIndex >= assignments.length || assignments[projectIdIndex] == null) return false;

        String[] row = assignments[projectIdIndex];
        int size = 0;
        while (size < row.length && row[size] != null) size++;

        for (int i = 0; i < size; i++) {
            if (row[i].equals(userId)) {
                System.arraycopy(row, i + 1, row, i, size - i - 1);
                row[size - 1] = null;
                return true;
            }
        }
        return false;
    }

    public String[] getAssignedUsers(String projectId) {

        int index = projectIdGenerator.elementIndex(projectId);

        if (projectId.isEmpty()) return new String[0];

        Project project = projectService.getProjectById(projectId);
        if (project == null || index >= assignments.length || assignments[index] == null) return new String[0];

        String[] row = assignments[index];
        int size = 0;
        while (size < row.length && row[size] != null) size++;

        return Arrays.copyOf(row, size);
    }
}
