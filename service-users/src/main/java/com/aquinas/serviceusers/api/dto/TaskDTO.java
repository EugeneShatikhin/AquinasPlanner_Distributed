package com.aquinas.serviceusers.api.dto;

import com.aquinas.serviceusers.repository.model.TaskType;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
public class TaskDTO {
    private String title;
    private String description;
    private Timestamp deadline;
    private TaskType status;
}