package com.juliana.demo_park_api.repositories;

import com.juliana.demo_park_api.entities.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, Long> {
}
