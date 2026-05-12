package com.grupo1.cursosvulcano.service;

import com.grupo1.cursosvulcano.dto.request.ClassScheduleRequest;
import com.grupo1.cursosvulcano.dto.response.AvailableScheduleGroupResponse;
import com.grupo1.cursosvulcano.dto.response.ClassScheduleResponse;
import com.grupo1.cursosvulcano.model.entity.ClassSchedule;
import com.grupo1.cursosvulcano.repository.ClassScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClassScheduleService {

    private final ClassScheduleRepository classScheduleRepository;

    public List<AvailableScheduleGroupResponse> getAvailableSchedules(Long expertId) {
        // Datos genéricos de horarios disponibles agrupados por fecha
        List<LocalDateTime> scheduleTimes = List.of(
            LocalDateTime.of(2027, 10, 1, 9, 0),
            LocalDateTime.of(2026, 8, 20, 11, 0),
            LocalDateTime.of(2026, 9, 20, 14, 0),
            LocalDateTime.of(2026, 7, 21, 10, 0),
            LocalDateTime.of(2026, 5, 21, 13, 0),
            LocalDateTime.of(2026, 6, 21, 16, 0),
            LocalDateTime.of(2026, 8, 22, 8, 0),
            LocalDateTime.of(2026, 5, 22, 12, 0)
        );

        Map<String, List<String>> grouped = new HashMap<>();

        for (LocalDateTime dateTime : scheduleTimes) {
            String date = dateTime.toLocalDate().toString();
            String time = formatTime(dateTime.getHour(), dateTime.getMinute());
            grouped.computeIfAbsent(date, k -> new ArrayList<>()).add(time);
        }

        return grouped.entrySet().stream()
            .map(entry -> {
                AvailableScheduleGroupResponse response = new AvailableScheduleGroupResponse();
                response.setDate(entry.getKey());
                response.setTimes(entry.getValue());
                return response;
            })
            .collect(Collectors.toList());
    }

    private String formatTime(int hour, int minute) {
        String ampm = hour >= 12 ? "PM" : "AM";
        int displayHour = hour % 12;
        if (displayHour == 0) displayHour = 12;
        return String.format("%02d:%02d %s", displayHour, minute, ampm);
    }

    public List<ClassScheduleResponse> getStudentSchedules(Long studentId) {
        List<ClassSchedule> schedules = classScheduleRepository.findByStudentId(studentId);
        return schedules.stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    public ClassScheduleResponse scheduleClass(Long studentId, ClassScheduleRequest request) {
        ClassSchedule schedule = new ClassSchedule();
        schedule.setStudentId(studentId);
        schedule.setCourseId(request.getCourseId());
        schedule.setStartTime(request.getStartTime());
        schedule.setNotes(request.getNotes());
        schedule.setStatus("SCHEDULED");
        ClassSchedule saved = classScheduleRepository.save(schedule);
        return mapToResponse(saved);
    }

    public ClassScheduleResponse modifySchedule(Long scheduleId, Long studentId, ClassScheduleRequest request) {
        ClassSchedule schedule = classScheduleRepository.findById(scheduleId)
            .orElseThrow(() -> new RuntimeException("Schedule not found"));
        if (!schedule.getStudentId().equals(studentId)) {
            throw new RuntimeException("Unauthorized");
        }
        schedule.setStartTime(request.getStartTime());
        schedule.setNotes(request.getNotes());
        ClassSchedule saved = classScheduleRepository.save(schedule);
        return mapToResponse(saved);
    }

    public void cancelSchedule(Long scheduleId, Long studentId) {
        ClassSchedule schedule = classScheduleRepository.findById(scheduleId)
            .orElseThrow(() -> new RuntimeException("Schedule not found"));
        if (!schedule.getStudentId().equals(studentId)) {
            throw new RuntimeException("Unauthorized");
        }
        classScheduleRepository.delete(schedule);
    }

    private ClassScheduleResponse mapToResponse(ClassSchedule schedule) {
        ClassScheduleResponse response = new ClassScheduleResponse();
        response.setId(schedule.getId());
        response.setStudentId(schedule.getStudentId());
        response.setCourseId(schedule.getCourseId());
        response.setStartTime(schedule.getStartTime());
        response.setEndTime(schedule.getEndTime());
        response.setStatus(schedule.getStatus());
        response.setNotes(schedule.getNotes());
        return response;
    }
}