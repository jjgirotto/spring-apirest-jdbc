package com.juliana.demo_park_api.services;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ParkingService {

    private final ClientSpaceService clientSpaceService;
    private final SpaceService spaceService;
    private final ClientService clientService;

}
