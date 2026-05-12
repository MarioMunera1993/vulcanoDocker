package com.grupo1.cursosvulcano.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.grupo1.cursosvulcano.model.entity.Review;

public interface ReviewRepository extends JpaRepository<Review, Long> {

}
