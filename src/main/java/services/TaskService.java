//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package services;

import Repository.TaskRepository;
import interfaces.ITaskService;
import interfaces.IdGenerator;
import interfaces.TaskFilter;
import interfaces.IStreamService;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import models.Task;
import utils.exceptions.EmptyProjectException;
import utils.exceptions.TaskNotFoundException;

public class TaskService implements ITaskService {
    private final TaskRepository taskRepository;
    private final IdGenerator taskIdGenerator;
    private final FilePersistenceService filePersistenceService;
    private ConcurrentTaskUpdateService concurrentUpdateService;
    private IStreamService streamService;

    public TaskService(TaskRepository var1, IdGenerator var2) {
        if (var1 == null) {
            throw new IllegalArgumentException("TaskRepository cannot be null");
        } else if (var2 == null) {
            throw new IllegalArgumentException("TaskIdGenerator cannot be null");
        } else {
            this.taskRepository = var1;
            this.taskIdGenerator = var2;
            this.filePersistenceService = new FilePersistenceService();
            this.concurrentUpdateService = null;
            this.streamService = null; // Will be injected later
        }
    }

    public void setConcurrentUpdateService(ConcurrentTaskUpdateService var1) {
        this.concurrentUpdateService = var1;
    }

    public void setStreamService(IStreamService streamService) {
        this.streamService = streamService;
    }

    public CompletableFuture<List<Task>> addTasksConcurrently(List<Task> var1) {
        if (streamService != null) {
            return streamService.streamTasksConcurrently(task -> true)
                    .thenCompose(existingTasks -> {
                        // Add new tasks and return updated list
                        return CompletableFuture.completedFuture(addTasksSequentially(var1));
                    });
        }
        return this.concurrentUpdateService == null ? CompletableFuture.completedFuture(this.addTasksSequentially(var1)) : this.concurrentUpdateService.addTasksConcurrently(var1);
    }

    public CompletableFuture<List<Task>> updateTasksConcurrently(List<String> var1, String var2) {
        if (streamService != null) {
            return streamService.batchUpdateTasksConcurrently(var1, var2);
        }
        return this.concurrentUpdateService == null ? CompletableFuture.completedFuture(this.updateTasksSequentially(var1, var2)) : this.concurrentUpdateService.updateTasksConcurrently(var1, var2);
    }

    private List<Task> addTasksSequentially(List<Task> var1) {
        ArrayList var2 = new ArrayList();

        for(Task var4 : var1) {
            try {
                this.addTask(var4);
                var2.add(var4);
            } catch (Exception var6) {
                System.err.println("Error adding task: " + var6.getMessage());
            }
        }

        return var2;
    }

    private List<Task> updateTasksSequentially(List<String> var1, String var2) {
        ArrayList var3 = new ArrayList();

        for(String var5 : var1) {
            try {
                Task var6 = this.updateTaskStatus(var5, var2);
                if (var6 != null) {
                    var3.add(var6);
                }
            } catch (Exception var7) {
                System.err.println("Error updating task " + var5 + ": " + var7.getMessage());
            }
        }

        return var3;
    }

    public void loadTasksFromFile() {
        try {
            List<Task> var1 = this.filePersistenceService.loadTasks();
            
            // Update the ID generator counter based on existing tasks
            if (!var1.isEmpty()) {
                List<String> existingTaskIds = var1.stream()
                    .filter(task -> task != null && task.getTaskId() != null)
                    .map(Task::getTaskId)
                    .toList();
                GenerateTaskId.updateCounterFromExistingIds(existingTaskIds);
            }

            for(Task var3 : var1) {
                if (var3 != null && var3.getTaskId() != null) {
                    this.taskRepository.add(var3, var3.getTaskId());
                }
            }

            System.out.println("Loaded " + var1.size() + " tasks from file.");
        } catch (Exception var4) {
            System.out.println("Starting with empty task list: " + var4.getMessage());
        }

    }

    public void addTask(Task var1) {
        if (var1 == null) {
            throw new IllegalArgumentException("Task cannot be null");
        } else if (!ValidationService.isValidTaskStatus(var1.getTaskStatus())) {
            throw new IllegalArgumentException("Invalid task status: " + var1.getTaskStatus() + ". Valid statuses: Pending, In Progress, Completed");
        } else if (var1.getProjectId() != null && !ValidationService.isValidProjectId(var1.getProjectId())) {
            throw new IllegalArgumentException("Invalid project ID format: " + var1.getProjectId() + ". Expected format: P001, P002, etc.");
        } else {
            Object var2 = null;

            try {
                if (var1.getTaskId() != null && !var1.getTaskId().isEmpty()) {
                    if (!ValidationService.isValidTaskId(var1.getTaskId())) {
                        throw new IllegalArgumentException("Invalid task ID format: " + var1.getTaskId() + ". Expected format: T001, T002, etc.");
                    }
                } else {
                    String var11 = this.taskIdGenerator.generate();
                    var1.setTaskId(var11);
                }

                Task var3 = this.taskRepository.findByTaskId(var1.getTaskId());
                if (var3 != null) {
                    throw new IllegalStateException("Task with id " + var1.getTaskId() + " already exists.");
                } else {
                    this.taskRepository.add(var1, var1.getTaskId());
                    this.saveTasksToFile();
                }
            } catch (TaskNotFoundException var8) {
                throw new IllegalStateException("Failed to add task: " + var8.getMessage(), var8);
            } catch (Exception var9) {
                throw new IllegalStateException("Unexpected error while adding task: " + var9.getMessage(), var9);
            } finally {
                ;
            }
        }
    }

