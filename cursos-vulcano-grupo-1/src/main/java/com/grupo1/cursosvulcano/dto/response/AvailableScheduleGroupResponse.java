package com.grupo1.cursosvulcano.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AvailableScheduleGroupResponse {

    @JsonProperty("date")
    private String date;
    
    @JsonProperty("times")
    private List<String> times;
}