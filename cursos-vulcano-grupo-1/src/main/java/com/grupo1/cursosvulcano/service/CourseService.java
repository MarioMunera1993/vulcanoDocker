package com.grupo1.cursosvulcano.service;

import java.util.List;
import java.util.stream.Collectors;
import com.grupo1.cursosvulcano.model.entity.Course;
import com.grupo1.cursosvulcano.model.entity.User;
import com.grupo1.cursosvulcano.model.enums.UserRole;
import com.grupo1.cursosvulcano.repository.CourseRepository;
import com.grupo1.cursosvulcano.repository.ModuleRepository;
import com.grupo1.cursosvulcano.repository.UserRepository;
import com.grupo1.cursosvulcano.dto.request.CourseRequestDTO;
import com.grupo1.cursosvulcano.dto.response.CourseResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private ModuleRepository moduleRepository;

    @Autowired
    private UserRepository userRepository;

    public List<CourseResponseDTO> getAllCourses() {
        return courseRepository.findAll().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public CourseResponseDTO getCourseById(Long id) {
        Course course = courseRepository.findById(id).orElseThrow();
        return mapToResponseDTO(course);
    }

    private void validateAdminRole(Long userId) {
        if (userId == null) {
            throw new IllegalArgumentException("El ID del usuario es obligatorio para esta acción.");
        }
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado."));
        
        if (user.getRole() != UserRole.ADMIN) {
            throw new SecurityException("¡Acceso Denegado! Solo los administradores pueden gestionar cursos.");
        }
    }

    public CourseResponseDTO createCourse(CourseRequestDTO request) {
        validateAdminRole(request.getCreatorUserId());

        if (Boolean.TRUE.equals(request.getIsPublished())) {
            throw new IllegalArgumentException("No se puede publicar un curso que no tiene módulos.");
        }
        
        Course course = new Course();
        course.setName(request.getName());
        course.setDescription(request.getDescription());
        course.setImageUrl(request.getImageUrl());
        course.setCourseLevel(request.getCourseLevel());
        course.setIsPublished(request.getIsPublished() != null ? request.getIsPublished() : false);
        course.setStatus(request.getStatus());

        Course savedCourse = courseRepository.save(course);
        return mapToResponseDTO(savedCourse);
    }

    public CourseResponseDTO updateCourse(Long id, CourseRequestDTO request) {
        validateAdminRole(request.getCreatorUserId());

        Course existingCourse = courseRepository.findById(id).orElseThrow();
        
        if (Boolean.TRUE.equals(request.getIsPublished())) {
            Integer moduleCount = moduleRepository.countByCourseId(id);
            if (moduleCount == null || moduleCount == 0) {
                throw new IllegalArgumentException("No se puede publicar un curso que no tiene módulos.");
            }
        }
        
        existingCourse.setName(request.getName());
        existingCourse.setDescription(request.getDescription());
        existingCourse.setImageUrl(request.getImageUrl());
        existingCourse.setCourseLevel(request.getCourseLevel());
        existingCourse.setIsPublished(request.getIsPublished() != null ? request.getIsPublished() : existingCourse.getIsPublished());
        existingCourse.setStatus(request.getStatus());
        
        Course updatedCourse = courseRepository.save(existingCourse);
        return mapToResponseDTO(updatedCourse);
    }

    public void deleteCourse(Long id) {
        courseRepository.deleteById(id);
    }

    private CourseResponseDTO mapToResponseDTO(Course course) {
        CourseResponseDTO dto = new CourseResponseDTO();
        dto.setId(course.getId());
        dto.setName(course.getName());
        dto.setDescription(course.getDescription());
        dto.setImageUrl(course.getImageUrl());
        dto.setCourseLevel(course.getCourseLevel());
        dto.setIsPublished(course.getIsPublished());
        dto.setStatus(course.getStatus());
        return dto;
    }
}