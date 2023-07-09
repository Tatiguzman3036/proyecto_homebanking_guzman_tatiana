package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Random;

@RestController @RequestMapping("/api")
public class AccountController {
    @Autowired
    private AccountService accountService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ClientService clientService;

    @RequestMapping("/accounts")
    public List<AccountDTO> getAccountsDTO() {
        return accountService.getAccountsDTO();
    }

    @RequestMapping("/accounts/{id}")
    public AccountDTO getAccountDTO(@PathVariable Long id){
        return new AccountDTO(accountService.findById(id));
    }

    @RequestMapping(path = "/clients/current/accounts", method = RequestMethod.POST)
    public ResponseEntity<Object> createAccount(Authentication authentication) {
        //genero el numero de cuentas aleatorias
        String randomNumber;
        do {
            Random random = new Random();
            randomNumber = "VIN-" + random.nextInt(99999999)+ 10000000;
        }while (accountService.findByNumber(randomNumber) != null);

        Client client = clientService.findByEmail(authentication.getName());
        //si el cliente tine 3 cuentas
        if(client.getAccounts().size() >= 3){
            return new ResponseEntity<>("Reached maximum number of accounts", HttpStatus.FORBIDDEN);
        }else {
            // Guardo la cuenta en el repositorio
            Account account = new Account(randomNumber, LocalDate.now(),0.0);
            client.addAccount(account);
            accountService.save(account);
        }
        return new ResponseEntity<>("Your account is created",HttpStatus.CREATED);
    }
}