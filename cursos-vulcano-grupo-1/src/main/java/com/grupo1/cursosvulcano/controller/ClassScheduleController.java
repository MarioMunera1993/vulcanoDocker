package com.grupo1.cursosvulcano.controller;

import com.grupo1.cursosvulcano.dto.request.ClassScheduleRequest;
import com.grupo1.cursosvulcano.dto.response.AvailableScheduleGroupResponse;
import com.grupo1.cursosvulcano.dto.response.ClassScheduleResponse;
import com.grupo1.cursosvulcano.service.ClassScheduleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
 
@RestController
@RequestMapping("/api/schedules")
@RequiredArgsConstructor
public class ClassScheduleController {

    private final ClassScheduleService classScheduleService;
 
    /**
     * GET /api/schedules/available/{expertId}
     * Retorna los horarios disponibles de un experto para los próximos 7 días.
     */
    @GetMapping("/available/{expertId}")
    public ResponseEntity<List<AvailableScheduleGroupResponse>> getAvailableSchedules(
            @PathVariable Long expertId) {
 
        List<AvailableScheduleGroupResponse> available = classScheduleService.getAvailableSchedules(expertId);
        return ResponseEntity.ok(available);
    }
 
    /**
     * GET /api/schedules/student/{studentId}
     * Retorna todas las clases activas agendadas por un estudiante.
     */
    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<ClassScheduleResponse>> getStudentSchedules(
            @PathVariable Long studentId) {
 
        List<ClassScheduleResponse> schedules = classScheduleService.getStudentSchedules(studentId);
        return ResponseEntity.ok(schedules);
    }
 
    /**
     * POST /api/schedules/student/{studentId}
     * Agenda una nueva clase privada.
     */
    @PostMapping("/student/{studentId}")
    public ResponseEntity<ClassScheduleResponse> scheduleClass(
            @PathVariable Long studentId,
            @Valid @RequestBody ClassScheduleRequest request) {
 
        ClassScheduleResponse response = classScheduleService.scheduleClass(studentId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
 
    /**
     * PUT /api/schedules/{scheduleId}/student/{studentId}
     * Modifica una clase ya agendada.
     */
    @PutMapping("/{scheduleId}/student/{studentId}")
    public ResponseEntity<ClassScheduleResponse> modifySchedule(
            @PathVariable Long scheduleId,
            @PathVariable Long studentId,
            @Valid @RequestBody ClassScheduleRequest request) {
 
        ClassScheduleResponse response = classScheduleService.modifySchedule(scheduleId, studentId, request);
        return ResponseEntity.ok(response);
    }
 
    /**
     * DELETE /api/schedules/{scheduleId}/student/{studentId}
     * Cancela una clase agendada.
     */
    @DeleteMapping("/{scheduleId}/student/{studentId}")
    public ResponseEntity<Void> cancelSchedule(
            @PathVariable Long scheduleId,
            @PathVariable Long studentId) {
 
        classScheduleService.cancelSchedule(scheduleId, studentId);
        return ResponseEntity.noContent().build();
    }
}