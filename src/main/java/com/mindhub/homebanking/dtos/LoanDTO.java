package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Loan;

import java.util.List;

public class LoanDTO {
    private Long id;
    private String name;
    private Double amount;
    private List<Integer> payments;
    public LoanDTO(Loan loan){
        id = loan.getId();
        name = loan.getName();
        amount = loan.getMaxAmount();
        payments = loan.getPayments();
    }

    public Long getId() {
        return id;
    }

    public String getLoanName() {
        return name;
    }

    public double getAmount() {
        return amount;
    }

    public List<Integer> getPayments() {
        return payments;
    }
}
