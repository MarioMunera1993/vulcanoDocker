package com.grupo1.cursosvulcano.dto.response;
import com.grupo1.cursosvulcano.model.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ModuleResponse {

    private Long id;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long courseId;  // ID del curso (para referencia)
    private String name;
    private String description;
    private String videoUrl;
    private Integer durationInMinutes;
    private String markdownUrl;
    private String interactiveGameUrl;
    private Integer orderIndex;
    private Status status;
}
