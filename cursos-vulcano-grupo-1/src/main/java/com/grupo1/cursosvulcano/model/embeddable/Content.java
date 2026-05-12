package com.grupo1.cursosvulcano.model.embeddable;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;


@Embeddable
@Getter
@Setter
public class Content {
    private String name;
    private String description;
}
