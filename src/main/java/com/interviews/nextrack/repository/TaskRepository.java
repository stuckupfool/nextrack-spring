package com.interviews.nextrack.repository;

import com.interviews.nextrack.todo.Task;

public interface TaskRepository extends org.springframework.data.jpa.repository.JpaRepository<Task, Long> {
    long countByCompleted(boolean completed);
}