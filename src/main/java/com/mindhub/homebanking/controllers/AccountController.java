package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.AccountType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.ClientService;
import com.mindhub.homebanking.utils.CardUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController @RequestMapping("/api")
public class AccountController {
    @Autowired
    private AccountService accountService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ClientService clientService;

    @GetMapping("/accounts")
    public List<AccountDTO> getAccountsDTO() {
        return accountService.getAccountsDTO();
    }

    @GetMapping("/accounts/{id}")
    public ResponseEntity<Object> getAccountDTO(@PathVariable Long id, Authentication authentication){
        Client client =clientService.findByEmail(authentication.getName());
        Account account = accountService.findById(id);
        if (account == null){
            return new ResponseEntity<>("Authentication required", HttpStatus.UNAUTHORIZED);
        }
        if (!account.getClient().equals(client)){
            return new ResponseEntity<>("The account is inactive", HttpStatus.UNAUTHORIZED);
        }
        if (!account.getHidden()){
            return new ResponseEntity<>("The account is hidden", HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>(new AccountDTO(account),HttpStatus.OK);
    }

    @PostMapping("/clients/current/accounts")
    public ResponseEntity<Object> createAccount(Authentication authentication, @RequestParam AccountType accountType) {
        String randomNumber;
        do {
            randomNumber = CardUtils.getRandomNumber();
        }while (accountService.findByNumber(randomNumber) != null);
        Client client = clientService.findByEmail(authentication.getName());
        if(client.getAccounts().stream().filter(account -> !account.getHidden()).count() > 3){
            return new ResponseEntity<>("Reached maximum number of accounts", HttpStatus.FORBIDDEN);
        }

            Account account = new Account(randomNumber, LocalDate.now(),0.0,true,accountType);
            client.addAccount(account);
            account.setAccountType(accountType);
            accountService.save(account);

        return new ResponseEntity<>("Your account is created",HttpStatus.CREATED);
    }
    @GetMapping("/clients/accounts")
    public List<AccountDTO> accountHidden (Authentication authentication){
        Client client =  clientService.findByEmail(authentication.getName());
        return client.getAccounts().stream().map(account -> new AccountDTO(account)).filter(accountDTO -> accountDTO.getHidden()).collect(Collectors.toList());
    }
    @PatchMapping("/accounts/{id}/hidden")
    public ResponseEntity<Object> deleteAccount(Authentication authentication, @PathVariable Long id){
        if (authentication == null){
            return new ResponseEntity<>("Authentication required", HttpStatus.UNAUTHORIZED);
        }
        if (!authentication.isAuthenticated()){
            return new ResponseEntity<>("Authentication no exist", HttpStatus.UNAUTHORIZED);
        }
        if (id == null){
            return new ResponseEntity<>("Id is required", HttpStatus.UNAUTHORIZED);
        }
        Client client = clientService.findByEmail(authentication.getName());
        if (client == null){
            return new ResponseEntity<>("Client no exist", HttpStatus.UNAUTHORIZED);
        }
        Account account = accountService.findById(id);
//        List<Transaction> transactionList = account.getTransactions().stream().filter(transaction -> transaction.getAccount().getHidden()).collect(Collectors.toList());
        if (account == null){
            return new ResponseEntity<>("Account is not exist", HttpStatus.FORBIDDEN);
        }
        if (!account.getClient().equals(client)){
            return new ResponseEntity<>("The authenticated client is not the legitimate owner of the card.", HttpStatus.UNAUTHORIZED);
        }
        if (!client.getAccounts().contains(account)){
            return new ResponseEntity<>("The card is not present in the client's list of cards.", HttpStatus.FORBIDDEN);
        }
        if (account.getBalance() != 0) {
            return new ResponseEntity<>("Cannot delete account with non-zero balance", HttpStatus.FORBIDDEN);
        }
//        for (Transaction transaction : transactionList ){
//            transaction.getAccount().getHidden(false);
//        }
        account.setHidden(false);
        accountService.save(account);
        return new ResponseEntity<>("Account deactivated successfully", HttpStatus.OK);
    }
}