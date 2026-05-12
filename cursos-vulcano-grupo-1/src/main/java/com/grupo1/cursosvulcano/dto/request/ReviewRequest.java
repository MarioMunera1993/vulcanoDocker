package com.grupo1.cursosvulcano.dto.request;

import com.grupo1.cursosvulcano.model.enums.Experience;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ReviewRequest {

    @NotNull
    private Long userId;

    @NotNull
    private Long moduleId;

    @NotNull
    private String comment;

    @NotNull
    @Min(1)
    @Max(5)
    private int rating;

    @NotNull
    private Experience experience;
}
