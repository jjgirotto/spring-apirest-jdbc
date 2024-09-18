package com.juliana.demo_park_api.services;

import com.juliana.demo_park_api.entities.Space;
import com.juliana.demo_park_api.exception.CodeUniqueViolationException;
import com.juliana.demo_park_api.exception.EntityNotFoundException;
import com.juliana.demo_park_api.exception.FreeSpaceException;
import com.juliana.demo_park_api.repositories.SpaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.juliana.demo_park_api.entities.Space.StatusSpace.FREE;

@Service
@RequiredArgsConstructor
public class SpaceService {

    private final SpaceRepository spaceRepository;

    @Transactional
    public Space save(Space space) {
        try {
            return spaceRepository.save(space);
        } catch (DataIntegrityViolationException ex) {
            throw new CodeUniqueViolationException("Space", space.getCode());
        }
    }

    @Transactional(readOnly = true)
    public Space searchByCode(String code) {
        return spaceRepository.findByCode(code).orElseThrow(
                () -> new EntityNotFoundException("Space", code)
        );
    }

    @Transactional(readOnly = true)
    public Space searchByFreeSpace() {
        return spaceRepository.findFirstByStatus(FREE).orElseThrow(
                () -> new FreeSpaceException()
        );
    }
}
