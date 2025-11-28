package services;

import models.Task;
import utils.ConsoleMenu;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TaskService {

    //A repository to store Tasks
    private List<Task> Tasks ;

    //Constructor to initialize the Tasks repo
    public  TaskService(){
        Tasks = new ArrayList<>();
    }

    //A method to add new Task it first check if the task id does not exist
    public void addTask(Task task) {
        boolean exists = Tasks.stream()
                .anyMatch(t -> t.getTaskId().equals(task.getTaskId()));

        if (!exists) {
            Tasks.add(task);
        }
    }

    //A method to get all the tasks
    public List<Task> getAllTasks() {
        return Tasks;
    }

    //A method to get a task by task id
    public Task getTaskById(String taskId) {
        return Tasks.stream().filter(t -> t.getTaskId().equals(taskId)).findFirst().orElse(null);
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
        Tasks.removeIf(t -> t.getTaskId().equals(taskId));
    }


    //a method to get all the tasks by project id
    public List<Task> getTasksByProjectId(String projectId) {

        return Tasks.stream().filter(t -> t.getProjectId().equals(projectId)).collect(Collectors.toList());

    }

    //calculate completion rate sum the all the completed tasks and divide it by the total number of tasks
    public double calculateCompletionRate(String projectId) {
        List<Task>  tasks=  Tasks.stream().filter(t -> t.getProjectId().equals(projectId)).collect(Collectors.toList());
        long completedTasks = tasks.stream().filter(t -> t.getTaskStatus().equals("Completed")).count();
        return (double) completedTasks / tasks.size();
    }

    
}
