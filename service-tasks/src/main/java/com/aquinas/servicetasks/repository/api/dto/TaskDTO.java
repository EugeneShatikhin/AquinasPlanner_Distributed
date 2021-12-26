package com.aquinas.servicetasks.repository.api.dto;

import com.aquinas.servicetasks.repository.model.TaskType;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
public class TaskDTO {
    private String title;
    private String description;
    private Timestamp deadline;
    private long group_id;
    private TaskType status;
}
