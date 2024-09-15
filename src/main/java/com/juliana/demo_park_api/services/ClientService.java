package com.juliana.demo_park_api.services;

import com.juliana.demo_park_api.entities.Client;
import com.juliana.demo_park_api.exception.CpfUniqueViolationException;
import com.juliana.demo_park_api.repositories.ClientRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;

    @Transactional
    public Client save(Client client) {
        try {
            return clientRepository.save(client);
        } catch (DataIntegrityViolationException ex) {
            throw new CpfUniqueViolationException(String.format("CPF '%s' cant be registeres, it already exists", client.getCpf()));
        }
    }

    @Transactional(readOnly = true)
    public Client searchById(Long id) {
        return clientRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(String.format("Client id=%s not found", id))
        );
    }
}
