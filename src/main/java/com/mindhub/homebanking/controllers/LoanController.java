package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.LoanApplicationDTO;
import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@RestController @RequestMapping("/api")
public class LoanController {
    @Autowired
    private ClientService clientService;
    @Autowired
    private LoanService loanService;
    @Autowired
    private ClientLoanService clientLoanService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private TransactionService transactionService;

    @RequestMapping("/loans")
    public List<LoanDTO> getLoanDTOs(){
        return loanService.getLoanDTOs() ;
    }
    @Transactional
    @RequestMapping(path = "/loans", method = RequestMethod.POST)
    public ResponseEntity<Object> loanRequest(Authentication authentication, @RequestBody LoanApplicationDTO loanApplicationDTO) {
        Client client = clientService.findByEmail(authentication.getName());
        if (loanApplicationDTO.getPayments() == null) {
            return new ResponseEntity<>("Missing payments", HttpStatus.FORBIDDEN);
        }
        if (loanApplicationDTO.getIdLoanType() == null) {
            return new ResponseEntity<>("Missing id", HttpStatus.FORBIDDEN);
        }
        if (loanApplicationDTO.getAmount() == null) {
            return new ResponseEntity<>("Missing amount", HttpStatus.FORBIDDEN);
        }
        if (loanApplicationDTO.getAccountDestination() == null) {
            return new ResponseEntity<>("Missing destination account", HttpStatus.FORBIDDEN);
        }
        Loan loan = loanService.findById(loanApplicationDTO.getIdLoanType());
        Account account = accountService.findByNumber(loanApplicationDTO.getAccountDestination());
        if (loan == null) {
            return new ResponseEntity<>("Incorrect loan", HttpStatus.FORBIDDEN);
        }
        if (account == null) {
            return new ResponseEntity<>("Account do not exist", HttpStatus.FORBIDDEN);
        }
        if (loanApplicationDTO.getAmount() <= 9999) {
            return new ResponseEntity<>("The amount is insufficient.", HttpStatus.FORBIDDEN);
        }
        if (loanApplicationDTO.getPayments() <= 0) {
            return new ResponseEntity<>("Incorrect payment amount.", HttpStatus.FORBIDDEN);
        }
        if (!loan.getPayments().contains(loanApplicationDTO.getPayments())) {
            return new ResponseEntity<>("The installment quantity is not available for the loan.", HttpStatus.FORBIDDEN);
        }
        if (loanApplicationDTO.getAmount() > loan.getMaxAmount()) {
            return new ResponseEntity<>("The payment amount exceeds the maximum allowed amount.", HttpStatus.FORBIDDEN);
        }
        if (client.getAccounts().stream().noneMatch(account1 -> account1.getNumber().equals(loanApplicationDTO.getAccountDestination()))) {
            return new ResponseEntity<>("The account does not belong to the client.", HttpStatus.FORBIDDEN);
        }
        Double loanApplicationPercentage = (loanApplicationDTO.getAmount() * 20 / 100) + (loanApplicationDTO.getAmount());
        ClientLoan clientLoan = new ClientLoan(loanApplicationPercentage, loanApplicationDTO.getPayments());
        Transaction transaction = new Transaction(TransactionType.CREDIT, loanApplicationDTO.getAmount(), loan.getName() + ":" + "loan approved", LocalDateTime.now());
        account.setBalance(account.getBalance() + loanApplicationDTO.getAmount());
        account.addTransaction(transaction);
        client.addClientLoan(clientLoan);
        loan.addClientLoan(clientLoan);
        transactionService.save(transaction);
        clientLoanService.save(clientLoan);
        accountService.save(account);
        loanService.save(loan);
        clientService.save(client);
        return new ResponseEntity<>("Loan added to the account.", HttpStatus.CREATED);
    }
}