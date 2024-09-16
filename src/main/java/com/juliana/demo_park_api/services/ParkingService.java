package com.juliana.demo_park_api.services;


import com.juliana.demo_park_api.entities.Client;
import com.juliana.demo_park_api.entities.ClientSpace;
import com.juliana.demo_park_api.entities.Space;
import com.juliana.demo_park_api.util.ParkingUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ParkingService {

    private final ClientSpaceService clientSpaceService;
    private final SpaceService spaceService;
    private final ClientService clientService;

    @Transactional
    public ClientSpace checkIn (ClientSpace clientSpace) {
        Client client = clientService.searchByCpf(clientSpace.getClient().getCpf());
        clientSpace.setClient(client);

        Space space = spaceService.searchByFreeSpace();
        space.setStatus(Space.StatusSpace.OCCUPIED);
        clientSpace.setSpace(space);

        clientSpace.setDateEntry(LocalDateTime.now());
        clientSpace.setRecipt(ParkingUtils.generateRecipt());

        return clientSpaceService.save(clientSpace);
    }

}
