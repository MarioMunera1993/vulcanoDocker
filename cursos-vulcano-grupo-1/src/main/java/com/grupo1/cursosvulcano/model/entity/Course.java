package com.grupo1.cursosvulcano.model.entity;

import com.grupo1.cursosvulcano.model.enums.Status;
import java.util.ArrayList;
import java.util.List;
import com.grupo1.cursosvulcano.model.enums.CourseLevel;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Getter
@Setter
@Table (name = "courses")
@NoArgsConstructor
@AllArgsConstructor

public class Course extends BaseEntity {

    @Column (nullable = false, length = 50)
    private String name;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String description;

     @Column(length = 500)
    private String imageUrl;

    @Enumerated (EnumType.STRING)
    @Column (length = 20)
    private CourseLevel courseLevel;

    @Column(name = "is_published")
    private Boolean isPublished = false;

    @Enumerated (EnumType.STRING)
    private Status status;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    private List<Module> modulos = new ArrayList<>();

    // Lado inverso de la relación muchos-a-muchos con User
    @ManyToMany(mappedBy = "courses")
    @JsonIgnoreProperties({"courses", "password", "profile", "hibernateLazyInitializer", "handler"})
    private List<User> users = new ArrayList<>();

}

