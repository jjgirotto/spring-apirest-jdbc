package com.juliana.demo_park_api.services;

import com.juliana.demo_park_api.entities.ClientSpace;
import com.juliana.demo_park_api.repositories.ClientSpaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ClientSpaceService {

    private final ClientSpaceRepository clientSpaceRepository;

    @Transactional
    public ClientSpace save(ClientSpace clientSpace) {
        return clientSpaceRepository.save(clientSpace);
    }

}
