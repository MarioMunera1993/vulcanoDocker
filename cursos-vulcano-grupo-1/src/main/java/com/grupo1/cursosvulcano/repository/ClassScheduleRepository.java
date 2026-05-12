package com.grupo1.cursosvulcano.repository;

import com.grupo1.cursosvulcano.model.entity.ClassSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClassScheduleRepository extends JpaRepository<ClassSchedule, Long> {

    List<ClassSchedule> findByStudentId(Long studentId);
}