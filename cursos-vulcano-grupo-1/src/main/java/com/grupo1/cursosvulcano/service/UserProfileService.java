package com.grupo1.cursosvulcano.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.grupo1.cursosvulcano.model.entity.UserProfile;
import com.grupo1.cursosvulcano.repository.UserProfileRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserProfileService {

    private final UserProfileRepository userProfileRepository;

    @Value("${UPLOAD_DIR:uploads}")
    private String uploadDir;


    public List<UserProfile> getAllProfiles(){
        return userProfileRepository.findAll();
    }

    public UserProfile saveProfile(UserProfile profile) {
        return userProfileRepository.save(profile);
    }

    /**
     * Guarda una imagen en la carpeta local y actualiza el perfil del usuario.
     */
    public UserProfile uploadImage(Long profileId, MultipartFile file) throws IOException {
        // 1. Buscar el perfil
        UserProfile profile = userProfileRepository.findById(profileId)
                .orElseThrow(() -> new RuntimeException("Perfil no encontrado"));

        // 2. Crear la carpeta si no existe
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // 3. Generar un nombre único para el archivo
        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        Path filePath = uploadPath.resolve(fileName);

        // 4. Guardar el archivo físicamente
        Files.copy(file.getInputStream(), filePath);

        // 5. Guardar la ruta en la base de datos (con el prefijo del mapeo web)
        profile.setProfilePictureUrl("/uploads/" + fileName);
        
        return userProfileRepository.save(profile);
    }

}

