package com.grupo1.cursosvulcano.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.grupo1.cursosvulcano.model.entity.User;
import com.grupo1.cursosvulcano.model.entity.UserProfile;
import com.grupo1.cursosvulcano.model.enums.UserRole;
import com.grupo1.cursosvulcano.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * Crear un usuario y su perfil simultáneamente.
     * El JSON enviado debe tener una estructura anidada para el perfil.
     */
    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody User user) {
        try {
            // Extraemos el perfil que viene dentro del objeto User
            UserProfile profile = user.getProfile();
            
            // Llamamos al servicio que ya tiene la lógica del método helper y el cascade
            User savedUser = userService.createUser(user, profile);
            
            return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
        } catch (IllegalArgumentException ex) {
            // Devolvemos un JSON con clave "message" para que el frontend lo parsee consistentemente
            return ResponseEntity.badRequest().body(Map.of("message", ex.getMessage()));
        }
    }

    /**
     * Obtener un usuario por su ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    /**
     * Actualizar el perfil de un usuario existente.
     *
     * ¿Cómo funciona?
     *   - Recibe el ID del usuario en la URL (ej: PUT /api/users/3)
     *   - Recibe en el BODY el objeto User con los nuevos datos del perfil
     *   - Delega la lógica al UserService.updateUser()
     *   - Retorna el usuario actualizado completo (200 OK)
     */
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User user) {
        User updatedUser = userService.updateUser(id, user);
        return ResponseEntity.ok(updatedUser);
    }

    /**
     * Eliminar un usuario (esto también borrará su perfil por el CascadeType.ALL).
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Obtener lista de todos los usuarios.
     */
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    /**
     * Actualizar solo el ROL de un usuario.
     * Recibe: { "role": "ADMIN" } o { "role": "USER" }
     */
    @PatchMapping("/{id}/role")
    public ResponseEntity<User> updateRole(@PathVariable Long id, @RequestBody Map<String, String> body) {
        String roleStr = body.get("role");
        UserRole role = UserRole.valueOf(roleStr);
        return ResponseEntity.ok(userService.updateUserRole(id, role));
    }

    /**
     * Inscribir un usuario en un curso.
     * POST /api/users/{userId}/courses/{courseId}
     */
    @PostMapping("/{userId}/courses/{courseId}")
    public ResponseEntity<?> enrollInCourse(@PathVariable Long userId, @PathVariable Long courseId) {
        try {
            User updatedUser = userService.enrollInCourse(userId, courseId);
            return ResponseEntity.ok(updatedUser);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(Map.of("message", ex.getMessage()));
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(Map.of("message", ex.getMessage()));
        }
    }
}


