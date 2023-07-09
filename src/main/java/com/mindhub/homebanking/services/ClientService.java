package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.ClientLoan;

import java.util.List;

public interface ClientService {
    void save(Client client);
    List<ClientDTO> getClientsDTO();
    ClientDTO getClientDTO(Long id);
    Client findById(Long id);
    Client findByEmail(String email);

}
