package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.TransferDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.ClientService;
import com.mindhub.homebanking.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

@RestController @RequestMapping("/api")
public class TransactionController {
    @Autowired
    private ClientService clientService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private TransactionService transactionService;
    @Transactional
    @PostMapping(path= "/transactions")
    public ResponseEntity<Object> sendTransactions (Authentication authentication, @RequestBody TransferDTO transferDTO){


        if (transferDTO.getAccountOrigin().isBlank()){
            return new  ResponseEntity<>("Origin account is missing.", HttpStatus.FORBIDDEN);
        }
        if ( transferDTO.getAccountDestination().isBlank()) {
            return new  ResponseEntity<>("Destination account is missing.", HttpStatus.FORBIDDEN);
        }
        if (transferDTO.getAmount() == null) {
            return new ResponseEntity<>("Amount is missing.", HttpStatus.FORBIDDEN);
        }
        if (transferDTO.getAmount() <= 0.0){
            return new ResponseEntity<>("These amounts are not valid.", HttpStatus.FORBIDDEN);
        }
        if ( transferDTO.getDescription().isBlank()){
            return new  ResponseEntity<>("Description is missing.", HttpStatus.FORBIDDEN);
        }
        if (transferDTO.getAccountDestination().equals(transferDTO.getAccountOrigin())){
            return new ResponseEntity<>("The origin account and the destination account cannot be the same.", HttpStatus.FORBIDDEN);
        }
        Account accountOrigin = accountService.findByNumber(transferDTO.getAccountOrigin());
        Account accountDestination = accountService.findByNumber(transferDTO.getAccountDestination());
        Double amount = transferDTO.getAmount();
        String description = transferDTO.getDescription();
        if (accountOrigin == null){
            return new ResponseEntity<>("The origin account is not entered.", HttpStatus.FORBIDDEN);
        }
        if (accountDestination == null){
            return new ResponseEntity<>("The destination account is not entered.", HttpStatus.FORBIDDEN);
        }
        Client client = clientService.findByEmail(authentication.getName());
        if (client.getAccounts().stream().filter(item -> item.getNumber().equals(transferDTO.getAccountOrigin())).collect(Collectors.toSet()).isEmpty()){
            return new ResponseEntity<>("The origin account does not belong to the authenticated client.", HttpStatus.FORBIDDEN);
        }
        if (accountOrigin.getBalance() < transferDTO.getAmount() ){
            return new ResponseEntity<>("The origin account does not have sufficient funds.", HttpStatus.FORBIDDEN);
        }

        Transaction transaction = new Transaction(TransactionType.CREDIT,amount,transferDTO.getAccountOrigin() + description, LocalDateTime.now(),accountDestination.getBalance() + amount);
        Transaction transaction1 = new Transaction(TransactionType.DEBIT,Double.parseDouble("-" + amount),transferDTO.getAccountDestination() + description, LocalDateTime.now(),accountOrigin.getBalance() - amount);
        accountOrigin.setBalance(accountOrigin.getBalance() - amount);
        accountDestination.setBalance(accountDestination.getBalance() + amount);
        accountDestination.addTransaction(transaction);
        accountOrigin.addTransaction(transaction1);
        transactionService.save(transaction1);
        transactionService.save(transaction);
        accountService.save(accountDestination);
        accountService.save(accountOrigin);
        return new ResponseEntity<>("Transaction completed.",HttpStatus.CREATED);
    }
}
