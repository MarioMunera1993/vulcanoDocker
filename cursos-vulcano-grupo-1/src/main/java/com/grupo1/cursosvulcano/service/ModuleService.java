package com.grupo1.cursosvulcano.service;

import com.grupo1.cursosvulcano.dto.response.ModuleResponse;
import com.grupo1.cursosvulcano.model.entity.Course;
import com.grupo1.cursosvulcano.model.entity.Module;
import com.grupo1.cursosvulcano.model.entity.User;
import com.grupo1.cursosvulcano.model.enums.UserRole;
import com.grupo1.cursosvulcano.repository.CourseRepository;
import com.grupo1.cursosvulcano.repository.ModuleRepository;
import com.grupo1.cursosvulcano.repository.UserRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ModuleService {
    @Autowired
    private ModuleRepository moduleRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UserRepository userRepository;

    public List<Module> getAllModules() {
        return moduleRepository.findAll();
    }

    public List<Module> getModulesByCourseId(Long courseId) {
        return moduleRepository.findByCourseIdOrderByOrderIndexAsc(courseId);
    }


    public Module getModuleById(Long id) {
        return moduleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Módulo no encontrado"));
    }

    /**
     * Valida que el usuario tenga rol ADMIN antes de ejecutar operaciones de escritura.
     * Misma lógica que CourseService.validateAdminRole().
     */
    private void validateAdminRole(Long userId) {
        if (userId == null) {
            throw new IllegalArgumentException("El ID del usuario es obligatorio para esta acción.");
        }
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado."));
        
        if (user.getRole() != UserRole.ADMIN) {
            throw new SecurityException("¡Acceso Denegado! Solo los administradores pueden gestionar módulos.");
        }
    }

    public Module createModule(Module module, Long courseId, Long userId) {
        validateAdminRole(userId);
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Curso no encontrado"));

        module.setCourse(course);
        return moduleRepository.save(module);
    }

    public Module updateModule(Long id, Module module, Long userId) {
        validateAdminRole(userId);
        module.setId(id);
        return moduleRepository.save(module);
    }

    public void deleteModule(Long id, Long userId) {
        validateAdminRole(userId);
        moduleRepository.deleteById(id);
    }

    private ModuleResponse mapToResponse(Module module) {
        ModuleResponse response = new ModuleResponse();
        response.setId(module.getId());
        response.setCreatedAt(module.getCreatedAt());
        response.setUpdatedAt(module.getUpdatedAt());
        response.setCourseId(module.getCourse().getId());
        response.setName(module.getContent().getName());
        response.setDescription(module.getContent().getDescription());
        response.setVideoUrl(module.getVideoUrl());
        response.setDurationInMinutes(module.getDurationInMinutes());
        response.setMarkdownUrl(module.getMarkdownUrl());
        response.setInteractiveGameUrl(module.getInteractiveGameUrl());
        response.setOrderIndex(module.getOrderIndex());
        response.setStatus(module.getStatus());
        return response;
    }

}

