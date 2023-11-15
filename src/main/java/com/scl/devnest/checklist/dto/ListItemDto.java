package com.scl.devnest.checklist.dto;

import com.scl.devnest.checklist.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ListItemDto {

    private Long id;

    private String summary;

    private Status status;

    private Long checkListId;

    private Date dueDate;
}
