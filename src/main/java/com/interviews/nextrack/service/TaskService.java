package com.interviews.nextrack.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.interviews.nextrack.repository.TaskRepository;
import com.interviews.nextrack.todo.Task;

@Service 
public class TaskService {
    @Autowired 
    private TaskRepository taskRepository;

    public boolean hasTasks() {
        return taskRepository.count() > 0;
    }

    public long getTaskCount() {
        return taskRepository.count();
    }

    public long getCompletedTaskCount() {
        return taskRepository.countByCompleted(true);
    }

    public Task createTask(String title, String description) {
        if (title == null || title.isEmpty()) {
            throw new IllegalArgumentException("Title cannot be null or empty");
        }
        if (description == null) {
            description = "";
        }
        return taskRepository.save(new Task(title, description));
    }

    public Task toggleTaskCompletion(Long taskId) {
        Task task = taskRepository.findById(taskId).orElse(null);
        if (task != null) {
            task.setCompleted(!task.getCompleted());
            return taskRepository.save(task);
        }
        return null;
    }

    public void deleteTask(Long taskId) {
        taskRepository.deleteById(taskId);
    }

    public Task getTask(Long taskId) {
        return taskRepository.findById(taskId).orElse(null);
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public Task saveTask(Task task) {
        return taskRepository.save(task);
    }
}
