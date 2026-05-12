package com.grupo1.cursosvulcano.model.entity;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.grupo1.cursosvulcano.model.enums.Status;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="user_profiles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserProfile extends BaseEntity {

    @Column(length = 50, nullable = false)
    private String firstName;

    @Column(length = 50, nullable = false)
    private String lastName;

    @Column(length = 100, unique = true, nullable = false)
    private String email;

    @Column(columnDefinition = "TEXT")
    private String bio;

    /** URL de la imagen de perfil almacenada en un servicio externo (ej. S3, Cloudinary). */
    @Column(name = "profile_picture_url", columnDefinition = "TEXT")
    private String profilePictureUrl;

    private LocalDate birthDate;

    @Column(name = "phone_number", length = 20)
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    private Status status = Status.ACTIVE;

    @JsonIgnore
    @OneToOne(mappedBy = "profile") // Nombre del atributo en Usuario
    private User user; 

}