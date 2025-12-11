package services;

import interfaces.IRepository;
import models.Project;

import java.util.Arrays;

public class ProjectUserAssignmentOperations {

    private final IRepository<Project> repository;
    private String[][] assignments;

    public ProjectUserAssignmentOperations(IRepository<Project> repository, int initialCapacity) {
        this.repository = repository;
        this.assignments = new String[initialCapacity][];
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

    public boolean assignUser(int projectId, String userId) {
        if (projectId < 0 || userId == null) return false;

        Project project = repository.getById(projectId);
        if (project == null) return false;

        int teamSize = Math.max(1, project.getTeamSize());
        ensureCapacity(projectId, teamSize);

        String[] row = assignments[projectId];
        int size = 0;
        while (size < row.length && row[size] != null) size++;

        if (size >= row.length) return false; // team full
        row[size] = userId;
        return true;
    }

    public boolean removeUser(int projectId, String userId) {
        if (projectId < 0 || userId == null) return false;

        Project project = repository.getById(projectId);
        if (project == null || projectId >= assignments.length || assignments[projectId] == null) return false;

        String[] row = assignments[projectId];
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

    public String[] getAssignedUsers(int projectId) {
        if (projectId < 0) return new String[0];

        Project project = repository.getById(projectId);
        if (project == null || projectId >= assignments.length || assignments[projectId] == null) return new String[0];

        String[] row = assignments[projectId];
        int size = 0;
        while (size < row.length && row[size] != null) size++;

        return Arrays.copyOf(row, size);
    }
}
