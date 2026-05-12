package com.grupo1.cursosvulcano.repository;
import com.grupo1.cursosvulcano.model.entity.Module;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ModuleRepository extends JpaRepository<Module, Long> {
    List<Module> findByCourseIdOrderByOrderIndexAsc(Long courseId);
    Integer countByCourseId(Long courseId);
    List<Module> findByCourseIdAndOrderIndexGreaterThanEqual(Long courseId, Integer orderIndex);
}
