package com.grupo1.cursosvulcano.dto.response;

import com.grupo1.cursosvulcano.model.enums.Experience;
import lombok.Data;

@Data
public class ReviewResponse {

    private Long id;
    private Long userId;
    private Long moduleId;
    private String comment;
    private int rating;
    private Experience experience;
}
