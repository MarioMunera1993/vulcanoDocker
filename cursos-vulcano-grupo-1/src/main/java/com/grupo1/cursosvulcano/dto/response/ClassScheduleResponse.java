package com.grupo1.cursosvulcano.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ClassScheduleResponse {

    private Long id;
    private Long studentId;
    private Long courseId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String status;
    private String notes;
}