package com.juliana.demo_park_api.repositories;

import com.juliana.demo_park_api.entities.Space;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SpaceRepository extends JpaRepository<Space, Long> {
    Optional<Space> findByCode(String code);
}
