package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Random;

import static java.util.stream.Collectors.toList;

@RestController @RequestMapping("/api")
public class ClientController {
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private AccountRepository accountRepository;

    @RequestMapping("/clients")
    public List<ClientDTO> getClientsDTO() {
        return clientRepository.findAll()
                .stream()
                .map(ClientDTO::new)
                .collect(toList());
    }
    @RequestMapping("/clients/{id}")

    public ClientDTO getClientDTO(@PathVariable Long id){

        return new ClientDTO (clientRepository.findById(id).orElse(null));

    }
    @Autowired
    private PasswordEncoder passwordEncoder;
    @RequestMapping(path = "/clients", method = RequestMethod.POST)
    public ResponseEntity<Object> register(
            @RequestParam String firstName, @RequestParam String lastName,
            @RequestParam String email, @RequestParam String password) {
        if (firstName.isBlank() || lastName.isBlank() || email.isBlank() || password.isBlank()) {
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }
        if (clientRepository.findByEmail(email) !=  null) {
            return new ResponseEntity<>("Name already in use", HttpStatus.FORBIDDEN);
        }
        Client client = new Client(firstName, lastName, email, passwordEncoder.encode(password));
        clientRepository.save(client);
        //agregar la cuenta cuando se cree el cliente
        String randomNumber;
        do {
            Random random = new Random();
            randomNumber = "VIN-" + random.nextInt(99999999)+ 10000000;
        }while (accountRepository.findByNumber(randomNumber) != null);
        Account account = new Account(randomNumber, LocalDate.now(), 0.0);
        account.setClient(client);
        accountRepository.save(account);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }
    @RequestMapping("/clients/current")
    public ClientDTO getAuthenticatedClient (Authentication authentication){
        return new ClientDTO(clientRepository.findByEmail(authentication.getName()));
    }
}