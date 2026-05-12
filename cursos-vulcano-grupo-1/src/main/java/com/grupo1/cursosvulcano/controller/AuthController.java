package com.grupo1.cursosvulcano.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.grupo1.cursosvulcano.dto.request.LoginRequest;
import com.grupo1.cursosvulcano.model.entity.User;
import com.grupo1.cursosvulcano.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;

    // ----------------------------------------------------------
    // POST /api/auth/login
    // ----------------------------------------------------------
    // Recibe: { "username": "...", "password": "..." }
    // Devuelve:
    //   200 OK  → el objeto User completo (incluyendo su rol y perfil)
    //   401     → texto de error ("Usuario no encontrado" / "Contraseña incorrecta")
    // ----------------------------------------------------------
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {

        // Buscamos el usuario por username en la base de datos
        User user = userRepository.findByUsername(request.getUsername()).orElse(null);

        // Si no existe el usuario → respondemos con error 401
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuario no encontrado");
        }

        // Verificamos la contraseña (comparación directa, sin encriptación por ahora)
        if (!user.getPassword().equals(request.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Contraseña incorrecta");
        }

        // ✅ Login exitoso: devolvemos el objeto User completo.
        // Como agregamos el campo 'role' en la entidad User, ahora el frontend
        // recibirá también si es ADMINISTRADOR o USUARIO.
        return ResponseEntity.ok(user);
    }
}
