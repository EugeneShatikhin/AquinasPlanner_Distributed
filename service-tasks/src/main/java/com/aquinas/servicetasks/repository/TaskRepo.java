package com.aquinas.servicetasks.repository;

import com.aquinas.servicetasks.repository.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepo extends JpaRepository<Task, Long> {
    Task findByTitle(String title);
}
