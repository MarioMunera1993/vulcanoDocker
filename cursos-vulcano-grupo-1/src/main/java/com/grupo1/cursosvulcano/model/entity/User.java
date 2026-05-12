package com.grupo1.cursosvulcano.model.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.grupo1.cursosvulcano.model.enums.UserRole;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseEntity {

    @Column(unique = true, nullable = false)
    private String username;

    private String password;

    @OneToOne(cascade = CascadeType.ALL) // Si guardo usuario, se guarda perfil
    @JoinColumn(name = "perfil_id", referencedColumnName = "id")
    private UserProfile profile;

    @Enumerated(EnumType.STRING)
    private UserRole role = UserRole.USER; // Valor por defecto

    // Relación muchos-a-muchos: un usuario puede inscribirse en muchos cursos
    @ManyToMany
    @JoinTable(
        name = "user_courses",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "course_id")
    )
    @JsonIgnoreProperties({"users", "modulos", "hibernateLazyInitializer", "handler"})
    private java.util.List<Course> courses = new java.util.ArrayList<>();


    // Métodos Helper (Convenience methods)
    public void setProfile(UserProfile profile) {
        this.profile = profile;
        if (profile != null) {
            profile.setUser(this); // Establece la relación en el sentido inverso
        }
    }

}
