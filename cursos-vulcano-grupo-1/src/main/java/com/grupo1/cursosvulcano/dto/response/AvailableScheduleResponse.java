package com.grupo1.cursosvulcano.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AvailableScheduleResponse {

    private Long expertId;
    private LocalDateTime availableTime;
    private boolean isAvailable;
}