package com.example.dao;

import com.example.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    @Query("SELECT new Task(t.heading, t.description, t.status, t.priority, t.executorName) FROM Task t")
    List<Task> findAllTasksWithoutId();
}