package com.juliana.demo_park_api.repositories;

import com.juliana.demo_park_api.entities.ClientSpace;
import com.juliana.demo_park_api.repositories.projection.ClientSpaceProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClientSpaceRepository extends JpaRepository<ClientSpace, Long> {
    Optional<ClientSpace> findByReciptAndDateExitIsNull(String recipt);

    long countByClientCpfAndDateExitIsNotNull(String cpf);

    Page<ClientSpaceProjection> findAllByClientCpf(String cpf, Pageable pageable);

    Page<ClientSpaceProjection> findAllByClientUserId(Long id, Pageable pageable);
}
