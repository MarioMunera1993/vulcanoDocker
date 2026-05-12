package com.grupo1.cursosvulcano.dto.request;

import com.grupo1.cursosvulcano.model.enums.CourseLevel;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CourseRequestDTO {

    @NotNull(message = "Debes enviar tu ID de usuario para validar permisos")
    private Long creatorUserId;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 50, message = "El nombre no puede tener más de 50 caracteres")
    private String name;

    @NotBlank(message = "La descripción es obligatoria")
    private String description;

    @Size(max = 500)
    private String imageUrl;

    private CourseLevel courseLevel;

    private Boolean isPublished;
    
    private com.grupo1.cursosvulcano.model.enums.Status status;
}
