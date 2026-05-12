package com.grupo1.cursosvulcano.repository;

import com.grupo1.cursosvulcano.model.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

}