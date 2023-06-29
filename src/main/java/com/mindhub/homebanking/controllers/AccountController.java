package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.AccountDTO;
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
public class AccountController {
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ClientRepository clientRepository;
    @RequestMapping("/accounts")

    public List<AccountDTO> getAccountsDTO() {

        return accountRepository.findAll()
                .stream()
                .map(AccountDTO::new)
                .collect(toList());
    }

    @RequestMapping("/accounts/{id}")

    public AccountDTO getAccount(@PathVariable Long id){

        return accountRepository.findById(id).map(client -> new AccountDTO(client)).orElse(null);

    }

    @RequestMapping(path = "/clients/current/accounts", method = RequestMethod.POST)
    public ResponseEntity<Object> createAccount(Authentication authentication) {

        //genero el numero de cuentas aleatorias
        String randomNumber;
        do {
            Random random = new Random();
            randomNumber = "VIN-" + random.nextInt(99999999)+ 10000000;
        }while (accountRepository.findByNumber(randomNumber) != null);

        Client client = clientRepository.findByEmail(authentication.getName());
        //si el cliente tine 3 cuentas
        if(client.getAccounts().size() >= 3){
            return new ResponseEntity<>("Reached maximum number of accounts", HttpStatus.FORBIDDEN);
        }else {
            // Guardo la cuenta en el repositorio
            Account account = new Account(randomNumber, LocalDate.now(),0.0);
            client.addAccount(account);
            accountRepository.save(account);
        }
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}