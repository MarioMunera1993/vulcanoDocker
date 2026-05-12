package com.grupo1.cursosvulcano.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.grupo1.cursosvulcano.model.entity.UserProfile;
import com.grupo1.cursosvulcano.service.UserProfileService;


@RestController
@RequestMapping("/api/userProfiles")
public class UserProfileController {

    @Autowired
    private UserProfileService profileService;

    @GetMapping
    public List<UserProfile> getAllProfiles() {
        return profileService.getAllProfiles();
    }

    @PostMapping
    public UserProfile saveProfile(@RequestBody UserProfile profile) {
        return profileService.saveProfile(profile);
    }

    /**
     * Endpoint para subir una imagen de perfil.
     * Se usa POST /api/userProfiles/{id}/upload-image
     */
    @PostMapping("/{id}/upload-image")
    public ResponseEntity<?> uploadImage(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        try {
            UserProfile updatedProfile = profileService.uploadImage(id, file);
            return ResponseEntity.ok(updatedProfile);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body("Error al guardar la imagen: " + e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}

