package com.juliana.demo_park_api.repositories;

import com.juliana.demo_park_api.entities.ClientSpace;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClientSpaceRepository extends JpaRepository<ClientSpace, Long> {
    Optional<ClientSpace> findByReciptAndDateExitIsNull(String recipt);
}
