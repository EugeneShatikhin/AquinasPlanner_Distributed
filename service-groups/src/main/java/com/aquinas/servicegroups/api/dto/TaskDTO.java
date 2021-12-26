package com.aquinas.servicegroups.api.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
public class TaskDTO {
    private long id;
    private String title;
    private String description;
    private Timestamp created;
    private Timestamp deadline;
    private long group_id;
    private TaskType status;
}
