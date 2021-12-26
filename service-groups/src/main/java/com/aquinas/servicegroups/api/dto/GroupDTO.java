package com.aquinas.servicegroups.api.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public final class GroupDTO {
    private String title;
    private String desc;
    private long owner_id;
}
