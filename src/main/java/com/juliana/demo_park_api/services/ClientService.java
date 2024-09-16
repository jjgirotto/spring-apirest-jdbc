package com.juliana.demo_park_api.services;

import com.juliana.demo_park_api.entities.Client;
import com.juliana.demo_park_api.exception.CpfUniqueViolationException;
import com.juliana.demo_park_api.exception.EntityNotFoundException;
import com.juliana.demo_park_api.repositories.ClientRepository;
import com.juliana.demo_park_api.repositories.projection.ClientProjection;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    @Transactional(readOnly = true)
    public Page<ClientProjection> searchAll(Pageable pageable) {
        return clientRepository.findAllPageable(pageable);
    }

    @Transactional(readOnly = true)
    public Client searchByUserId(Long id) {
        return clientRepository.findByUserId(id);
    }

    @Transactional(readOnly = true)
    public Client searchByCpf(String cpf) {
        return clientRepository.findByCpf(cpf).orElseThrow(
                () -> new EntityNotFoundException(String.format("Client cpf=%s not found", cpf))
        );
    }
}
