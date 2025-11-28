package services;

import models.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TaskService {

    //A repository to store Tasks
    private final List<Task> tasks;

    //Constructor to initialize the Tasks repo
    public  TaskService(){
        tasks = new ArrayList<>();
    }

    //A method to add new Task it first check if the task id does not exist
    public void addTask(Task task) {
        boolean exists = tasks.stream()
                .anyMatch(t -> t.getTaskId().equals(task.getTaskId()));

        if (!exists) {
            tasks.add(task);
        }
    }

    //A method to get all the tasks
    public List<Task> getAllTasks() {
        return tasks;
    }

    //A method to get a task by task id
    public Task getTaskById(String taskId) {
        return tasks.stream().filter(t -> t.getTaskId().equals(taskId)).findFirst().orElse(null);
    }



    //a method to update a task status
    public Task updateTaskStatus(String taskId, String taskStatus) {
        Task task = getTaskById(taskId);
        if (task != null) {
            task.setTaskStatus(taskStatus);

        }
        return  task;
    }

    //a method to delete a task
    public void deleteTask(String taskId) {
        tasks.removeIf(t -> t.getTaskId().equals(taskId));
    }


    //a method to get all the tasks by project id
    public List<Task> getTasksByProjectId(String projectId) {

        return tasks.stream().filter(t -> t.getProjectId().equals(projectId)).collect(Collectors.toList());

    }

    //calculate completion rate sum the all the completed tasks and divide it by the total number of tasks
    public double calculateCompletionRate(String projectId) {
        List<Task>  tasks=  this.tasks.stream().filter(t -> t.getProjectId().equals(projectId)).collect(Collectors.toList());
        long completedTasks = tasks.stream().filter(t -> t.getTaskStatus().equals("Completed")).count();
        return (double) completedTasks / tasks.size() * 100;
    }



    
}
