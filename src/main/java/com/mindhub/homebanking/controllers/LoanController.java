package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.LoanApplicationDTO;
import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/loans")
    public List<LoanDTO> getLoanDTOs(){
        return loanService.getLoanDTOs() ;
    }
    @Transactional
    @PostMapping("/loans")
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
        if (!account.isActive()) {
            return new ResponseEntity<>("The account is hidden. Unable to request a loan.", HttpStatus.FORBIDDEN);
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
        Double loanApplicationPercentage = (loanApplicationDTO.getAmount() * loan.getPercentage()) + (loanApplicationDTO.getAmount());
        ClientLoan clientLoan = new ClientLoan(loanApplicationPercentage, loanApplicationDTO.getPayments(),loanApplicationDTO.getPayments(),loanApplicationPercentage);
        Transaction transaction = new Transaction(TransactionType.CREDIT, loanApplicationDTO.getAmount(), loan.getName() + ":" + "loan approved", LocalDateTime.now(), account.getBalance() + loanApplicationDTO.getAmount());
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
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/loans/createdLoans")
    public ResponseEntity<Object> createdLoans(Authentication authentication, @RequestBody LoanDTO loanDTO){
        Client client = clientService.findByEmail(authentication.getName());
        if (client == null){
            return new ResponseEntity<>("Client no exist", HttpStatus.UNAUTHORIZED);
        }
        if (authentication == null){
            return new ResponseEntity<>("Authentication required", HttpStatus.UNAUTHORIZED);
        }
        if (!authentication.isAuthenticated()){
            return new ResponseEntity<>("Authentication no exist", HttpStatus.UNAUTHORIZED);
        }
        if (loanDTO.getName().isBlank()){
            return new ResponseEntity<>("Name of loan is missing", HttpStatus.FORBIDDEN);
        }
        if (loanDTO.getMaxAmount().isNaN()){
            return new ResponseEntity<>("Max amount is missing", HttpStatus.FORBIDDEN);
        }
        if (loanDTO.getMaxAmount() <= 0){
            return new ResponseEntity<>("Max amount is missing", HttpStatus.FORBIDDEN);
        }
        if (loanDTO.getPayments().isEmpty()){
            return new ResponseEntity<>("Missing Payments", HttpStatus.FORBIDDEN);
        }
        if (loanDTO.getPercentage().isNaN()){
            return new ResponseEntity<>("Percentage is missing", HttpStatus.FORBIDDEN);
        }

        Loan loan = new Loan(loanDTO.getName(),loanDTO.getMaxAmount(),loanDTO.getPayments(),loanDTO.getPercentage());
        loanService.save(loan);
        return new ResponseEntity<>("Loan created successfully", HttpStatus.CREATED);
    }
}