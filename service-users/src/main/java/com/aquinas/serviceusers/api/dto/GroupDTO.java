package com.aquinas.serviceusers.api.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public final class GroupDTO {
    private long id;
    private String title;
    private String description;
    private long owner_id;
}
