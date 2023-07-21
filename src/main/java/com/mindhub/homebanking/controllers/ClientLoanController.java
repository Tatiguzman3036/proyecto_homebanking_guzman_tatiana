package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.ClientLoanService;
import com.mindhub.homebanking.services.ClientService;
import com.mindhub.homebanking.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController @RequestMapping("/api")
public class ClientLoanController {
    @Autowired
    ClientLoanService clientLoanService;
    @Autowired
    AccountService accountService;
    @Autowired
    TransactionService transactionService;
    @Autowired
    ClientService clientService;

    @PostMapping("/clientLoan/payments")
    public ResponseEntity<Object> payLoan(Authentication authentication, @RequestParam Long loanToPay, @RequestParam String account) {
        if (authentication == null) {
            return new ResponseEntity<>("Client must be authenticated", HttpStatus.FORBIDDEN);
        }
        if (loanToPay == null) {
            return new ResponseEntity<>("Missing loan information", HttpStatus.FORBIDDEN);
        }
        if (account.isBlank()) {
            return new ResponseEntity<>("Missing account", HttpStatus.FORBIDDEN);
        }
        Client client = clientService.findByEmail(authentication.getName());
        Account account1 = accountService.findByNumber(account);
        ClientLoan clientLoan = clientLoanService.findById(loanToPay);
        Double payment = clientLoan.getAmount() / clientLoan.getPayments();
        if (client == null) {
            return new ResponseEntity<>("Client doesn't exist", HttpStatus.FORBIDDEN);
        }
        if (account1 == null) {
            return new ResponseEntity<>("Account doesn't exist", HttpStatus.FORBIDDEN);
        }
        if (account1.getBalance() < payment) {
            return new ResponseEntity<>("Insufficient amount", HttpStatus.FORBIDDEN);
        }
        if (clientLoan.getRestAmount() <= 0) {
            return new ResponseEntity<>("Fully paid loan", HttpStatus.FORBIDDEN);
        }
        Transaction transaction = new Transaction(TransactionType.DEBIT,payment, "cuenta pagada", LocalDateTime.now(),account1.getBalance()-payment);
        account1.setBalance(account1.getBalance() - payment);
        clientLoan.setLoanPayments(clientLoan.getLoanPayments() - 1);
        clientLoan.setRestAmount(clientLoan.getRestAmount() - payment);
        account1.addTransaction(transaction);
        accountService.save(account1);
        clientLoanService.save(clientLoan);
        transactionService.save(transaction);
        return new ResponseEntity<>("Loan payment correct", HttpStatus.OK);
    }
}
