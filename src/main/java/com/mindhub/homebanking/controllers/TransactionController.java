package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.TransactionDTO;
import com.mindhub.homebanking.dtos.TransferDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
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
    ClientRepository clientRepository;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    TransactionRepository transactionRepository;
    @Transactional
    @RequestMapping(path= "/transactions", method = RequestMethod.POST)
    public ResponseEntity<Object> sendTransactions (Authentication authentication, @RequestBody TransferDTO transferDTO){

        Account accountOrigin = accountRepository.findByNumber(transferDTO.getAccountOrigin());
        Account accountDestination = accountRepository.findByNumber(transferDTO.getAccountDestination());
        Double amount = transferDTO.getAmount();
        String description = transferDTO.getDescription();
        if (transferDTO.getAccountOrigin().isBlank()){
            return new  ResponseEntity<>("falto la cuenta de origen", HttpStatus.FORBIDDEN);
        }
        if ( transferDTO.getAccountDestination().isBlank()) {
            return new  ResponseEntity<>("falto la cuenta de destino", HttpStatus.FORBIDDEN);
        }
        if ( transferDTO.getAmount().isNaN()){
            return new  ResponseEntity<>("falto el monto", HttpStatus.FORBIDDEN);
        }
        if (transferDTO.getAmount() < 1.0){
            return new ResponseEntity<>("Fondos insuficientes", HttpStatus.FORBIDDEN);
        }
        if ( transferDTO.getDescription().isBlank()){
            return new  ResponseEntity<>("falto la descripcion", HttpStatus.FORBIDDEN);
        }
        if (transferDTO.getAccountDestination().equals(transferDTO.getAccountOrigin())){
            return new ResponseEntity<>("las cuentas son iguales", HttpStatus.FORBIDDEN);
        }
        if (accountOrigin == null){
            return new ResponseEntity<>("la cuenta de origen no existe", HttpStatus.FORBIDDEN);
        }
        if (accountDestination == null){
            return new ResponseEntity<>("la cuenta de destino no existe", HttpStatus.FORBIDDEN);
        }
        Client client = clientRepository.findByEmail(authentication.getName());
        if (client.getAccounts().stream().filter(item -> item.getNumber().equals(transferDTO.getAccountOrigin())).collect(Collectors.toSet()).isEmpty()){
            return new ResponseEntity<>("la cuenta está en uso" , HttpStatus.FORBIDDEN);
        }
        if (accountOrigin.getBalance() < transferDTO.getAmount() ){
            return new ResponseEntity<>("la cuenta de origen no tiene fondos suficientes", HttpStatus.FORBIDDEN);
        }
        Transaction transaction = new Transaction(TransactionType.CREDIT,amount,transferDTO.getAccountOrigin() + description, LocalDateTime.now());
        Transaction transaction1 = new Transaction(TransactionType.DEBIT,Double.parseDouble("-" + amount),transferDTO.getAccountDestination() + description, LocalDateTime.now());
        accountOrigin.setBalance(accountOrigin.getBalance() - amount);
        accountDestination.setBalance(accountDestination.getBalance() + amount);
        accountDestination.addTransaction(transaction);
        accountOrigin.addTransaction(transaction1);
        transactionRepository.save(transaction1);
        transactionRepository.save(transaction);

        return new ResponseEntity<>("Transaccion realizada",HttpStatus.CREATED);
    }
}
