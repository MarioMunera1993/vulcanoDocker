package com.grupo1.cursosvulcano.dto.response;

import com.grupo1.cursosvulcano.model.enums.CourseLevel;
import com.grupo1.cursosvulcano.model.enums.Status;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CourseResponseDTO {
    
    private Long id;
    private String name;
    private String description;
    private String imageUrl;
    private CourseLevel courseLevel;
    private Boolean isPublished;
    private Status status;
}
