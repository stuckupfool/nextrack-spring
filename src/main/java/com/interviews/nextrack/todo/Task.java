package com.interviews.nextrack.todo;

import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Task {
    private @Id @GeneratedValue Long id;

    private String title;
    private String description;
    private boolean completed;
    private int rank;

    public Task() {}
    public Task(String title, String description) {
        this.title = title;
        this.description = description;
        this.completed = false;
        this.rank = 0;
    }
    public Task(String title, String description, boolean completed, int rank) {
        this.title = title;
        this.description = description;
        this.completed = completed;
        this.rank = rank;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public boolean getCompleted() {
        return completed;
    }

    public int getRank() {
        return rank;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        
        Task task = (Task) o;
        return (id != null && id.equals(task.id)) && 
            (title != null && title.equals(task.title));
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title);
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", completed=" + completed +
                ", rank=" + rank +
                '}';
    }
}
