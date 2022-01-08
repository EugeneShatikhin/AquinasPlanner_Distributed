package com.aquinas.serviceusers.api.dto;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class UserTasksDTO {
    private String title;
    private String desc;
    private long owner_id;
    private List<TaskDTO> tasks;

    public UserTasksDTO(String title, String desc, long owner_id, List<TaskDTO> tasks) {
        this.title = title;
        this.desc = desc;
        this.owner_id = owner_id;
        this.tasks = tasks;
    }
}
