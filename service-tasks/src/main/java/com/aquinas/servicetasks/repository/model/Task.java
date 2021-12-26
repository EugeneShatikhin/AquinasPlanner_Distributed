package com.aquinas.servicetasks.repository.model;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "tasks")
public final class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column(unique = true)
    private String title;
    private String description;
    private Timestamp created;
    private Timestamp deadline;
    private long group_id;
    private TaskType status;

    public Task() {
    }

    public Task(String title, String description, Timestamp created, Timestamp deadline, long group_id, TaskType status) {
        this.title = title;
        this.description = description;
        this.created = created;
        this.deadline = deadline;
        this.group_id = group_id;
        this.status = status;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Timestamp getCreated() {
        return created;
    }

    public void setCreated(Timestamp created) {
        this.created = created;
    }

    public Timestamp getDeadline() {
        return deadline;
    }

    public void setDeadline(Timestamp deadline) {
        this.deadline = deadline;
    }

    public long getGroup_id() {
        return group_id;
    }

    public void setGroup_id(long group_id) {
        this.group_id = group_id;
    }

    public TaskType getStatus() {
        return status;
    }

    public void setStatus(TaskType status) {
        this.status = status;
    }
}
