package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.AccountType;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

public class AccountDTO {
    private long id;
    private String number;
    private LocalDate creationDate;
    private double balance;
    private boolean hidden;
    private AccountType accountType;

    private Set<TransactionDTO> transaction;

    public AccountDTO(Account account) {

        this.id = account.getId();

        this.number = account.getNumber();

        this.creationDate = account.getCreationDate();

        this.balance = account.getBalance();

        this.hidden = account.getHidden();

        this.accountType = account.getAccountType();

        this.transaction = account.getTransactions().stream().map(TransactionDTO::new).collect(Collectors.toSet());


    }

    public Set<TransactionDTO> getTransaction() {
        return transaction;
    }

    public long getId() {
        return id;
    }

    public boolean getHidden() {
        return hidden;
    }

    public String getNumber() {
        return number;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public double getBalance() {
        return balance;
    }

    public AccountType getAccountType() {
        return accountType;
    }
}
