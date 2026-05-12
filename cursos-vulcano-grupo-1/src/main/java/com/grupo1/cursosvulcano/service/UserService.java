package com.grupo1.cursosvulcano.service;

import java.util.List;
import org.springframework.stereotype.Service;

import com.grupo1.cursosvulcano.model.entity.Course;
import com.grupo1.cursosvulcano.model.entity.User;
import com.grupo1.cursosvulcano.model.entity.UserProfile;
import com.grupo1.cursosvulcano.model.enums.UserRole;
import com.grupo1.cursosvulcano.repository.CourseRepository;
import com.grupo1.cursosvulcano.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor // Genera el constructor para la inyección de dependencias
public class UserService {

    private final UserRepository userRepository;
    private final CourseRepository courseRepository;

    @Transactional
    public User createUser(User user, UserProfile profile) {
        if (user.getUsername() == null || user.getUsername().isBlank()) {
            throw new IllegalArgumentException("El username es obligatorio");
        }

        if (userRepository.existsByUsername(user.getUsername())) {
            throw new IllegalArgumentException("El username ya está en uso");
        }

        // Usamos el método helper que creamos para vincular ambos objetos en memoria
        user.setProfile(profile);
        
        // REGLA DE NEGOCIO: mario_munera es siempre ADMINISTRADOR
        if ("mario_munera".equalsIgnoreCase(user.getUsername())) {
            user.setRole(UserRole.ADMIN);
        }
        
        // Al guardar el usuario, se guarda el perfil automáticamente por el cascade
        return userRepository.save(user);
    }

    /**
     * Obtiene un usuario por ID.
     */
    public User getUserById(Long id) {
        return userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));
    }

    /**
     * Actualiza los datos del perfil de un usuario existente.
     *
     * ¿Cómo funciona?
     *   1. Buscamos al usuario que ya existe en la base de datos.
     *   2. Accedemos a su perfil existente.
     *   3. Solo pisamos los campos que llegaron en el payload (los que no son null).
     *   4. Guardamos. El CascadeType.ALL se encarga de guardar el perfil también.
     *
     * @param id         ID del usuario a actualizar
     * @param datosNuevos Objeto User con los nuevos datos en su campo "profile"
     * @return El usuario actualizado completo
     */
    @Transactional
    public User updateUser(Long id, User datosNuevos) {
        // PASO 1: Buscamos el usuario que existe en la base de datos
        User userExistente = getUserById(id);
        UserProfile perfilExistente = userExistente.getProfile();
        UserProfile perfilNuevo = datosNuevos.getProfile();

        // PASO 2: Actualizamos solo los campos que llegaron (que no son null)
        if (perfilNuevo != null) {
            if (perfilNuevo.getFirstName() != null)
                perfilExistente.setFirstName(perfilNuevo.getFirstName());

            if (perfilNuevo.getLastName() != null)
                perfilExistente.setLastName(perfilNuevo.getLastName());

            if (perfilNuevo.getEmail() != null)
                perfilExistente.setEmail(perfilNuevo.getEmail());

            if (perfilNuevo.getBio() != null)
                perfilExistente.setBio(perfilNuevo.getBio());

            if (perfilNuevo.getPhoneNumber() != null)
                perfilExistente.setPhoneNumber(perfilNuevo.getPhoneNumber());

            if (perfilNuevo.getProfilePictureUrl() != null)
                perfilExistente.setProfilePictureUrl(perfilNuevo.getProfilePictureUrl());
        }

        // PASO 3: Guardamos el usuario y Spring persiste el perfil por el cascade
        return userRepository.save(userExistente);
    }

    /**
     * Elimina un usuario y, debido a CascadeType.ALL, también eliminará su perfil.
     */
    @Transactional
    public void deleteUser(Long id) {
        User user = getUserById(id);
        userRepository.delete(user);
    }

    /**
     * Obtener todos los usuarios registrados.
     */
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Actualizar solo el rol de un usuario.
     */
    @Transactional
    public User updateUserRole(Long id, UserRole role) {
        User user = getUserById(id);
        user.setRole(role);
        return userRepository.save(user);
    }

    /**
     * Inscribir un usuario en un curso (relación ManyToMany).
     * Valida que ambos existan y que el usuario no esté ya inscrito.
     */
    @Transactional
    public User enrollInCourse(Long userId, Long courseId) {
        User user = getUserById(userId);
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Curso no encontrado con ID: " + courseId));

        // Verificar que no esté ya inscrito
        boolean alreadyEnrolled = user.getCourses().stream()
                .anyMatch(c -> c.getId().equals(courseId));
        if (alreadyEnrolled) {
            throw new IllegalArgumentException("El usuario ya está inscrito en este curso.");
        }

        user.getCourses().add(course);
        return userRepository.save(user);
    }
}


