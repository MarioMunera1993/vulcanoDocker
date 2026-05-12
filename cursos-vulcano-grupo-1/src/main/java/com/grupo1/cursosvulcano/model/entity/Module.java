package com.grupo1.cursosvulcano.model.entity;
import com.grupo1.cursosvulcano.model.enums.Status;

import com.grupo1.cursosvulcano.model.embeddable.Content;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "modules")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Module extends BaseEntity {
     @Embedded
     private Content content; 
     private String videoUrl;
     private Integer durationInMinutes;
     private String  markdownUrl; 
     private String interactiveGameUrl;
     private Integer orderIndex;
     @Enumerated (EnumType.STRING)
     @Column(length = 30)
     private Status status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false) 
    @com.fasterxml.jackson.annotation.JsonIgnoreProperties({"modulos", "hibernateLazyInitializer", "handler"})
    private Course course;
}
