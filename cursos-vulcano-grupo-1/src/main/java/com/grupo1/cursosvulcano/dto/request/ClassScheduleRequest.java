package com.grupo1.cursosvulcano.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ClassScheduleRequest {

    @NotNull
    private Long courseId;

    @NotNull
    private LocalDateTime startTime;

    private String notes;
}