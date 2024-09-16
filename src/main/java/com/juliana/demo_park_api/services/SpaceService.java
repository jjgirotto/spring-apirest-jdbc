package com.juliana.demo_park_api.services;

import com.juliana.demo_park_api.entities.Space;
import com.juliana.demo_park_api.exception.CodeUniqueViolationException;
import com.juliana.demo_park_api.exception.EntityNotFoundException;
import com.juliana.demo_park_api.repositories.SpaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SpaceService {

    private final SpaceRepository spaceRepository;

    @Transactional
    public Space save(Space space) {
        try {
            return spaceRepository.save(space);
        } catch (DataIntegrityViolationException ex) {
            throw new CodeUniqueViolationException(String.format("Space code %s already registered", space.getCode()));
        }
    }

    @Transactional(readOnly = true)
    public Space searchByCode(String code) {
        return spaceRepository.findByCode(code).orElseThrow(
                () -> new EntityNotFoundException(String.format("Space code=%s not found", code))
        );
    }
}
