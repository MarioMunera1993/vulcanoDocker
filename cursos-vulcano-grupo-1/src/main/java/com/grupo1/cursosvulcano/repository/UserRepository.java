package com.grupo1.cursosvulcano.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.grupo1.cursosvulcano.model.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Busca un usuario por su username
    Optional<User> findByUsername(String username);

    // Comprueba si ya existe un username
    boolean existsByUsername(String username);

}
