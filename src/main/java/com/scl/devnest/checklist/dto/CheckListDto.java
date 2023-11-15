package com.scl.devnest.checklist.dto;

import com.scl.devnest.checklist.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CheckListDto {

    private Long id;

    private String name;

    private String description;

    private Status status;
}