    public List<Task> getAllTasks() {
        return this.taskRepository.getAll();
    }

    public Task getTaskById(String var1) {
        return var1 == null ? null : this.taskRepository.findByTaskId(var1);
    }

    public Task updateTaskStatus(String var1, String var2) {
        if (var1 == null) {
            return null;
        } else if (!ValidationService.isValidTaskId(var1)) {
            throw new IllegalArgumentException("Invalid task ID format: " + var1 + ". Expected format: T001, T002, etc.");
        } else if (!ValidationService.isValidTaskStatus(var2)) {
            throw new IllegalArgumentException("Invalid task status: " + var2 + ". Valid statuses: Pending, In Progress, Completed");
        } else {
            Object var3 = null;

            try {
                Task var15 = this.getTaskById(var1);
                if (var15 != null) {
                    if ("Completed".equals(var2)) {
                        this.taskRepository.markAsComplete(var15);
                    } else {
                        var15.setTaskStatus(var2);
                    }

                    this.taskRepository.update(var1, var15);
                    this.saveTasksToFile();
                }

                return var15;
            } catch (TaskNotFoundException var11) {
                Object var16 = null;
                return (Task)var16;
            } catch (IllegalArgumentException var12) {
                Object var5 = null;
                return (Task)var5;
            } catch (Exception var13) {
                throw new IllegalStateException("Unexpected error while updating task status: " + var13.getMessage(), var13);
            } finally {
                ;
            }
        }
    }

    public void deleteTask(String var1) {
        if (var1 != null) {
            if (!ValidationService.isValidTaskId(var1)) {
                throw new IllegalArgumentException("Invalid task ID format: " + var1 + ". Expected format: T001, T002, etc.");
            } else {
                try {
                    try {
                        Task var2 = this.getTaskById(var1);
                        if (var2 != null) {
                            this.taskRepository.removeById(var1);
                            this.saveTasksToFile();
                        }
                    } catch (TaskNotFoundException var8) {
                    } catch (IllegalArgumentException var9) {
                    } catch (Exception var10) {
                        throw new IllegalStateException("Unexpected error while deleting task: " + var10.getMessage(), var10);
                    }

                } finally {
                    ;
                }
            }
        }
    }

    public List<Task> getTasksByProjectId(String var1) {
        if (var1 == null) {
            return new ArrayList();
        } else {
            try {
                return this.taskRepository.findByProjectId(var1);
            } catch (EmptyProjectException var3) {
                return new ArrayList();
            } catch (Exception var4) {
                throw new IllegalStateException("Unexpected error while retrieving tasks by project ID: " + var4.getMessage(), var4);
            }
        }
    }

    public double calculateCompletionRate(String var1) {
        if (streamService != null) {
            try {
                return streamService.calculateProjectCompletionRateConcurrently(var1).get(5, TimeUnit.SECONDS);
            } catch (Exception e) {
                System.err.println("StreamService calculation failed, falling back to traditional method: " + e.getMessage());
            }
        }
        
        try {
            List<Task> var2 = this.getTasksByProjectId(var1);
            if (var2.isEmpty()) {
                return (double)0.0F;
            } else {
                long var3 = var2.stream().filter((var0) -> var0 != null && "Completed".equalsIgnoreCase(var0.getTaskStatus())).count();
                return var2.isEmpty() ? (double)0.0F : (double)var3 * (double)100.0F / (double)var2.size();
            }
        } catch (ArithmeticException var5) {
            return (double)0.0F;
        } catch (Exception var6) {
            throw new IllegalStateException("Unexpected error while calculating completion rate: " + var6.getMessage());
        }
    }

    public List<Task> filterTasks(TaskFilter var1) {
        return (List)this.taskRepository.getAll().stream().filter(var1).collect(Collectors.toList());
    }

    public long getTaskCountByStatus(String var1) {
        return this.taskRepository.getAll().stream().filter((var1x) -> var1x != null && var1.equalsIgnoreCase(var1x.getTaskStatus())).count();
    }

    public List<String> getProjectIdsWithTasks() {
        return (List)this.taskRepository.getAll().stream().filter((var0) -> var0 != null && var0.getProjectId() != null).map(Task::getProjectId).distinct().collect(Collectors.toList());
    }

    public synchronized void saveTasksToFile() {
        try {
            this.filePersistenceService.saveTasks(this.taskRepository.getAll());
        } catch (Exception var2) {
            System.err.println("Failed to save tasks: " + var2.getMessage());
        }

    }

    public List<Task> getTasksByAssignedUserId(String var1) {
        return (List<Task>)(var1 == null ? new ArrayList() : this.taskRepository.findByAssignedUserId(var1));
    }
}
