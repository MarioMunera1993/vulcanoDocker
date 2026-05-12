package com.grupo1.cursosvulcano.dto.request;
import jakarta.validation.constraints.NotNull;
import com.grupo1.cursosvulcano.model.enums.Status;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ModuleRequest {
    @NotNull
    private Long courseId;

    @NotNull
    private String name;

    private String description;

    private String videoUrl;

    private Integer durationInMinutes;

    private String markdownUrl;

    private String interactiveGameUrl;

    private Integer orderIndex;

    @NotNull
    private Status status;

}
